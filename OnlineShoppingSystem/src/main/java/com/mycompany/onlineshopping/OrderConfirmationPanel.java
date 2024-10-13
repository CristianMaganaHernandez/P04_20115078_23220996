package com.mycompany.onlineshopping;

import javax.swing.*;
import java.awt.*;

public class OrderConfirmationPanel extends JPanel {

    private Image thankYouImage;

    public OrderConfirmationPanel(PizzaPlanetGUI gui) {
        // Load the thank you image
        thankYouImage = new ImageIcon(getClass().getClassLoader().getResource("Thank_you.jpg")).getImage();

        // Set layout to GridBagLayout for better control over component placement
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Padding around components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Center the components horizontally and vertically
        gbc.fill = GridBagConstraints.NONE;     // Don't stretch the components

        // Order confirmation label
        //JLabel confirmationLabel = new JLabel("Thank you for your order!", JLabel.CENTER);
        //confirmationLabel.setFont(new Font("Arial", Font.BOLD, 20));
        //confirmationLabel.setForeground(Color.BLACK); // Set text color to stand out against the image
        //add(confirmationLabel, gbc);

        // Move to the next row for the button
        gbc.gridy++;
        
        JButton newOrderButton = new JButton("Place a New Order");
        newOrderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newOrderButton.addActionListener(e -> {
            // Reset cart and clear customer fields when placing a new order
            gui.cart = new Cart();  // Create a new empty cart
            gui.nameField.setText("");  // Clear name field
            gui.addressField.setText("");  // Clear address field
            gui.cardLayout.show(gui.mainPanel, "CustomerInformation");  // Go back to customer info screen
        });
        add(newOrderButton, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the thank you image, scaling it to the full size of the panel
        if (thankYouImage != null) {
            int width = getWidth();
            int height = getHeight();
            g.drawImage(thankYouImage, 0, 0, width, height, this);  // Scale the image to cover the entire panel
        }
    }

    // Optional method to set order details (if needed)
    public void setOrderDetails(String orderDetails) {
        JTextArea orderDetailsTextArea = new JTextArea(orderDetails);
        orderDetailsTextArea.setEditable(false);
        add(new JScrollPane(orderDetailsTextArea));
    }
}
