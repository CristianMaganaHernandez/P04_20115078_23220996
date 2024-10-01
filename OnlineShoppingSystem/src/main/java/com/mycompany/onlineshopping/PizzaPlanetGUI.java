package com.mycompany.onlineshopping;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.util.Map;
import java.sql.*;

public class PizzaPlanetGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Cart cart;
    private Customer customer;
    private LocalDateTime orderDateTime;
    private Menu menu;
    private JPanel cartItemsPanel;
    private JLabel totalLabel;

    private JTextField nameField;
    private JTextField addressField;

    private DerbyDBConnector dbConnector;
    private int customerId; // Store the customer ID from the database
    private int cartId; // Store the cart ID after inserting into the Cart table

    public PizzaPlanetGUI() {
        dbConnector = DerbyDBConnector.getInstance(); // Get the singleton instance of the database connector

        // Drop existing tables and recreate them at the start of the program
//        try {
//            dbConnector.dropTables();
//            dbConnector.createTables();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error initializing database tables", "Database Error", JOptionPane.ERROR_MESSAGE);
//        }
        // GUI setup
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
                customer = new Customer(name, address); // Create a new Customer object

                // Insert customer into the database and store the generated CustomerID
                try {
                    customerId = dbConnector.insertCustomer(name, address);
                    if (customerId == -1) {
                        JOptionPane.showMessageDialog(this, "Failed to insert customer.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Insert a new cart into the Cart table for this customer
                    cartId = dbConnector.insertCart(customerId);
                    if (cartId == -1) {
                        JOptionPane.showMessageDialog(this, "Failed to insert cart.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                cardLayout.show(mainPanel, "Menu"); // Go to the Menu screen
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

        // Cart label
        JLabel cartLabel = new JLabel("Your Cart", JLabel.CENTER);
        cartLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(cartLabel, BorderLayout.NORTH);

        // Cart items panel with BoxLayout for vertical stacking
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS)); // Vertical stacking
        cartItemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add cartItemsPanel to a scroll pane with a fixed height
        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setPreferredSize(new Dimension(600, 180)); // Adjust the height to reduce the total space
        panel.add(scrollPane, BorderLayout.CENTER);

        // Total label
        totalLabel = new JLabel("Total: $0.00", JLabel.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(totalLabel, BorderLayout.SOUTH);

        // Bottom panel with checkout and back buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        checkoutButton.setPreferredSize(new Dimension(150, 30));  //Preferred size
        checkoutButton.setMinimumSize(new Dimension(130, 30));   //Minimum size Need to be tested in other size of monitor
        checkoutButton.addActionListener(e -> {
            try {
                // Insert the order into the Orders table and get the generated OrderID
                int orderId = dbConnector.insertOrder(customerId, cart.getTotalPrice());
                System.out.println("Order ID: " + orderId); // Add debug print

                // Insert each cart item into the CartItem table and the OrderItem table
                Map<String, Integer> frequencyMap = cart.getItemFrequency();
                for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
                    String productName = entry.getKey();
                    int quantity = entry.getValue();
                    double itemPrice = cart.getItemPrice(productName);

                    // Retrieve the ProductID from the database
                    int productId = getProductIdFromDatabase(productName);
                    System.out.println("Product ID: " + productId); // Add debug print

                    // If the productId is valid, insert it into the CartItem and OrderItem tables
                    if (productId != -1) {
                        dbConnector.insertCartItem(cartId, productId, quantity);  // Insert into CartItem

                        // Insert into the OrderItem table (OrderID, ProductID, Quantity)
                        dbConnector.insertOrderItem(orderId, productId, quantity);  // Insert into OrderItem
                    } else {
                        JOptionPane.showMessageDialog(null, "Product not found: " + productName, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                // Proceed to the order confirmation page
                Order order = new Order(customer, cart, orderDateTime);
                order.saveOrder("orders.txt"); // Save the order to file as well
                cardLayout.show(mainPanel, "OrderConfirmation");
                displayOrderConfirmation(order);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        bottomPanel.add(checkoutButton);

        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(150, 30));  //Preferred size
        backButton.setMinimumSize(new Dimension(130, 30));  //Minimum size Need to be tested in other size of monitor
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        bottomPanel.add(backButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private int getProductIdFromDatabase(String productName) throws SQLException {
        String query = "SELECT ProductID FROM Product WHERE TRIM(LOWER(Name)) = TRIM(LOWER(?))";
        try ( PreparedStatement pstmt = dbConnector.getConnection().prepareStatement(query)) {
            pstmt.setString(1, productName.trim().toLowerCase()); // Normalize case and trim
            System.out.println("Querying for product: " + productName); // Add debug print

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Product found: " + productName + " with ID " + rs.getInt("ProductID")); // Add debug print
                return rs.getInt("ProductID");
            } else {
                System.out.println("Product not found: " + productName); // Add debug print
            }
        }
        return -1; // Return -1 if the product is not found
    }

    private void updateCartPanel() {
        cartItemsPanel.removeAll(); // Clear previous items

        // Get cart items
        Map<String, Integer> frequencyMap = cart.getItemFrequency();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double itemPrice = cart.getItemPrice(itemName);

            // Add each cart item to the cartItemsPanel
            JPanel cartItemPanel = createCartItemPanel(itemName, quantity, itemPrice);
            cartItemsPanel.add(cartItemPanel);
        }

        totalLabel.setText("Total Price: $" + cart.getTotalPrice());

        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    private JPanel createCartItemPanel(String itemName, int quantity, double itemPrice) {
        // Create a panel for each cart item
        JPanel cartItemPanel = new JPanel(new BorderLayout());
        cartItemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // Set a thinner height for the cart item panel
        cartItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Reduce the height to make it thinner
        cartItemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create label to display item details
        JLabel productLabel = new JLabel(itemName + " - " + quantity + " pcs - $" + (itemPrice * quantity) + " ( $" + itemPrice + " each )");
        productLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cartItemPanel.add(productLabel, BorderLayout.CENTER);

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Create remove button for each item
        JButton removeButton = new JButton("Remove");
        removeButton.setFont(new Font("Arial", Font.PLAIN, 12)); // Slightly reduce the button font size
        removeButton.setPreferredSize(new Dimension(80, 30)); // Keep the button size consistent

        // Action listener for removing all items of a specific type
        removeButton.addActionListener(e -> {
            cart.removeItem(itemName); // Remove the item from the cart
            updateCartPanel(); // Refresh the cart display
        });

        // Create delete button for removing one item at a time
        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 12)); // Slightly reduce the button font size
        deleteButton.setPreferredSize(new Dimension(80, 30)); // Keep the button size consistent

        // Action listener for deleting one item at a time
        deleteButton.addActionListener(e -> {
            cart.deleteItem(itemName); // Decrease item quantity by 1
            updateCartPanel(); // Refresh the cart display
        });

        // Add both buttons to the button panel
        buttonPanel.add(deleteButton);
        buttonPanel.add(removeButton);

        // Add the button panel to the cart item panel
        cartItemPanel.add(buttonPanel, BorderLayout.EAST);

        return cartItemPanel;
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

        details.append("\nTotal Order Cost: $").append(String.format("%.2f", order.getCart().getTotalPrice())).append("\n");
        details.append(order.getOrderTimestamp());
        return details.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PizzaPlanetGUI().setVisible(true);
        });

        // Register a shutdown hook to close the database connection and shutdown Derby when the program exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DerbyDBConnector dbConnector = DerbyDBConnector.getInstance();
            dbConnector.closeConnection();  // Close the database connection
            dbConnector.shutdownDatabase(); // Shutdown the database
        }));
    }
}
