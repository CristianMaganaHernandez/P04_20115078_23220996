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
            //connection.setAutoCommit(false); // Enable manual commit

            // Automatically create tables if they don't exist
            createTables(); // Automatically create tables on database startup
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

    // Return the database connection
    public Connection getConnection() {
        return connection;
    }

    // Method to shut down the database
    public void shutdownDatabase() {
        try {
            // Check if the connection is open before trying to shutdown
            if (connection != null && !connection.isClosed()) {
                if (!connection.getAutoCommit()) {
                    connection.commit();  // commit any pending transactions
                }
                // Now shutdown the Derby system
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
                System.out.println("Database shut down successfully.");
            }
        } catch (SQLException e) {
            // Derby system shutdown will always throw an exception with SQLState "XJ015"
            if ("XJ015".equals(e.getSQLState())) {
                System.out.println("Derby shutdown normally.");
            } else {
                e.printStackTrace();
            }
        }
    }

    // Method to close the connection
    public void closeConnection() {
        try {
            if (!connection.getAutoCommit()) {
                connection.commit(); // commit any open transaction
            }
            connection.close(); // close the connection
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() throws SQLException {
        if (!doesTableExist("PRODUCT")) {
            String createProductTable = "CREATE TABLE Product ("
                    + "ProductID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "Name VARCHAR(100) NOT NULL,"
                    + "Price DECIMAL(10, 2) NOT NULL)";
            executeStatement(createProductTable);
        }

        if (!doesTableExist("CUSTOMER")) {
            String createCustomerTable = "CREATE TABLE Customer ("
                    + "CustomerID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "Name VARCHAR(100) NOT NULL,"
                    + "Address VARCHAR(255) NOT NULL)";
            executeStatement(createCustomerTable);
        }

        if (!doesTableExist("CART")) {
            String createCartTable = "CREATE TABLE Cart ("
                    + "CartID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "CustomerID INT, FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID))";
            executeStatement(createCartTable);
        }

        if (!doesTableExist("CARTITEM")) {
            String createCartItemTable = "CREATE TABLE CartItem ("
                    + "CartItemID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "CartID INT, ProductID INT, Quantity INT NOT NULL,"
                    + "FOREIGN KEY (CartID) REFERENCES Cart(CartID),"
                    + "FOREIGN KEY (ProductID) REFERENCES Product(ProductID))";
            executeStatement(createCartItemTable);
        }

        if (!doesTableExist("ORDERS")) {
            String createOrdersTable = "CREATE TABLE Orders ("
                    + "OrderID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "CustomerID INT, OrderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "TotalPrice DECIMAL(10, 2),"
                    + "FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID))";
            executeStatement(createOrdersTable);
        }

        if (!doesTableExist("ORDERITEM")) {
            String createOrderItemTable = "CREATE TABLE OrderItem ("
                    + "OrderItemID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "OrderID INT, ProductID INT, Quantity INT NOT NULL,"
                    + "FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),"
                    + "FOREIGN KEY (ProductID) REFERENCES Product(ProductID))";
            executeStatement(createOrderItemTable);
        }

        System.out.println("Tables checked and created (if necessary).");
    }

    private boolean doesTableExist(String tableName) throws SQLException {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        try ( ResultSet rs = dbMetaData.getTables(null, null, tableName.toUpperCase(), null)) {
            return rs.next();
        }
    }

    private void executeStatement(String sql) throws SQLException {
        try ( Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public void dropTables() throws SQLException {
        String[] tables = {"OrderItem", "Orders", "CartItem", "Cart", "Product", "Customer"};
        for (String table : tables) {
            try {
                String dropSQL = "DROP TABLE " + table;
                try ( Statement stmt = connection.createStatement()) {
                    stmt.execute(dropSQL);
                }
                System.out.println("Dropped table: " + table);
            } catch (SQLException e) {
                if (e.getSQLState().equals("42Y55")) {
                    // Table does not exist, ignore the exception
                } else {
                    throw e; // Rethrow other SQL exceptions
                }
            }
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
                    //connection.commit();
                    return generatedKeys.getInt(1); // Return the CustomerID
                }
            } catch (SQLException e) {
                //connection.rollback();  // Rollback on failure
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
                    //connection.commit();
                    return generatedKeys.getInt(1); // Return the OrderID
                }
            } catch (SQLException e) {
                //connection.rollback();  // Rollback on failure
                throw e;
            }
        }
        return -1;
    }

    public void insertOrderItem(int orderId, int productId, int quantity) throws SQLException {
        String insertSQL = "INSERT INTO OrderItem (OrderID, ProductID, Quantity) VALUES (?, ?, ?)";
        try ( PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setInt(1, orderId);  // The OrderID for this order
            pstmt.setInt(2, productId); // The ProductID of the product being ordered
            pstmt.setInt(3, quantity);  // The quantity of the product
            pstmt.executeUpdate();
            //connection.commit();  // Commit after successful insert
        } catch (SQLException e) {
            //connection.rollback();  // Rollback on failure
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
                    //connection.commit();
                    return generatedKeys.getInt(1); // Return the ProductID
                }
            } catch (SQLException e) {
                //connection.rollback();  // Rollback on failure
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
                    //connection.commit();
                    return generatedKeys.getInt(1); // Return the CartID
                }
            } catch (SQLException e) {
                //connection.rollback();  // Rollback on failure
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
            //connection.commit();  // Commit after successful insert
        } catch (SQLException e) {
            //connection.rollback();  // Rollback on failure
            throw e;
        }
    }
}
