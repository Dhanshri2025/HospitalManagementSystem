package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private Connection connection;

    public Doctor(Connection connection){
        this.connection = connection;
    }

    public void addDoctor() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Doctor Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Specialization: ");
        String specialization = sc.nextLine();

        String query = "INSERT INTO doctors(name, specialization) VALUES(?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, specialization);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Doctor Added Successfully!!");
            } else {
                System.out.println("Failed to Add Doctor!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void viewDoctors(){
        String query = "select * from doctors";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+------------+--------------------+------------------+");
            System.out.println("| Doctor Id  | Name               | Specialization   |");
            System.out.println("+------------+--------------------+------------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("| %-10s | %-18s | %-16s |\n", id, name, specialization);
                System.out.println("+------------+--------------------+------------------+");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateDoctor() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Doctor ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (!getDoctorById(id)) {
            System.out.println("Doctor not found!");
            return;
        }

        System.out.print("Enter new name: ");
        String name = sc.nextLine();
        System.out.print("Enter new specialization: ");
        String specialization = sc.nextLine();

        String query = "UPDATE doctors SET name = ?, specialization = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, specialization);
            ps.setInt(3, id);

            int result = ps.executeUpdate();
            System.out.println(result > 0 ? "Doctor Updated Successfully!" : "Update Failed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDoctor() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Doctor ID to delete: ");
        int id = sc.nextInt();

        if (!getDoctorById(id)) {
            System.out.println("Doctor not found!");
            return;
        }

        String query = "DELETE FROM doctors WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            System.out.println(result > 0 ? "Doctor Deleted Successfully!" : "Delete Failed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public boolean getDoctorById(int id){
        String query = "SELECT * FROM doctors WHERE id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
