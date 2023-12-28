import javax.swing.*;
import java.awt.*;
public class login extends JFrame{

    login(){
        setLayout(null);
        getContentPane().setBackground(Color.white);
        setBounds(600, 300, 600, 400);
        setSize(600,300);
        setLocation(300, 150);
        setVisible(true);

        JLabel l1 = new JLabel("Username");
        l1.setBounds(50, 30, 100, 30);
        l1.setForeground(Color.black);
        add(l1);

        JLabel l2 = new JLabel("Password");
        l2.setBounds(50, 90, 100, 30);
        l2.setForeground(Color.black);
        add(l2);

        JTextField t1 = new JTextField();
        t1.setBounds(150, 30, 150, 30);
        t1.setForeground(Color.red);
        add(t1);

        JTextField t2 = new JTextField();
        t2.setBounds(150, 90, 150, 30);
        t2.setForeground(Color.black);
        add(t2);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/second.jpg"));
        Image i2 = i1.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(350, 0, 200, 200);
        add(l3); 

        JButton b1 = new JButton("Login");
        b1.setBackground(Color.black);
        b1.setForeground(Color.white);
        b1.setBounds(150, 150, 100, 30);
        add(b1);
    }
    public static void main(String[] args) throws Exception {
        new login();
    }
}
