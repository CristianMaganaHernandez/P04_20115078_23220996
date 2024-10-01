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
 * @author santi
 */
public class CustomerTest {

    public CustomerTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @Test
    public void testGetName() {
        System.out.println("getName");
        Customer instance = new Customer("Olivia Anderson", "654 Birch Ln");
        String expResult = "Olivia Anderson";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAddress() {
        System.out.println("getAddress");
        Customer instance = new Customer("James Rodriguez", "321 Walnut Cir");
        String expResult = "321 Walnut Cir";
        String result = instance.getAddress();
        assertEquals(expResult, result);
    }

    @Test
    public void testCustomerInitialization() {
        System.out.println("testCustomerInitialization");
        Customer instance = new Customer("Isabella Scott", "111 Aspen Ct");

        assertEquals("Isabella Scott", instance.getName(), "Customer name was not initialized correctly.");
        assertEquals("111 Aspen Ct", instance.getAddress(), "Customer address was not initialized correctly.");
    }

    @Test
    public void testNullHandling() {
        System.out.println("testNullHandling");
        Customer instance = new Customer(null, null);

        assertNull(instance.getName(), "Expected name to be null.");
        assertNull(instance.getAddress(), "Expected address to be null.");
    }

    @Test
    public void testCustomerEquality() {
        System.out.println("testCustomerEquality");
        Customer customer1 = new Customer("Benjamin Lewis", "202 Cherry Pl");
        Customer customer2 = new Customer("Benjamin Lewis", "202 Cherry Pl");

        assertEquals(customer1.getName(), customer2.getName(), "Customers should have the same name.");
        assertEquals(customer1.getAddress(), customer2.getAddress(), "Customers should have the same address.");
    }
}
