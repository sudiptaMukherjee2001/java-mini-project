package ui;

import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Student;

public class MainUI extends JFrame {
    private List<Student> students;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public MainUI(List<Student> students) {
        this.students = students;

        setTitle("🎓 School Management System");
        setSize(950, 650); // Increased size for better visibility
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatIntelliJLaf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Panel for Add / Update
        JPanel addPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        addPanel.setBorder(BorderFactory.createTitledBorder("➕ Add / Update Student"));
        addPanel.setBackground(Color.WHITE);

        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField gradeField = new JTextField();

        JButton addButton = new JButton("Add Student");
        addButton.setBackground(new Color(76, 175, 80));
        addButton.setForeground(Color.WHITE);

        addPanel.add(new JLabel("Name:"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Age:"));
        addPanel.add(ageField);
        addPanel.add(new JLabel("Grade:"));
        addPanel.add(gradeField);
        addPanel.add(new JLabel());
        addPanel.add(addButton);

        // Table Setup
        String[] columns = {"ID", "Name", "Age", "Grade"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(26);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(850, 250));

        // Panel for Delete / Update
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("❌ Delete / 🔁 Update Student"));
        bottomPanel.setBackground(Color.WHITE);

        JTextField deleteIdField = new JTextField(5);
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(244, 67, 54));
        deleteButton.setForeground(Color.WHITE);

        JTextField updateIdField = new JTextField(5);
        JTextField updateNameField = new JTextField(10);
        JTextField updateAgeField = new JTextField(5);
        JTextField updateGradeField = new JTextField(5);
        JButton updateButton = new JButton("Update");
        updateButton.setBackground(new Color(33, 150, 243));
        updateButton.setForeground(Color.WHITE);

        bottomPanel.add(new JLabel("Delete ID:"));
        bottomPanel.add(deleteIdField);
        bottomPanel.add(deleteButton);
        bottomPanel.add(new JLabel("Update ID:"));
        bottomPanel.add(updateIdField);
        bottomPanel.add(new JLabel("Name:"));
        bottomPanel.add(updateNameField);
        bottomPanel.add(new JLabel("Age:"));
        bottomPanel.add(updateAgeField);
        bottomPanel.add(new JLabel("Grade:"));
        bottomPanel.add(updateGradeField);
        bottomPanel.add(updateButton);

        // Status Label
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(0, 123, 255));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Main Layout Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(addPanel);
        mainPanel.add(tableScroll);
        mainPanel.add(bottomPanel);

        add(mainPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // Action: Add
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            String grade = gradeField.getText().trim();

            if (name.isEmpty() || ageText.isEmpty() || grade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int age = Integer.parseInt(ageText);
                int id = students.size() > 0 ? students.get(students.size() - 1).getId() + 1 : 1;
                Student s = new Student(id, name, age, grade);
                students.add(s);
                refreshTable();
                nameField.setText("");
                ageField.setText("");
                gradeField.setText("");
                showStatus("✅ Student added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Age must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action: Delete
        deleteButton.addActionListener(e -> {
            String idText = deleteIdField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter ID to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(idText);
                boolean removed = students.removeIf(s -> s.getId() == id);
                refreshTable();
                deleteIdField.setText("");
                if (removed) {
                    showStatus("❌ Student deleted.");
                } else {
                    JOptionPane.showMessageDialog(this, "Student ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action: Update
        updateButton.addActionListener(e -> {
            String idText = updateIdField.getText().trim();
            String name = updateNameField.getText().trim();
            String ageText = updateAgeField.getText().trim();
            String grade = updateGradeField.getText().trim();

            if (idText.isEmpty() || name.isEmpty() || ageText.isEmpty() || grade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields and provide Update ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(idText);
                int age = Integer.parseInt(ageText);
                boolean found = false;

                for (int i = 0; i < students.size(); i++) {
                    Student s = students.get(i);
                    if (s.getId() == id) {
                        students.set(i, new Student(id, name, age, grade));
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    JOptionPane.showMessageDialog(this, "Student ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    refreshTable();
                    updateIdField.setText("");
                    updateNameField.setText("");
                    updateAgeField.setText("");
                    updateGradeField.setText("");
                    showStatus("🔁 Student updated successfully!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID and Age must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getAge(),
                    s.getGrade()
            });
        }
    }

    private void showStatus(String message) {
        statusLabel.setText(message);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                statusLabel.setText(" ");
            }
        }, 3000);
    }
}