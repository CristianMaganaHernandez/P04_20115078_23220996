package com.mycompany.onlineshopping;

import java.util.Scanner;
import java.sql.*;
import java.util.Map;

class Menu {

    // This class represents the menu for the online shopping system.
    private final Cart cart;
    private final Scanner scanner;
    private Product[] products;
    private boolean isCUI;

    public Menu(Cart cart, boolean isCUI) {
        this.cart = cart;
        this.isCUI = isCUI;  // Set whether we're in CUI mode
        this.scanner = new Scanner(System.in);
        initializeProducts();
    }

    private void initializeProducts() {
        products = new Product[]{
            ProductFactory.createProduct("pizza", "Margherita Pizza", 8.99),
            ProductFactory.createProduct("pizza", "Pepperoni Pizza", 9.99),
            ProductFactory.createProduct("pizza", "Space Special Pizza", 12.99),
            ProductFactory.createProduct("pizza", "Gluten-Free Pizza", 10.99),
            ProductFactory.createProduct("pizza", "Vegan Pizza", 11.99),
            ProductFactory.createProduct("drink", "Cola", 1.99),
            ProductFactory.createProduct("drink", "Lemonade", 2.49),
            ProductFactory.createProduct("dessert", "Meteor Fries", 3.99),
            ProductFactory.createProduct("dessert", "Moon Cheesecake", 4.99)
        };


        // Only interact with the database if not in CUI mode
        if (!isCUI) {
            try {
                DerbyDBConnector dbConnector = DerbyDBConnector.getInstance();
                for (Product product : products) {
                    if (!productExists(dbConnector, product.getProductName())) {
                        dbConnector.insertProduct(product.getProductName(), product.getProductPrice());
                        System.out.println("Inserted product: " + product.getProductName());
                    } else {
                        System.out.println("Product already exists: " + product.getProductName());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean productExists(DerbyDBConnector dbConnector, String productName) throws SQLException {
        if (isCUI) {
            return false;  // In CUI mode, don't check the database
        }
        
        String query = "SELECT COUNT(*) FROM Product WHERE Name = ?";
        try (PreparedStatement pstmt = dbConnector.getConnection().prepareStatement(query)) {
            pstmt.setString(1, productName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;  // Returns true if the product exists
                }
            }
        }
        return false; // Product does not exist
    }

    public Product[] getProducts() {
        return products;
    }

    public void showMenu() {
        System.out.println("*** Welcome to Pizza Planet Pizzeria ***");
        System.out.println("----- Menu -----");
        for (int index = 0; index < products.length; index++) {
            Product currentItem = products[index];
            System.out.println((index + 1) + ". " + currentItem.getDescription());
        }
    }

    public void handleUserInput() {
        while (true) {
            showMenu();
            System.out.print("Please select an option ('x' to checkout): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("x")) {
                checkout();
                return; // Exit after checkout
            }

            try {
                int choice = Integer.parseInt(input);

                if (choice > 0 && choice <= products.length) {
                    Product selectedProduct = products[choice - 1];
                    cart.addItem(selectedProduct);
                    System.out.println("Item added to cart.");

                    // Only add the product to the CartItem table if not in CUI mode
                    if (!isCUI) {
                        addProductToCartTable(selectedProduct);
                    }
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addProductToCartTable(Product product) {
        try {
            if (!isCUI) {
                DerbyDBConnector dbConnector = DerbyDBConnector.getInstance();
                int orderId = 1; // Assuming an order exists or creating one before this
                int quantity = 1; // Assuming default quantity is 1
                dbConnector.insertCartItem(orderId, product.getProductID(), quantity);
                System.out.println("Product added to CartItem table: " + product.getDescription());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkout() {
        System.out.println("Proceeding to checkout...");

        if (!isCUI) {
            try {
                DerbyDBConnector dbConnector = DerbyDBConnector.getInstance();

                int customerId = 1;
                double totalPrice = cart.getTotalPrice();

                int orderId = dbConnector.insertOrder(customerId, totalPrice);
                if (orderId != -1) {
                    System.out.println("Order created successfully with Order ID: " + orderId);

                    for (Map.Entry<Product, Integer> entry : cart.getItemsWithQuantities().entrySet()) {
                        Product product = entry.getKey();
                        int quantity = entry.getValue();
                        dbConnector.insertCartItem(orderId, product.getProductID(), quantity);
                    }
                    System.out.println("Items added to CartItem table for order: " + orderId);
                } else {
                    System.out.println("Failed to create order.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Handle checkout in CUI mode without interacting with the database
            System.out.println("CUI Mode: Proceeding to checkout without using the database.");
        }
    }
}
