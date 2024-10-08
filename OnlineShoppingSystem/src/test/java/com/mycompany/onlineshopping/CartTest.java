/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.onlineshopping;

import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author santi
 */
public class CartTest {

    public CartTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @Test
    public void testAddItem() {
        System.out.println("addItem");
        Product item = new Pizza("Pepperoni Pizza", 9.99);
        Cart instance = new Cart();
        instance.addItem(item);
        assertEquals(1, instance.getItemQuantity(item));
    }

    @Test
    public void testRemoveItem() {
        System.out.println("removeItem");
        Product item = new Pizza("Space Special Pizza", 12.99);
        Cart instance = new Cart();
        instance.addItem(item);
        instance.removeItem("Space Special Pizza");
        assertEquals(0, instance.getItemQuantity(item));
    }

    @Test
    public void testDeleteItem() {
        System.out.println("deleteItem");
        Product item = new Pizza("Vegan Pizza", 11.99);
        Cart instance = new Cart();
        instance.addItem(item);
        instance.addItem(item);
        instance.deleteItem("Vegan Pizza");
        assertEquals(0, instance.getItemQuantity(item));
    }

    @Test
    public void testGetItemsWithQuantities() {
        System.out.println("getItemsWithQuantities");
        Cart instance = new Cart();
        Product item1 = new Pizza("Pepperoni Pizza", 9.99);
        Product item2 = new Pizza("Meteor Fries", 3.99);
        instance.addItem(item1);
        instance.addItem(item2);
        Map<Product, Integer> result = instance.getItemsWithQuantities();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetTotalPrice() {
        System.out.println("getTotalPrice");
        Cart instance = new Cart();
        Product pizza = new Pizza("Pepperoni Pizza", 9.99);
        Product fries = new Pizza("Meteor Fries", 3.99);
        instance.addItem(pizza);
        instance.addItem(fries);
        double expResult = 9.99 + 3.99;
        double result = instance.getTotalPrice();
        assertEquals(expResult, result, 0.01);
    }

    @Test
    public void testGetItemFrequency() {
        System.out.println("getItemFrequency");
        Cart instance = new Cart();
        Product pizza = new Pizza("Pepperoni Pizza", 9.99);
        instance.addItem(pizza);
        instance.addItem(pizza);
        Map<String, Integer> result = instance.getItemFrequency();
        assertEquals(2, result.get("Pepperoni Pizza"));
    }

    @Test
    public void testGetItemQuantity() {
        System.out.println("getItemQuantity");
        Product fries = new Pizza("Meteor Fries", 3.99);
        Cart instance = new Cart();
        instance.addItem(fries);
        instance.addItem(fries);
        int result = instance.getItemQuantity(fries);
        assertEquals(2, result);
    }

    @Test
    public void testGetItemPrice() {
        System.out.println("getItemPrice");
        Product mc = new Pizza("Moon Cheesecake", 4.99);
        Cart instance = new Cart();
        instance.addItem(mc);
        double result = instance.getItemPrice("Moon Cheesecake");
        assertEquals(4.99, result, 0.01);
    }
}
