package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient(){
        scanner.nextLine();
        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Patient Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Patient Gender: ");
        String gender = scanner.nextLine();

        try{
            String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient Added Successfully!!");
            }else{
                System.out.println("Failed to add Patient!!");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatients(){
        String query = "select * from patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+----------+------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("| %-10s | %-18s | %-8s | %-10s |\n", id, name, age, gender);
                System.out.println("+------------+--------------------+----------+------------+");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updatePatient() {
        System.out.print("Enter Patient ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (!getPatientById(id)) {
            System.out.println("Patient not found!");
            return;
        }

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new gender: ");
        String gender = scanner.nextLine();

        String query = "UPDATE patients SET name = ?, age = ?, gender = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setInt(4, id);

            int result = ps.executeUpdate();
            System.out.println(result > 0 ? "Patient Updated Successfully!" : "Update Failed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePatient() {
        System.out.print("Enter Patient ID to delete: ");
        int id = scanner.nextInt();

        if (!getPatientById(id)) {
            System.out.println("Patient not found!");
            return;
        }

        try {
            // Step 1: delete related appointments
            String deleteAppointments = "DELETE FROM appointments WHERE patient_id = ?";
            PreparedStatement psApp = connection.prepareStatement(deleteAppointments);
            psApp.setInt(1, id);
            psApp.executeUpdate();

            // Step 2: delete patient
            String deletePatient = "DELETE FROM patients WHERE id = ?";
            PreparedStatement psPatient = connection.prepareStatement(deletePatient);
            psPatient.setInt(1, id);

            int result = psPatient.executeUpdate();

            System.out.println(result > 0 ? "Patient Deleted Successfully!" : "Delete Failed!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public boolean getPatientById(int id){
        String query = "SELECT * FROM patients WHERE id = ?";
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
