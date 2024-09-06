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

    // Use GridBagLayout for a 2x2 grid structure
    JPanel itemsPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;  // Make the components stretch to fit
    gbc.insets = new Insets(10, 10, 10, 10);  // Padding between items
    gbc.weightx = 0.5;  // Allow horizontal stretching, but limited to 50% per column
    gbc.weighty = 1.0;  // Allow vertical stretching
    gbc.gridwidth = 1;  // Each item spans one column
    gbc.gridheight = 2;  // Each item takes two rows (one for the label, one for the button)

    int row = 0;
    int col = 0;

    for (Product product : menu.getProducts()) {
        // Create the product label for description
        JLabel productLabel = new JLabel(product.getDescription(), JLabel.CENTER);

        // Create the "Add to Cart" button
        JButton addButton = new JButton("Add to Cart");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cart.addItem(product);  // Add item to cart
            }
        });

        // Add product description to grid (top part)
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.gridheight = 1;  // Product label takes one row
        itemsPanel.add(productLabel, gbc);

        // Add "Add to Cart" button below the description (second row)
        gbc.gridy = row + 1;  // Move to next row for the button
        itemsPanel.add(addButton, gbc);

        // Move to the next column
        col++;

        // If we've filled two columns, reset the column and move to the next row
        if (col >= 2) {
            col = 0;
            row += 2;  // Move two rows down since each item takes two rows
        }
    }

    // Add the items panel inside a scroll pane
    JScrollPane scrollPane = new JScrollPane(itemsPanel);
    panel.add(scrollPane, BorderLayout.CENTER);

    // Add a "View Cart" button at the bottom
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
    
    // Initialize the cart items panel
    cartItemsPanel = new JPanel(new GridBagLayout());  // Updated layout
    cartItemsPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));  // Border for clarity
    
    // Add the cart items panel inside a scroll pane
    JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
    panel.add(scrollPane, BorderLayout.CENTER);  // Added scroll pane to handle larger content
    
    // Initialize the total label
    totalLabel = new JLabel("Total: $0.00", JLabel.CENTER);
    panel.add(totalLabel, BorderLayout.SOUTH);
    
    // Now that all components are initialized, update the cart panel
    updateCartPanel();  // Populate cart items panel after initialization
    
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

    if (cart == null || cart.getItemFrequency().isEmpty()) {
        JLabel emptyCartLabel = new JLabel("Your cart is empty.", JLabel.CENTER);
        cartItemsPanel.setLayout(new GridBagLayout());  // Use GridBagLayout for control
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        cartItemsPanel.add(emptyCartLabel, gbc);
        totalLabel.setText("Total: $0.00");
    } else {
        cartItemsPanel.setLayout(new GridBagLayout());  // Use GridBagLayout for more control
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;  // Allow items to grow horizontally and vertically
        gbc.insets = new Insets(5, 5, 5, 5);  // Add padding between cells
        gbc.weightx = 1.0;  // Allow horizontal stretching of components
        gbc.weighty = 1.0;  // Allow vertical stretching of components

        // Add headers for the grid
        gbc.gridx = 0;
        gbc.gridy = 0;
        cartItemsPanel.add(new JLabel("Item", JLabel.CENTER), gbc);
        
        gbc.gridx = 1;
        cartItemsPanel.add(new JLabel("Quantity", JLabel.CENTER), gbc);
        
        gbc.gridx = 2;
        cartItemsPanel.add(new JLabel("Price", JLabel.CENTER), gbc);
        
        // Reset grid position for the cart items
        int row = 1;  // Start from row 1 (after the headers)
        Map<String, Integer> frequencyMap = cart.getItemFrequency();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double itemPrice = cart.getItemPrice(itemName);

            // Add the item name to the grid
            gbc.gridx = 0;
            gbc.gridy = row;
            JLabel itemLabel = new JLabel(itemName, JLabel.CENTER);
            cartItemsPanel.add(itemLabel, gbc);

            // Add the quantity to the grid
            gbc.gridx = 1;
            JLabel quantityLabel = new JLabel(String.valueOf(quantity), JLabel.CENTER);
            cartItemsPanel.add(quantityLabel, gbc);

            // Add the total price for the item to the grid
            gbc.gridx = 2;
            String totalAmount = String.format("$%.2f",(itemPrice * quantity));
            JLabel priceLabel = new JLabel(totalAmount, JLabel.CENTER);
            cartItemsPanel.add(priceLabel, gbc);

            row++;  // Move to the next row for the next item
        }

        // Update the total label
        totalLabel.setText("Total: $" + cart.getTotalPrice());
    }

    // Refresh the panel to reflect updates
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
        details.append("Customer Name: ").append(order.getCustomer().getName()).append("\n");
        details.append("Customer Address: ").append(order.getCustomer().getAddress()).append("\n");
        details.append("Order Items:\n");
        
        Map<String, Integer> frequencyMap = order.getCart().getItemFrequency();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double itemPrice = order.getCart().getItemPrice(itemName);
            details.append("- ").append(itemName).append(" - ").append(quantity).append(" pcs - $").append(itemPrice * quantity).append("\n");
        }
        
        details.append("Total Order: $").append(String.format("%.2f", order.getCart().getTotalPrice())).append("\n");
        return details.toString();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PizzaPlanetGUI().setVisible(true));
    }
}
