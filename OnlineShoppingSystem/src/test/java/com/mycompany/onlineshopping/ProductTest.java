/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.onlineshopping;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author crist
 */
public class ProductTest {

    class ProductImpl extends Product {

        public ProductImpl(String productName, double productPrice) {
            super(productName, productPrice);
        }

        @Override
        public String getDescription() {
            return "This is a test product.";
        }
    }

    private Product product;

    public ProductTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        System.out.println("Starting tests for the Product class.");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("Finished tests for the Product class.");
    }

    @Test
    public void testGetProductID() {
        System.out.println("testGetProductID");
        product = new ProductImpl("Test Product", 10.99);
        product.setProductID(1234);  // Setting a product ID
        int expResult = 1234;        // Expected result
        int result = product.getProductID();  // Actual result from method
        assertEquals(expResult, result, "The Product ID should be 1234.");
    }

    @Test
    public void testSetProductID() {
        System.out.println("testSetProductID");
        product = new ProductImpl("Test Product", 10.99);
        product.setProductID(5678);  // Setting a new product ID
        assertEquals(5678, product.getProductID(), "The Product ID should be 5678.");
    }

    @Test
    public void testGetProductName() {
        System.out.println("testGetProductName");
        product = new ProductImpl("Test Product", 10.99);
        String expResult = "Test Product";  // Expected product name
        String result = product.getProductName();  // Actual product name from method
        assertEquals(expResult, result, "The product name should be 'Test Product'.");
    }

    @Test
    public void testGetProductPrice() {
        System.out.println("testGetProductPrice");
        product = new ProductImpl("Test Product", 10.99);
        double expResult = 10.99;  // Expected product price
        double result = product.getProductPrice();  // Actual product price from method
        assertEquals(expResult, result, 0.01, "The product price should be 10.99.");
    }

    @Test
    public void testGetDescription() {
        System.out.println("testGetDescription");
        product = new ProductImpl("Test Product", 10.99);
        String expResult = "This is a test product.";  // Expected description
        String result = product.getDescription();  // Actual description from method
        assertEquals(expResult, result, "The product description should be 'This is a test product.'");
    }
}
