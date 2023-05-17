/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg2dgamesframework;


import javax.swing.*;
import java.awt.*;

public class MainScreen extends JFrame {
    
    public MainScreen() {
        super("Main Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        // create components
        JLabel titleLabel = new JLabel("Welcome to My Application");
        JTextField nameField = new JTextField(20);
        JButton submitButton = new JButton("Submit");
        
        // add components to frame
        JPanel panel = new JPanel();
        panel.add(titleLabel);
        panel.add(nameField);
        panel.add(submitButton);
        getContentPane().add(panel, BorderLayout.CENTER);
    }
}