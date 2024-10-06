package com.mycompany.onlineshopping;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

public class OrderController {

    private final DerbyDBConnector dbConnector;

    public OrderController() {
        // Initialize the database connector
        dbConnector = DerbyDBConnector.getInstance();
    }

    // Method to create a new customer and insert them into the database
    public int createCustomer(String name, String address) {
        int customerId = -1;
        try {
            customerId = dbConnector.insertCustomer(name, address);
            if (customerId == -1) {
                System.err.println("Failed to insert customer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerId;
    }

    // Method to fetch a customer ID from the database using their name and address
    public int getCustomerIdFromDatabase(String name, String address) {
        int customerId = -1;
        String query = "SELECT CustomerID FROM Customer WHERE LOWER(Name) = LOWER(?) AND LOWER(Address) = LOWER(?)";
        try ( PreparedStatement pstmt = dbConnector.getConnection().prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                customerId = rs.getInt("CustomerID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerId;
    }

    // Method to add a product to the database
    public int addProduct(String productName, double productPrice) {
        int productId = -1;
        try {
            productId = dbConnector.insertProduct(productName, productPrice);
            if (productId == -1) {
                System.err.println("Failed to insert product.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productId;
    }

    // Method to fetch a product ID from the database using the product name
    public int getProductIdFromDatabase(String productName) {
        int productId = -1;
        String query = "SELECT ProductID FROM Product WHERE LOWER(Name) = LOWER(?)"; // Normalize name case for comparison
        try ( PreparedStatement pstmt = dbConnector.getConnection().prepareStatement(query)) {
            pstmt.setString(1, productName); // Set product name parameter
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                productId = rs.getInt("ProductID"); // Get the product ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productId;
    }

    // Method to create a new cart for a customer and return the CartID
    public int createCart(int customerId) {
        int cartId = -1;
        try {
            cartId = dbConnector.insertCart(customerId);
            if (cartId == -1) {
                System.err.println("Failed to create cart.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartId;
    }

    // Method to add items to the cart in the database
    public void addItemToCart(int cartId, int productId, int quantity) {
        try {
            dbConnector.insertCartItem(cartId, productId, quantity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to create an order for a customer and insert it into the database
    public int createOrder(Customer customer, Cart cart) {
        int orderId = -1;
        try {
            // Retrieve the CustomerID from the database
            int customerId = getCustomerIdFromDatabase(customer.getName(), customer.getAddress());

            // Insert the order into the database
            orderId = dbConnector.insertOrder(customerId, cart.getTotalPrice());
            if (orderId == -1) {
                System.err.println("Failed to create order.");
                return -1;
            }

            // Insert each cart item into the OrderItem table
            for (Map.Entry<String, Integer> entry : cart.getItemFrequency().entrySet()) {
                String productName = entry.getKey();
                int quantity = entry.getValue();
                int productId = getProductIdFromDatabase(productName);  // Fetch product ID
                if (productId != -1) {
                    dbConnector.insertOrderItem(orderId, productId, quantity);  // Insert each item with its quantity
                } else {
                    System.err.println("Product not found: " + productName);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderId;
    }

    // Method to save an order to a file
    public void saveOrderToFile(Order order, String filePath) {
        order.saveOrder(filePath);
    }

    public String getOrderDetails(Order order) {
        StringBuilder details = new StringBuilder();
        details.append("Customer Name: ").append(order.getCustomer().getName()).append("\n");
        details.append("Customer Address: ").append(order.getCustomer().getAddress()).append("\n");
        details.append("Order Items:\n");

        for (Map.Entry<String, Integer> entry : order.getCart().getItemFrequency().entrySet()) {
            details.append(entry.getKey()).append(" - ").append(entry.getValue()).append(" pcs\n");
        }

        details.append("\nTotal Order Cost: $").append(String.format("%.2f", order.getCart().getTotalPrice())).append("\n");
        details.append("Order Timestamp: ").append(order.getFormattedOrderTimestamp()).append("\n");

        return details.toString();
    }
    
    // Method to calculate the total order cost based on items in the cart
    public double calculateTotalOrderCost(Cart cart) {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : cart.getItemFrequency().entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            int productId = getProductIdFromDatabase(productName);
            if (productId != -1) {
                try {
                    double productPrice = dbConnector.getProductPrice(productId); // Fetch product price
                    total += productPrice * quantity;
                } catch (SQLException e) {
                    System.err.println("Error fetching price for product ID: " + productId);
                    e.printStackTrace();
                }
            } else {
                System.err.println("Product ID not found for product: " + productName);
            }
        }
        return total;
    }

    // Method to process a complete order
    public void processOrder(Customer customer, Cart cart, String orderFilePath) {
        // Create and save the order
        Order order = new Order(customer, cart, LocalDateTime.now());
        createOrder(customer, cart);
        saveOrderToFile(order, orderFilePath);
    }

    // Method to shutdown the database when finished
    public void shutdown() {
        dbConnector.shutdownDatabase();
    }
}
