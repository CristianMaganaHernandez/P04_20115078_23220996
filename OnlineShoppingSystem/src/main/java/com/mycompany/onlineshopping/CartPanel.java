package com.mycompany.onlineshopping;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Map;

public class CartPanel extends JPanel {
    private PizzaPlanetGUI gui;

    public CartPanel(PizzaPlanetGUI gui) {
        this.gui = gui;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Cart label at the top
        JLabel cartLabel = new JLabel("Your Cart", JLabel.CENTER);
        cartLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(cartLabel, BorderLayout.NORTH);

        // Cart items panel (where cart items will be displayed)
        gui.cartItemsPanel = new JPanel();
        gui.cartItemsPanel.setLayout(new BoxLayout(gui.cartItemsPanel, BoxLayout.Y_AXIS));
        gui.cartItemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Scroll pane to make the cart scrollable if it has many items
        JScrollPane scrollPane = new JScrollPane(gui.cartItemsPanel);
        scrollPane.setPreferredSize(new Dimension(600, 180));
        add(scrollPane, BorderLayout.CENTER);

        // Total price label at the bottom
        gui.totalLabel = new JLabel("Total: $0.00", JLabel.CENTER);
        gui.totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(gui.totalLabel, BorderLayout.SOUTH);

        // Bottom panel containing the 'Checkout' and 'Back to Menu' buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // 'Checkout' button
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        checkoutButton.addActionListener(e -> {
            gui.orderDateTime = LocalDateTime.now();
            Order order = new Order(gui.customer, gui.cart, gui.orderDateTime);

            // Process the order using OrderController
            gui.orderController.createOrder(gui.customer, gui.cart);

            // Save the order to the file
            gui.orderController.saveOrderToFile(order, "orders.txt");

            // Display the order confirmation screen
            gui.cardLayout.show(gui.mainPanel, "OrderConfirmation");

            // Optionally, display confirmation details
            gui.displayOrderConfirmation(order);
        });

        bottomPanel.add(checkoutButton);

        // 'Back to Menu' button
        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.addActionListener(e -> gui.cardLayout.show(gui.mainPanel, "Menu"));
        bottomPanel.add(backButton);

        // Add bottom panel to the CartPanel
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Method to update the cart display (used when items are added/removed)
    public void updateCartPanel() {
        gui.cartItemsPanel.removeAll(); // Clear previous items

        // Get cart items from the cart object and update the panel
        Map<String, Integer> frequencyMap = gui.cart.getItemFrequency();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double itemPrice = gui.cart.getItemPrice(itemName);

            // Create and add a panel for each cart item
            JPanel cartItemPanel = createCartItemPanel(itemName, quantity, itemPrice);
            gui.cartItemsPanel.add(cartItemPanel);
        }

        // Update total price in the cart panel
        gui.updateTotalPrice();

        // Repaint and revalidate the panel to reflect the changes
        gui.cartItemsPanel.revalidate();
        gui.cartItemsPanel.repaint();
    }

    // Helper method to create a panel for each cart item
    private JPanel createCartItemPanel(String itemName, int quantity, double itemPrice) {
        JPanel cartItemPanel = new JPanel(new BorderLayout());
        cartItemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        cartItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cartItemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        double totalItemPrice = itemPrice * quantity;
        // Label showing item details
        JLabel productLabel = new JLabel(itemName + " - " + quantity + " pcs - $" + String.format("%.2f",totalItemPrice) + " ( $" + itemPrice + " each )");
        productLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cartItemPanel.add(productLabel, BorderLayout.CENTER);

        // Panel for 'Remove' and 'Delete' buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // 'Remove' button (removes one item)
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            gui.cart.removeItem(itemName);
            updateCartPanel();
            gui.updateTotalPrice();
        });

        // 'Delete' button (removes all items)
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            gui.cart.deleteItem(itemName);
            updateCartPanel();
            gui.updateTotalPrice();
        });

        // Add buttons to the button panel
        buttonPanel.add(deleteButton);
        buttonPanel.add(removeButton);

        // Add the button panel to the cart item panel
        cartItemPanel.add(buttonPanel, BorderLayout.EAST);

        return cartItemPanel;
    }
}
