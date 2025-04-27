package ui;

import model.Student;
import storage.FileHandler;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MainUI extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> studentList;
    private List<Student> students;
    private int nextId = 1;

    private JTextField nameField;
    private JTextField ageField;
    private JTextField gradeField;

    public MainUI() {
        setTitle("School Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel titleLabel = new JLabel("Student Management");
        titleLabel.setBounds(180, 10, 200, 30);
        add(titleLabel);

        // Name
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 50, 100, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(100, 50, 150, 25);
        add(nameField);

        // Age
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBounds(20, 90, 100, 25);
        add(ageLabel);

        ageField = new JTextField();
        ageField.setBounds(100, 90, 150, 25);
        add(ageField);

        // Grade
        JLabel gradeLabel = new JLabel("Grade:");
        gradeLabel.setBounds(20, 130, 100, 25);
        add(gradeLabel);

        gradeField = new JTextField();
        gradeField.setBounds(100, 130, 150, 25);
        add(gradeField);

        // Buttons
        JButton addButton = new JButton("Add Student");
        addButton.setBounds(280, 50, 150, 30);
        add(addButton);

        JButton updateButton = new JButton("Update Student");
        updateButton.setBounds(280, 90, 150, 30);
        add(updateButton);

        JButton deleteButton = new JButton("Delete Student");
        deleteButton.setBounds(280, 130, 150, 30);
        add(deleteButton);

        // List
        listModel = new DefaultListModel<>();
        studentList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(studentList);
        scrollPane.setBounds(20, 180, 440, 200);
        add(scrollPane);

        // Load data
        students = FileHandler.readStudents();
        if (!students.isEmpty()) {
            nextId = students.get(students.size() - 1).getId() + 1;
        }
        refreshStudentList();

        // Actions
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());

        studentList.addListSelectionListener(e -> fillSelectedStudentDetails());

        setVisible(true);
    }

    private void addStudent() {
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String grade = gradeField.getText().trim();

        if (name.isEmpty() || ageText.isEmpty() || grade.isEmpty()) {
            showToast("Please fill all fields!");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            Student s = new Student(nextId++, name, age, grade);
            students.add(s);
            FileHandler.writeStudents(students);
            refreshStudentList();
            clearFields();
            showToast("Student Added Successfully!");
        } catch (NumberFormatException e) {
            showToast("Age must be a number!");
        }
    }

    private void updateStudent() {
        int index = studentList.getSelectedIndex();
        if (index == -1) {
            showToast("Please select a student to update!");
            return;
        }

        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String grade = gradeField.getText().trim();

        if (name.isEmpty() || ageText.isEmpty() || grade.isEmpty()) {
            showToast("Please fill all fields!");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            Student s = students.get(index);
            s.setName(name);
            s.setAge(age);
            s.setGrade(grade);
            FileHandler.writeStudents(students);
            refreshStudentList();
            clearFields();
            showToast("Student Updated Successfully!");
        } catch (NumberFormatException e) {
            showToast("Age must be a number!");
        }
    }

    private void deleteStudent() {
        int index = studentList.getSelectedIndex();
        if (index == -1) {
            showToast("Please select a student to delete!");
            return;
        }

        students.remove(index);
        FileHandler.writeStudents(students);
        refreshStudentList();
        clearFields();
        showToast("Student Deleted Successfully!");
    }

    private void refreshStudentList() {
        listModel.clear();
        for (Student s : students) {
            listModel.addElement(s.getId() + ": " + s.getName() + " (" + s.getAge() + ", " + s.getGrade() + ")");
        }
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        gradeField.setText("");
    }

    private void fillSelectedStudentDetails() {
        int index = studentList.getSelectedIndex();
        if (index >= 0) {
            Student s = students.get(index);
            nameField.setText(s.getName());
            ageField.setText(String.valueOf(s.getAge()));
            gradeField.setText(s.getGrade());
        }
    }

    private void showToast(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
