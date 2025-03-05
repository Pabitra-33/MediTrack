package com.HospitalManagementServices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {
    private final Connection connection;

    //parameterized constructor
    public Doctor(Connection connection) {
        this.connection = connection;
    }

    //view doctors
    public void viewDoctors(){
        String query = "Select * from doctors";

        //handling sql exception as we are performing sql query execution
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            //resultSet interface is used to hold the table coming from database and using next() method it points and prints it.
            System.out.println("Doctors: ");
            //doing formatting to output as a table format
            System.out.println("+-----------+--------------------+-----------------+");
            System.out.println("| Doctor Id | Name               | Specialization   |");
            System.out.println("+-----------+--------------------+-----------------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("| %-9s | %-18s | %-16s |\n",id, name, specialization);
                System.out.println("+-----------+--------------------+-----------------+");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    //get Patients By Id
    public boolean getDoctorById(int id){
        String query = "Select * From doctors Where id = ?";
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
