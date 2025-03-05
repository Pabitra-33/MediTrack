package com.HospitalManagementServices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private final Connection connection;
    private final Scanner scanner;

    //parameterized constructor
    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    //addPatient()
    public void addPatient(){
        System.out.print("Enter Patient Name: ");
        String name = scanner.next();
        System.out.print("Enter Patient Age: ");
        int age = scanner.nextInt();
        System.out.print("Enter Patient Gender: ");
        String gender = scanner.next();

        //try-catch block
        try{
            //creating query
            String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";

            //creating a Prepared statement to give instructions to table
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            //setting placeholder(?) values by preparedStatement
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            //check data inserted to table and print some message
            int affectedRows = preparedStatement.executeUpdate();
            //executeUpdate() used to execute the Sql query and returns an integer value resulting how many rows are affected by this query.
            if(affectedRows > 0){
                System.out.println("Patient Added Successfully!");
            }
            else {
                System.out.println("Failed To Add patient");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    //view patients
    public void viewPatients(){
        String query = "Select * from patients";

        //handling sql exception as we are performing sql query execution
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            //resultSet interface is used to hold the table coming from database and using next() method it points and prints it.
            System.out.println("Patients: ");
            //doing formatting to output as a table format
            System.out.println("+------------+--------------------+----------+------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("| %-10s |%-18s | %-8s | %-10s |\n", id, name, age, gender);
                System.out.println("+------------+--------------------+----------+------------+");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    //get Patients By Id
    public boolean getPatientById(int id){
        String query = "Select * From patients Where id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            //by executing it will return a row
            ResultSet resultSet = preparedStatement.executeQuery();
            //if the data has returned, then we return true, else return false
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
