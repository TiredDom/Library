package lib;  // Declare the package

import javax.swing.*;
import java.awt.*;

public class LoadingScreen extends JFrame {
    public LoadingScreen() {
        // Set up the frame
        setTitle("Library System - Loading");
        setSize(400, 400);  // Adjust size to fit the logo and text
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Load the logo image (ensure correct path)
        ImageIcon logoIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/Group_2_Logo.png"));

        // Resize the logo to smaller size (set max width/height to 100px)
        Image logoImage = logoIcon.getImage();
        Image resizedImage = logoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);  // Resize to 100x100 pixels
        ImageIcon resizedLogoIcon = new ImageIcon(resizedImage);

        // Create a panel to hold the message and logo
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create a label to display the loading message with customized fonts
        String message = "<html>"
                + "<div style='text-align: center;'>"
                + "<span style='font-size: 30px; font-weight: bold;'>Library System</span><br><br>"
                + "Made by Group 3<br>"
                + "Bacalando, Ginelle D.<br>"
                + "Gonzales, John Dominic<br>"
                + "Javier, Jonathan T.<br>"
                + "Principe, Franz Jethro V.<br>"
                + "Ranara, Cristina"
                + "</div>"
                + "</html>";

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 16)); // Set font for text

        // Create a label for the resized logo and place it at the top
        JLabel logoLabel = new JLabel(resizedLogoIcon);
        panel.add(logoLabel, BorderLayout.NORTH);  // Add logo to the top of the panel
        panel.add(label, BorderLayout.CENTER);    // Add the text below the logo

        // Add the panel to the frame
        getContentPane().add(panel);

        // Set the frame to be visible
        setVisible(true);
    }
}

