package com.mycompany.onlineshopping;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Map;

public class PizzaPlanetGUI extends JFrame {

    public CardLayout cardLayout;
    public JPanel mainPanel;

    public Cart cart;
    public Customer customer;
    public LocalDateTime orderDateTime;
    public Menu menu;

    public JTextField nameField;
    public JTextField addressField;

    public JPanel cartItemsPanel;
    public JLabel totalLabel;
    public JLabel cartCounterLabel;
    public int cartCounter = 0;

    public int customerId;
    public int cartId;

    public DerbyDBConnector dbConnector;
    public OrderController orderController;
    public CartPanel cartPanel;

    public PizzaPlanetGUI() {
        dbConnector = DerbyDBConnector.getInstance();
        orderController = new OrderController();

        setTitle("Pizza Planet Pizzeria");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cart = new Cart();
        menu = new Menu(cart, false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add panels to the CardLayout
        mainPanel.add(new IntroductionPanel(this), "Welcome");
        mainPanel.add(new CustomerInfoPanel(this), "CustomerInformation");
        mainPanel.add(new MenuPanel(this), "Menu");
        cartPanel = new CartPanel(this); // Initialize CartPanel
        mainPanel.add(cartPanel, "Cart");
        mainPanel.add(new OrderConfirmationPanel(this), "OrderConfirmation");

        add(mainPanel);
        cardLayout.show(mainPanel, "Welcome");
    }

    // Method to clear customer fields
    public void clearCustomerFields() {
        customer = null;
        if (nameField != null) {
            nameField.setText("");
        }
        if (addressField != null) {
            addressField.setText("");
        }
    }

    // Method to update the cart counter
    public void updateCartCounter() {
        cartCounter++;
        cartCounterLabel.setText(String.valueOf(cartCounter));
    }

    // Method to update the total price
    public void updateTotalPrice() {
        double totalPrice = orderController.calculateTotalOrderCost(cart);
        totalLabel.setText("Total: $" + String.format("%.2f", totalPrice));
    }

    // Method to update the cart panel
    public void updateCartPanel() {
        cartPanel.updateCartPanel();
    }

    // Method to display the order confirmation with details
    public void displayOrderConfirmation(Order order) {
        JTextArea orderDetailsTextArea = new JTextArea();
        orderDetailsTextArea.setText(orderController.getOrderDetails(order));  // Fetch details from OrderController
        orderDetailsTextArea.setEditable(false);

        // Display the total order cost
        totalLabel.setText("Total Order Cost: $" + String.format("%.2f", order.getCart().getTotalPrice()));

        // Show the order confirmation in a dialog
        JOptionPane.showMessageDialog(this, new JScrollPane(orderDetailsTextArea), "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PizzaPlanetGUI().setVisible(true);
        });

        // Add a shutdown hook to close the DerbyDBConnector
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DerbyDBConnector dbConnector = DerbyDBConnector.getInstance();
            dbConnector.closeConnection();
            dbConnector.shutdownDatabase();
        }));
    }
}
