package netpacksniff.ui;

import javax.swing.*;
import java.awt.*;

public class Password extends JFrame {

    private static final long serialVersionUID = 1L; // Added serialVersionUID

    public Password() {
        // Set the title of the JFrame
        setTitle("Login");

        // Create text fields for user ID and password
        JTextField id = new JTextField(15); // 15 columns wide
        JPasswordField pass = new JPasswordField(15); // 15 columns wide

        // Create labels for the text fields
        JLabel l1 = new JLabel("User ID: ");
        JLabel l2 = new JLabel("Password: ");

        // Create a JPanel to hold the labels and text fields
        JPanel pa = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding between components

        // Add the User ID label and text field to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        pa.add(l1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        pa.add(id, gbc);

        // Add the Password label and text field to the panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        pa.add(l2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        pa.add(pass, gbc);

        // Add the panel to the frame
        add(pa);

        // Set the default close operation and the size of the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    public static void main(String[] args) {
        // Create and display the Password frame on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            Password p = new Password();
            p.setVisible(true);
        });
    }
}
