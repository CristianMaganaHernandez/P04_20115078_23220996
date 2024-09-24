package com.mycompany.onlineshopping;

import java.sql.*;

public class DerbyDBConnector {

    private static DerbyDBConnector instance;
    private Connection connection;
    private String jdbcURL = "jdbc:derby:OnlineShoppingSystem_db;create=true;user=pdc;password=pdc";

    // Private constructor to prevent multiple instances
    private DerbyDBConnector() {
        try {
            connection = DriverManager.getConnection(jdbcURL);
            System.out.println("Database connected.");
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Singleton pattern to ensure only one instance of the database connection
    public static synchronized DerbyDBConnector getInstance() {
        if (instance == null) {
            instance = new DerbyDBConnector();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void shutdownDatabase() {
        try {
            DriverManager.getConnection("jdbc:derby:OnlineShoppingSystem_db;shutdown=true");
        } catch (SQLException e) {
            if ("XJ015".equals(e.getSQLState())) {
                System.out.println("Derby shut down normally.");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert customer information into the Customer table
    public int insertCustomer(String name, String address) throws SQLException {
        String insertSQL = "INSERT INTO Customer (Name, Address) VALUES (?, ?)";
        try ( PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.executeUpdate();

            try ( ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    connection.commit();
                    return generatedKeys.getInt(1); // Return the CustomerID
                }
            } catch (SQLException e) {
                connection.rollback();  // Rollback on failure
                throw e;
            }
        }
        return -1; // If no ID was generated
    }

    // Insert order details into Orders table
    public int insertOrder(int customerId, double totalPrice) throws SQLException {
        String insertSQL = "INSERT INTO Orders (CustomerID, TotalPrice) VALUES (?, ?)";
        try ( PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, customerId);
            pstmt.setDouble(2, totalPrice);
            pstmt.executeUpdate();

            try ( ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    connection.commit();
                    return generatedKeys.getInt(1); // Return the OrderID
                }
            } catch (SQLException e) {
                connection.rollback();  // Rollback on failure
                throw e;
            }
        }
        return -1;
    }

public void insertOrderItem(int orderId, int productId, int quantity) throws SQLException {
    String insertSQL = "INSERT INTO OrderItem (OrderID, ProductID, Quantity) VALUES (?, ?, ?)";
    try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
        pstmt.setInt(1, orderId);  // The OrderID for this order
        pstmt.setInt(2, productId); // The ProductID of the product being ordered
        pstmt.setInt(3, quantity);  // The quantity of the product
        pstmt.executeUpdate();
        connection.commit();  // Commit after successful insert
    } catch (SQLException e) {
        connection.rollback();  // Rollback on failure
        throw e;
    }
}


    public int insertProduct(String name, double price) throws SQLException {
        String insertSQL = "INSERT INTO Product (Name, Price) VALUES (?, ?)";
        try ( PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.executeUpdate();

            // Retrieve the generated ProductID
            try ( ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    connection.commit();
                    return generatedKeys.getInt(1); // Return the ProductID
                }
            } catch (SQLException e) {
                connection.rollback();  // Rollback on failure
                throw e;
            }
        }
        return -1; // Return -1 if no ProductID was generated
    }

    public int insertCart(int customerId) throws SQLException {
        String insertSQL = "INSERT INTO Cart (CustomerID) VALUES (?)";
        try ( PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, customerId);
            pstmt.executeUpdate();

            // Retrieve the generated CartID
            try ( ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    connection.commit();
                    return generatedKeys.getInt(1); // Return the CartID
                }
            } catch (SQLException e) {
                connection.rollback();  // Rollback on failure
                throw e;
            }
        }
        return -1; // Return -1 if no CartID was generated
    }

    public void insertCartItem(int cartId, int productId, int quantity) throws SQLException {
        String insertSQL = "INSERT INTO CartItem (CARTID, PRODUCTID, QUANTITY) VALUES (?, ?, ?)";
        try ( PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setInt(1, cartId);  // Use CARTID instead of OrderID
            pstmt.setInt(2, productId); // Use PRODUCTID
            pstmt.setInt(3, quantity);  // Use QUANTITY
            pstmt.executeUpdate();
            connection.commit();  // Commit after successful insert
        } catch (SQLException e) {
            connection.rollback();  // Rollback on failure
            throw e;
        }
    }

}
