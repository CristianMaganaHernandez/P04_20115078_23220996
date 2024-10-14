/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.onlineshopping;

/**
 *
 * @author crist
 */
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class IntroductionPanel extends JPanel {
    private Image backgroundImage;

    public IntroductionPanel(PizzaPlanetGUI gui) {
        // Load the welcome image
        try {
            backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("TSwift_welcome.jpg")); // Replace with your actual image file
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        // Add the Start Order button at the bottom
        JButton startOrderButton = new JButton("Start Order");
        startOrderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startOrderButton.addActionListener(e -> {
            gui.nameField.setText("");
            gui.addressField.setText("");
            gui.cardLayout.show(gui.mainPanel, "CustomerInformation");
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(startOrderButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image, scaling it to the size of the panel
        if (backgroundImage != null) {
            int width = getWidth();
            int height = getHeight();
            g.drawImage(backgroundImage, 0, 0, width, height, this);
        }
    }
}
