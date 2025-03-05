package com.HospitalManagementDriver;

import com.HospitalManagementServices.Doctor;
import com.HospitalManagementServices.Patient;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementApp {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";//mysql connection url
    private static final String username = "root";
    private static final String password = "Secure10@database";

    //main method
    public static void main(String[] args) {
        //loading all drivers for performing all operations
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();//printing the stack frame which is affected by exception
        }

        Scanner scanner = new Scanner(System.in);
        //making connection
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            //creating patient and doctor object
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);//objects are ready

            //show menu programs to user
            while (true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients ");
                System.out.println("3. View Doctors ");
                System.out.println("4. Book Appointment ");
                System.out.println("5. Exit");
                //take user's choice input
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        //Add Patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //View Patients
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        //View Doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        //Book Appointment
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("Exited Successfully!!!");
                        return;
                    default:
                        System.out.println("Enter a valid choice!!!");
                        break;
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();//printing the stack frame which is affected by exception
        }
    }

    //book Appointment method
    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
        //checking both the patient and doctor is present in the database or not
        System.out.print("Enter Patient Id: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();

        //checking whether the patient & doctor exist or not
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId) )
        {
            //if both are present in database then check for doctor availability
            if(checkDoctorAvailability(doctorId, appointmentDate, connection)){
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected > 0){
                        System.out.println("Appointment Booked");
                    }else {
                        System.out.println("Failed To Book An Appointment!!!");
                    }

                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor is not available on this date!");
            }

        }else {
            System.out.println("Either doctor or patient doesn't exist !!!");
        }
    }

    //check doctor availability
    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT Count(*) FROM appointments WHERE doctor_id = ? AND patient_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);

            //ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count == 0){
                    return true;
                }else {
                    return false;
                }
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
