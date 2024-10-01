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

    // Concrete subclass to test the abstract Product class
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
        System.out.println("Starting tests for Product class.");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("Finished tests for Product class.");
    }

    /**
     * Test of getProductID method, of class Product.
     */
    @Test
    public void testGetProductID() {
        product = new ProductImpl("Test Product", 10.99);
        product.setProductID(1234);
        int expResult = 1234;
        int result = product.getProductID();
        assertEquals(expResult, result);
    }

    /**
     * Test of setProductID method, of class Product.
     */
    @Test
    public void testSetProductID() {
        product = new ProductImpl("Test Product", 10.99);
        product.setProductID(5678);
        assertEquals(5678, product.getProductID());
    }

    /**
     * Test of getProductName method, of class Product.
     */
    @Test
    public void testGetProductName() {
        product = new ProductImpl("Test Product", 10.99);
        String expResult = "Test Product";
        String result = product.getProductName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getProductPrice method, of class Product.
     */
    @Test
    public void testGetProductPrice() {
        product = new ProductImpl("Test Product", 10.99);
        double expResult = 10.99;
        double result = product.getProductPrice();
        assertEquals(expResult, result, 0.01);  // delta to handle floating-point precision
    }

    /**
     * Test of getDescription method, of class Product.
     */
    @Test
    public void testGetDescription() {
        product = new ProductImpl("Test Product", 10.99);
        String expResult = "This is a test product.";
        String result = product.getDescription();
        assertEquals(expResult, result);
    }
}
