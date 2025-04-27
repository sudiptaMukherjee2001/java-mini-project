package storage;

import model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String FILE_NAME = "students.txt";

    public static List<Student> readStudents() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student s = Student.fromString(line);
                if (s != null) {
                    students.add(s);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing student file found. A new one will be created.");
        }
        return students;
    }

    public static void writeStudents(List<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                writer.write(s.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
