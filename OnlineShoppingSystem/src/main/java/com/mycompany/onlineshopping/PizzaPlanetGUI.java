package com.mycompany.onlineshopping;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class PizzaPlanetGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private Cart cart;
    private Customer customer;
    private Menu menu;
    private JPanel cartItemsPanel;  // Panel to hold cart items
    private JLabel totalLabel;  // Label to display the total cost
    
    public PizzaPlanetGUI() {
        // Setting up the frame
        setTitle("Pizza Planet Pizzeria");
        setSize(600, 400);  // Reduced size for a more compact look
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize core components
        cart = new Cart();
        menu = new Menu(cart);
        
        // Setting up the card layout for switching between screens
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Add panels to the main panel
        mainPanel.add(createIntroductionPanel(), "Welcome");
        mainPanel.add(createCustomerInfoPanel(), "CustomerInformation");
        mainPanel.add(createMenuPanel(), "Menu");
        mainPanel.add(createCartPanel(), "Cart");
        mainPanel.add(createOrderConfirmationPanel(), "OrderConfirmation");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "Welcome"); // First show the introduction panel
    }
    
    private JPanel createIntroductionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome to Pizza Planet Pizzeria!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(welcomeLabel);
        
        panel.add(Box.createVerticalStrut(20));  // Add spacing
        
        JButton startOrderButton = new JButton("Start Order");
        startOrderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "CustomerInformation");
            }
        });
        panel.add(startOrderButton);
        
        return panel;
    }
    
    private JPanel createCustomerInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height));
        
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField addressField = new JTextField();
        addressField.setMaximumSize(new Dimension(Integer.MAX_VALUE, addressField.getPreferredSize().height));
        
        JButton nextButton = new JButton("Next");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Capture customer information
                String name = nameField.getText();
                String address = addressField.getText();
                customer = new Customer(name, address);
                
                cardLayout.show(mainPanel, "Menu");
            }
        });
        
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(5));  // Add spacing
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(10));  // Add spacing
        panel.add(addressLabel);
        panel.add(Box.createVerticalStrut(5));  // Add spacing
        panel.add(addressField);
        panel.add(Box.createVerticalStrut(20));  // Add spacing
        panel.add(nextButton);
        
        return panel;
    }
    
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel menuLabel = new JLabel("Menu", JLabel.CENTER);
        menuLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(menuLabel, BorderLayout.NORTH);
        
        JPanel itemsPanel = new JPanel(new GridLayout(0, 1, 10, 10));  // Adjusted spacing
        for (Product product : menu.getProducts()) {
            JPanel productPanel = new JPanel(new BorderLayout());
            JLabel productLabel = new JLabel(product.getDescription() + " - $" + product.getProductPrice());
            JButton addButton = new JButton("Add to Cart");
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cart.addItem(product);
                }
            });
            productPanel.add(productLabel, BorderLayout.CENTER);
            productPanel.add(addButton, BorderLayout.EAST);
            itemsPanel.add(productPanel);
        }
        
        panel.add(new JScrollPane(itemsPanel), BorderLayout.CENTER);
        
        JButton cartButton = new JButton("View Cart");
        cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCartPanel();  // Update the cart panel to reflect the latest items
                cardLayout.show(mainPanel, "Cart");
            }
        });
        panel.add(cartButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel cartLabel = new JLabel("Your Cart", JLabel.CENTER);
        cartLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(cartLabel, BorderLayout.NORTH);
        
        cartItemsPanel = new JPanel(new GridLayout(0, 1, 10, 10));  // Panel to hold cart items
        panel.add(new JScrollPane(cartItemsPanel), BorderLayout.CENTER);
        
        totalLabel = new JLabel("Total: $0.00", JLabel.CENTER);  // Label to display the total cost
        panel.add(totalLabel, BorderLayout.SOUTH);
        
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Order order = new Order(customer, cart);
                order.saveOrder("orders.txt"); // Save order to a file
                cardLayout.show(mainPanel, "OrderConfirmation");
                displayOrderConfirmation(order);
            }
        });
        panel.add(checkoutButton, BorderLayout.SOUTH);
        
        return panel;
    }

    private void updateCartPanel() {
        cartItemsPanel.removeAll();  // Clear the existing items

        Map<String, Integer> frequencyMap = cart.getItemFrequency();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double itemPrice = cart.getItemPrice(itemName);
            JLabel productLabel = new JLabel(itemName + " - " + quantity + " pcs - $" + (itemPrice * quantity));
            cartItemsPanel.add(productLabel);
        }
        
        totalLabel.setText("Total: $" + cart.getTotalPrice());  // Update the total label
        
        cartItemsPanel.revalidate();  // Refresh the panel
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
        
        panel.add(Box.createVerticalStrut(20));  // Add spacing
        
        JButton newOrderButton = new JButton("Place a New Order");
        newOrderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cart = new Cart(); // Reset cart
                cardLayout.show(mainPanel, "CustomerInformation");
            }
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
        details.append("Customer: ").append(order.getCustomer().getName()).append("\n");
        details.append("Address: ").append(order.getCustomer().getAddress()).append("\n");
        details.append("Items:\n");
        
        Map<String, Integer> frequencyMap = order.getCart().getItemFrequency();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double itemPrice = order.getCart().getItemPrice(itemName);
            details.append("- ").append(itemName).append(" - ").append(quantity).append(" pcs - $").append(itemPrice * quantity).append("\n");
        }
        
        details.append("Total: $").append(order.getCart().getTotalPrice()).append("\n");
        return details.toString();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PizzaPlanetGUI().setVisible(true));
    }
}
