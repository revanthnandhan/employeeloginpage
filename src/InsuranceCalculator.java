import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsuranceCalculator extends JFrame {
    private JTextField ageField, coverageField, premiumField;

    public InsuranceCalculator() {
        setTitle("Insurance Calculator");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI();
    }

    private void createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Age:"));
        ageField = new JTextField();
        panel.add(ageField);

        panel.add(new JLabel("Coverage Amount:"));
        coverageField = new JTextField();
        panel.add(coverageField);

        panel.add(new JLabel()); // Empty label for spacing
        panel.add(new JLabel());

        JButton calculateButton = new JButton("Calculate Premium");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateAndInsertPremium();
            }
        });
        panel.add(calculateButton);

        panel.add(new JLabel()); // Empty label for spacing
        panel.add(new JLabel());

        panel.add(new JLabel("Premium:"));
        premiumField = new JTextField();
        premiumField.setEditable(false);
        panel.add(premiumField);

        add(panel);
    }

    private void calculateAndInsertPremium() {
        try {
            int age = Integer.parseInt(ageField.getText());
            double coverageAmount = Double.parseDouble(coverageField.getText());

            // Basic premium calculation logic (modify as needed)
            double premium = age * 5 + coverageAmount * 0.1;

            premiumField.setText(String.format("%.2f", premium));

            // JDBC connection and insertion
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/227y1a6797", "root", "mysql")) {
                String query = "INSERT INTO insurance_rates (age, coverage_amount, premium_amount) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, age);
                    preparedStatement.setDouble(2, coverageAmount);
                    preparedStatement.setDouble(3, premium);

                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Data successfully inserted into the database.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error inserting data into the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for age and coverage amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InsuranceCalculator calculator = new InsuranceCalculator();
            calculator.setVisible(true);
        });
    }
}


