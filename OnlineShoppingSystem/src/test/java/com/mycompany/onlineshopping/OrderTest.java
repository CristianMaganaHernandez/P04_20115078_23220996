/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.onlineshopping;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author santi
 */
public class OrderTest {

    public OrderTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @Test
    public void testGetOrderDateTime() {
        System.out.println("getOrderDateTime");
        Customer customer = new Customer("Damian Hunt", "789 Maple St");
        Cart cart = new Cart();
        LocalDateTime dateTime = LocalDateTime.now();
        Order instance = new Order(customer, cart, dateTime);

        // Fetching the result from the order
        LocalDateTime result = instance.getOrderDateTime();

        // Compare date and time with precision by truncating nanoseconds
        assertEquals(dateTime.withNano(0), result.withNano(0), "The order date time should match without considering nanoseconds.");
    }

    @Test
    public void testGetOrderTimestamp() {
        System.out.println("getOrderTimestamp");
        Customer customer = new Customer("Sarah Williams", "234 Oak Ave");
        Cart cart = new Cart();
        LocalDateTime dateTime = LocalDateTime.now();
        Order instance = new Order(customer, cart, dateTime);

        long expectedMillis = Timestamp.valueOf(dateTime).getTime();
        long actualMillis = instance.getOrderTimestamp().getTime();

        assertEquals(expectedMillis, actualMillis, 5, "Timestamps do not match in milliseconds.");
    }

    @Test
    public void testValidateNotNull() {
        System.out.println("validateNotNull");
        Order instance = new Order(new Customer("David Martinez", "456 Pine Blvd"), new Cart(), LocalDateTime.now());

        assertDoesNotThrow(() -> instance.validateNotNull("Non-null Object", "ObjectName"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            instance.validateNotNull(null, "ObjectName");
        });
        assertTrue(exception.getMessage().contains("ObjectName cannot be null!"));
    }

    @Test
    public void testCartItemCount() {
        System.out.println("testCartItemCount");

        Customer customer = new Customer("Emma Thompson", "101 Elm St");
        Cart cart = new Cart();
        cart.addItem(new Item("Pepperoni Pizza", 9.99));

        LocalDateTime orderTime = LocalDateTime.now();
        Order order = new Order(customer, cart, orderTime);

        assertEquals(1, order.getCart().getItemsWithQuantities().size(), "The cart should contain 1 item");
    }

    @Test
    public void testItemDetails() {
        System.out.println("itemDetails");
        Customer customer = new Customer("Christopher Garcia", "987 Cedar Dr");
        Cart cart = new Cart();
        Order instance = new Order(customer, cart, LocalDateTime.now());

        cart.addItem(new Item("Margherita Pizza", 8.99));

        Map.Entry<String, Integer> entry = new AbstractMap.SimpleEntry<>("Margherita Pizza", 2);
        String result = instance.itemDetails(entry);
        String expResult = "Margherita Pizza - 2 pcs - $17.98\n";
        assertEquals(expResult, result);
    }
}
