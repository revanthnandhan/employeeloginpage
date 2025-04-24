import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.security.MessageDigest;
import java.util.prefs.Preferences;

interface AuthService {
    boolean login(String username, String password);
    boolean register(String username, String password);
    boolean userExists(String username);
}

class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/employee_db";
    private static final String USER = "root";
    private static final String PASS = "root";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

class AuthServiceImpl implements AuthService {
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean login(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM employees WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean register(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            if (userExists(username)) return false;
            String sql = "INSERT INTO employees (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean userExists(String username) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM employees WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

class AdminDashboard extends JFrame {
    public AdminDashboard(String username) {
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        add(welcomeLabel);
    }
}

class RegisterPage extends JFrame {
    public RegisterPage() {
        setTitle("Register");
        setSize(400, 250);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 25);
        add(userLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 50, 150, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 25);
        add(passLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 150, 25);
        add(passwordField);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(150, 150, 100, 25);
        add(registerBtn);

        AuthService authService = new AuthServiceImpl();

        registerBtn.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            boolean success = authService.register(user, pass);
            JOptionPane.showMessageDialog(null, success ? "Registered successfully!" : "User already exists.");
        });
    }
}

class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox rememberMe;
    private Preferences prefs = Preferences.userRoot().node(this.getClass().getName());

    public LoginPage() {
        setTitle("Employee Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 25);
        add(userLabel);

        usernameField = new JTextField(prefs.get("username", ""));
        usernameField.setBounds(150, 50, 150, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 150, 25);
        add(passwordField);

        rememberMe = new JCheckBox("Remember Me");
        rememberMe.setBounds(150, 130, 150, 25);
        add(rememberMe);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 160, 100, 25);
        add(loginBtn);

        JButton registerBtn = new JButton("Sign Up");
        registerBtn.setBounds(150, 200, 100, 25);
        add(registerBtn);

        AuthService authService = new AuthServiceImpl();

        loginBtn.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            boolean success = authService.login(user, pass);
            if (success) {
                if (rememberMe.isSelected()) prefs.put("username", user);
                new AdminDashboard(user).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Login Failed!");
            }
        });

        registerBtn.addActionListener(e -> new RegisterPage().setVisible(true));
    }
}

class SplashScreen extends JWindow {
    public SplashScreen() {
        JLabel label = new JLabel("Loading Employee System...", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        getContentPane().add(label);
        setBounds(500, 300, 400, 200);
    }

    public void showSplash() {
        setVisible(true);
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
        setVisible(false);
    }
}

public class Main {
    public static void main(String[] args) {
        new SplashScreen().showSplash();
        new LoginPage().setVisible(true);
    }
}