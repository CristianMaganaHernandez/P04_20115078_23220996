package com.mycompany.onlineshopping;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Map;

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
    private int customerId;
    private int cartId;

    // Variables added
    private JLabel cartIconLabel;
    private JLabel cartCounterLabel;
    private int cartCounter = 0;

    private OrderController orderController; // Add a reference to the OrderController

    public PizzaPlanetGUI() {
        dbConnector = DerbyDBConnector.getInstance(); // Get the singleton instance of the database connector
        orderController = new OrderController(); // Initialize the OrderController

        // GUI setup
        setTitle("Pizza Planet Pizzeria");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cart = new Cart();
        menu = new Menu(cart, false);

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
            clearCustomerFields();
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

                // Use the OrderController to insert the customer into the database
                customerId = orderController.createCustomer(name, address);
                if (customerId == -1) {
                    JOptionPane.showMessageDialog(this, "Failed to insert customer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create a new cart using OrderController
                cartId = orderController.createCart(customerId);
                if (cartId == -1) {
                    JOptionPane.showMessageDialog(this, "Failed to create cart.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
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

        JPanel cartPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        ImageIcon cartIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("CART.jpeg"))
                .getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH));
        cartIconLabel = new JLabel(cartIcon);

        cartCounterLabel = new JLabel(String.valueOf(cartCounter));
        cartCounterLabel.setFont(new Font("Arial", Font.BOLD, 18));
        cartCounterLabel.setForeground(Color.BLACK);

        cartPanel.add(cartIconLabel);
        cartPanel.add(cartCounterLabel);
        panel.add(cartPanel, BorderLayout.NORTH);

        JPanel itemsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        for (Product product : menu.getProducts()) {
            JPanel productPanel = new JPanel();
            productPanel.setBorder(BorderFactory.createTitledBorder(product.getDescription()));
            productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

            JLabel productLabel = new JLabel();
            JButton addButton = new JButton("Add to Cart");
            addButton.addActionListener(e -> {
                int productId = orderController.getProductIdFromDatabase(product.getProductName());
                if (productId != -1) {
                    // Add item to the in-memory cart
                    cart.addItem(product);
                    
                    // Add item to the database cart
                    orderController.addItemToCart(cartId, productId, 1);
                    
                    // Update cart counter and total price
                    updateCartCounter();
                    updateTotalPrice();
                } else {
                    JOptionPane.showMessageDialog(this, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

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

    private void updateCartCounter() {
        cartCounter++;
        cartCounterLabel.setText(String.valueOf(cartCounter));
    }

    private void updateTotalPrice() {
        double totalPrice = orderController.calculateTotalOrderCost(cart);  // Fetch total price from OrderController
        totalLabel.setText("Total: $" + String.format("%.2f", totalPrice));  // Update the GUI label with the total price
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel cartLabel = new JLabel("Your Cart", JLabel.CENTER);
        cartLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(cartLabel, BorderLayout.NORTH);

        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setPreferredSize(new Dimension(600, 180));
        panel.add(scrollPane, BorderLayout.CENTER);

        totalLabel = new JLabel("Total: $0.00", JLabel.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(totalLabel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        checkoutButton.addActionListener(e -> {
            orderDateTime = LocalDateTime.now();
            Order order = new Order(customer, cart, orderDateTime);
            orderController.createOrder(customer, cart); // Process the order using OrderController
            
            // Save the order using the saveOrderToFile method from OrderController
            orderController.saveOrderToFile(order, "orders.txt");
            
            updateTotalPrice(); // Refresh total price before displaying confirmation
            cardLayout.show(mainPanel, "OrderConfirmation");
            displayOrderConfirmation(order);
        });

        bottomPanel.add(checkoutButton);

        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        bottomPanel.add(backButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
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

        // Update total price
        updateTotalPrice();

        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    private JPanel createCartItemPanel(String itemName, int quantity, double itemPrice) {
        JPanel cartItemPanel = new JPanel(new BorderLayout());
        cartItemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        cartItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cartItemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel productLabel = new JLabel(itemName + " - " + quantity + " pcs - $" + (itemPrice * quantity) + " ( $" + itemPrice + " each )");
        productLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cartItemPanel.add(productLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            cart.removeItem(itemName);
            updateCartPanel();
            updateTotalPrice();
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            cart.deleteItem(itemName);
            updateCartPanel();
            updateTotalPrice();
        });

        buttonPanel.add(deleteButton);
        buttonPanel.add(removeButton);

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
        orderDetailsTextArea.setText(orderController.getOrderDetails(order)); // Fetch details from controller
        orderDetailsTextArea.setEditable(false);

        totalLabel.setText("Total Order Cost: $" + String.format("%.2f", order.getCart().getTotalPrice()));

        JOptionPane.showMessageDialog(this, new JScrollPane(orderDetailsTextArea), "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PizzaPlanetGUI().setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DerbyDBConnector dbConnector = DerbyDBConnector.getInstance();
            dbConnector.closeConnection();
            dbConnector.shutdownDatabase();
        }));
    }
}
