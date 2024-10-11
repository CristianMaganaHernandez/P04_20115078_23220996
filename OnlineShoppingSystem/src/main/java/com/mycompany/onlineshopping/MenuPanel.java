package com.mycompany.onlineshopping;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    public MenuPanel(PizzaPlanetGUI gui) {
        // Set layout and border for the panel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Menu label at the top
        JLabel menuLabel = new JLabel("Menu", JLabel.CENTER);
        menuLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(menuLabel, BorderLayout.NORTH);

        // Cart counter at the top right corner
        JPanel cartPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ImageIcon cartIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("CART.jpeg"))
                .getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH));
        JLabel cartIconLabel = new JLabel(cartIcon);

        gui.cartCounterLabel = new JLabel(String.valueOf(gui.cartCounter));
        gui.cartCounterLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gui.cartCounterLabel.setForeground(Color.BLACK);

        cartPanel.add(cartIconLabel);
        cartPanel.add(gui.cartCounterLabel);
        add(cartPanel, BorderLayout.NORTH);

        // Panel for displaying menu items
        JPanel itemsPanel = new JPanel(new GridLayout(0, 2, 10, 10));  // GridLayout with 2 columns

        // Add products to the menu
        for (Product product : gui.menu.getProducts()) {
            // Create a panel for each product
            JPanel productPanel = new JPanel();
            productPanel.setBorder(BorderFactory.createTitledBorder(product.getDescription()));
            productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

            //JLabel productLabel = new JLabel(product.getProductName());
            JButton addButton = new JButton("Add to Cart");

            // Add action to the 'Add to Cart' button
            addButton.addActionListener(e -> {
                int productId = gui.orderController.getProductIdFromDatabase(product.getProductName());
                if (productId != -1) {
                    // Add item to the in-memory cart
                    gui.cart.addItem(product);
                    // Add item to the database cart
                    gui.orderController.addItemToCart(gui.cartId, productId, 1);
                    // Update cart counter and total price
                    gui.updateCartCounter(true);  // Update the counter for adding an item
                    gui.updateTotalPrice();
                } else {
                    JOptionPane.showMessageDialog(gui, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Add components to the product panel
            //productPanel.add(productLabel);
            productPanel.add(Box.createVerticalStrut(5));
            productPanel.add(addButton);

            // Add the product panel to the itemsPanel
            itemsPanel.add(productPanel);
        }

        // Add items panel to the scroll pane
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with 'View Cart' and 'Back' buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton cartButton = new JButton("View Cart");
        cartButton.addActionListener(e -> {
            gui.updateCartPanel();
            gui.cardLayout.show(gui.mainPanel, "Cart");
        });
        bottomPanel.add(cartButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> gui.cardLayout.show(gui.mainPanel, "CustomerInformation"));
        bottomPanel.add(backButton);

        // Add bottom panel to the MenuPanel
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
