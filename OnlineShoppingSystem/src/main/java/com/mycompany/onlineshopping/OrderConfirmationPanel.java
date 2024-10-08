package com.mycompany.onlineshopping;

import javax.swing.*;
import java.awt.*;

public class OrderConfirmationPanel extends JPanel {

    public OrderConfirmationPanel(PizzaPlanetGUI gui) {
        // Set layout and border for the panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Order confirmation label
        JLabel confirmationLabel = new JLabel("Thank you for your order!", JLabel.CENTER);
        confirmationLabel.setFont(new Font("Arial", Font.BOLD, 20));
        confirmationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(confirmationLabel);

        // Text area to display order details
        JTextArea orderDetailsTextArea = new JTextArea();
        orderDetailsTextArea.setEditable(false);  // Non-editable for order details
        add(new JScrollPane(orderDetailsTextArea));  // Scroll pane for the text area

        // Space between the text area and buttons
        add(Box.createVerticalStrut(20));

        // Button to place a new order
        JButton newOrderButton = new JButton("Place a New Order");
        newOrderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newOrderButton.addActionListener(e -> {
            // Reset cart and clear customer fields when placing a new order
            gui.cart = new Cart();  // Create a new empty cart
            gui.nameField.setText("");  // Clear name field
            gui.addressField.setText("");  // Clear address field
            gui.cardLayout.show(gui.mainPanel, "CustomerInformation");  // Go back to customer info screen
        });
        add(newOrderButton);
    }

    // Optional method to set order details (if needed)
    public void setOrderDetails(String orderDetails) {
        JTextArea orderDetailsTextArea = new JTextArea(orderDetails);
        orderDetailsTextArea.setEditable(false);
        add(new JScrollPane(orderDetailsTextArea));
    }
}
