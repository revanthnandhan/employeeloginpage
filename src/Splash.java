
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Splash extends JFrame implements ActionListener{
    Splash(){
        getContentPane().setBackground(Color.white);
        setLayout(null);
        JLabel l1 = new JLabel("Library Management System");
        l1.setBounds(250, 50, 600, 100);
        l1.setFont(new Font("Serif", Font.PLAIN, 50));
        l1.setForeground(Color.blue);
        add(l1);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/splash.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1270, 450, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l2 = new JLabel(i3);
        l2.setBounds(0, 150, 1270, 450);
        add(l2);   
        
        JButton b1 = new JButton("CLICK HERE TO CONTINUE");
        b1.setBackground(Color.white);
        b1.setForeground(Color.black);
        b1.setBounds(500, 500, 200, 100);
        b1.addActionListener(this);
        add(b1);      

    setSize(1170,650);
    setLocation(150,50);
    setVisible(true);
     while(true){
            l1.setVisible(false);
            try{
                Thread.sleep(500);
            }catch(Exception e){}
            l1.setVisible(true);
            try{
                Thread.sleep(500);
            }catch(Exception e){}
        }  
    
    }
    public void actionPerformed(ActionEvent ae){
        setVisible(false);
        new login();
        
        
    }
    public static void main(String args[]){
        new Splash();
    }
    
}

    
