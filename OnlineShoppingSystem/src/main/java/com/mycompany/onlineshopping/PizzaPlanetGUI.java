package com.mycompany.onlineshopping;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.util.Map;

public class PizzaPlanetGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Cart cart;
    private Customer customer;
    private Menu menu;
    private JPanel cartItemsPanel;
    private JLabel totalLabel;

    private JTextField nameField;
    private JTextField addressField;

    public PizzaPlanetGUI() {
        setTitle("Pizza Planet Pizzeria");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cart = new Cart();
        menu = new Menu(cart);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createIntroductionPanel(), "Welcome");
        mainPanel.add(createCustomerInfoPanel(), "CustomerInformation");
        mainPanel.add(createMenuPanel(), "Menu");
        mainPanel.add(createCartPanel(), "Cart");
        mainPanel.add(createOrderConfirmationPanel(), "OrderConfirmation");

        add(mainPanel);
        cardLayout.show(mainPanel, "Welcome");
    }

    private JPanel createIntroductionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome to Pizza Planet Pizzeria!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(welcomeLabel, gbc);

        gbc.gridy++;
        JButton startOrderButton = new JButton("Start Order");
        startOrderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startOrderButton.addActionListener(e -> {
            clearCustomerFields(); // Clean Customer information
            cardLayout.show(mainPanel, "CustomerInformation");
        });
        panel.add(startOrderButton, gbc);

        return panel;
    }

    private JPanel createCustomerInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel nameLabel = new JLabel("Name: *");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(nameLabel, gbc);

        gbc.gridy++;
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 30));
        panel.add(nameField, gbc);

        gbc.gridy++;
        JLabel addressLabel = new JLabel("Address: *");
        addressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(addressLabel, gbc);

        gbc.gridy++;
        addressField = new JTextField();
        addressField.setPreferredSize(new Dimension(200, 30));
        panel.add(addressField, gbc);

        gbc.gridy++;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both name and address.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                customer = new Customer(name, address);
                cardLayout.show(mainPanel, "Menu");
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Welcome"));

        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);

        gbc.gridy++;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private void clearCustomerFields() {
        customer = null;
        if (nameField != null) {
            nameField.setText("");
        }
        if (addressField != null) {
            addressField.setText("");
        }
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel menuLabel = new JLabel("Menu", JLabel.CENTER);
        menuLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(menuLabel, BorderLayout.NORTH);

        JPanel itemsPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Two columns for products
        for (Product product : menu.getProducts()) {
            JPanel productPanel = new JPanel();
            productPanel.setBorder(BorderFactory.createTitledBorder(product.getDescription()));
            productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

            JLabel productLabel = new JLabel();
            JButton addButton = new JButton("Add to Cart");
            addButton.addActionListener(e -> cart.addItem(product));

            productPanel.add(productLabel);
            productPanel.add(Box.createVerticalStrut(5));
            productPanel.add(addButton);
            itemsPanel.add(productPanel);
        }

        panel.add(new JScrollPane(itemsPanel), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton cartButton = new JButton("View Cart");
        cartButton.addActionListener(e -> {
            updateCartPanel();
            cardLayout.show(mainPanel, "Cart");
        });
        bottomPanel.add(cartButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "CustomerInformation"));
        bottomPanel.add(backButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel cartLabel = new JLabel("Your Cart", JLabel.CENTER);
        cartLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(cartLabel, BorderLayout.NORTH);

        cartItemsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.add(new JScrollPane(cartItemsPanel), BorderLayout.CENTER);

        totalLabel = new JLabel("Total: $0.00", JLabel.CENTER);
        panel.add(totalLabel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> {
            Order order = new Order(customer, cart);
            order.saveOrder("orders.txt");
            cardLayout.show(mainPanel, "OrderConfirmation");
            displayOrderConfirmation(order);
        });
        bottomPanel.add(checkoutButton);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        bottomPanel.add(backButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateCartPanel() {
        cartItemsPanel.removeAll();

        Map<String, Integer> frequencyMap = cart.getItemFrequency();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double itemPrice = cart.getItemPrice(itemName);
            JLabel productLabel = new JLabel(itemName + " - " + quantity + " pcs - $" + (itemPrice * quantity));
            cartItemsPanel.add(productLabel);
        }

        totalLabel.setText("Total Price: $" + cart.getTotalPrice());

        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    private JPanel createOrderConfirmationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel confirmationLabel = new JLabel("Thank you for your order!", JLabel.CENTER);
        confirmationLabel.setFont(new Font("Arial", Font.BOLD, 20));
        confirmationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(confirmationLabel);

        JTextArea orderDetailsTextArea = new JTextArea();
        orderDetailsTextArea.setEditable(false);
        panel.add(new JScrollPane(orderDetailsTextArea));

        panel.add(Box.createVerticalStrut(20));

        JButton newOrderButton = new JButton("Place a New Order");
        newOrderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newOrderButton.addActionListener(e -> {
            cart = new Cart();
            clearCustomerFields();
            cardLayout.show(mainPanel, "CustomerInformation");
        });
        panel.add(newOrderButton);

        return panel;
    }

    private void displayOrderConfirmation(Order order) {
        JTextArea orderDetailsTextArea = new JTextArea();
        orderDetailsTextArea.setText(getOrderDetails(order));
        orderDetailsTextArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(orderDetailsTextArea), "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    private String getOrderDetails(Order order) {
        StringBuilder details = new StringBuilder();
        details.append("Customer Name: ").append(order.getCustomer().getName()).append("\n");
        details.append("Customer Address: ").append(order.getCustomer().getAddress()).append("\n");
        details.append("Order Items:\n");

        Map<String, Integer> frequencyMap = order.getCart().getItemFrequency();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            details.append(entry.getKey()).append(" - ").append(entry.getValue()).append(" pcs\n");
        }

        details.append("\nTotal Order Cost: $").append(order.getCart().getTotalPrice());

        return details.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PizzaPlanetGUI().setVisible(true);
        });
    }
}