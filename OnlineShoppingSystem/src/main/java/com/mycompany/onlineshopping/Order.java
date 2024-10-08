/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.onlineshopping;

/**
 *
 * @author Cristian Alejandro Magana Hernandez
 * @author Santiago Viscarra
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

class Order {
    // This class manages orders placed by the customer
    private final Customer customer;
    private final Cart cart;

    public Order(Customer customer, Cart cart) {
        validateNotNull(customer, "customer");
        validateNotNull(cart, "cart");
        this.customer = customer;
        this.cart = cart;
    }

    public void validateNotNull(Object obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " cannot be null!");
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public Cart getCart() {
        return cart;
    }

    public void saveOrder(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writeOrder(writer);
        } catch (IOException e) {
            System.out.println("Error saving order" + e.getMessage());
        }
    }

    public void writeOrder(BufferedWriter writer) throws IOException {
        writer.write("Customer: " + customer.getName() + "\n");
        writer.write("Address: " + customer.getAddress() + "\n");
        writer.write("Items:\n");
        cart.getItemFrequency()
                .entrySet()
                .stream()
                .map(this::itemDetails)
                .forEach(details -> {
                    try {
                        writer.write(details);
                    } catch (IOException e) {
                        throw new RuntimeException("Error writing Item details!", e);
                    }
                });
        String totalPrice = String.format("%.2f", cart.getTotalPrice());
        writer.write("Total Order Cost: $" + totalPrice + "\n");
        writer.write("====================================\n");
    }

    public String itemDetails(Map.Entry<String, Integer> entry) {
        String itemName = entry.getKey();
        int quantity = entry.getValue();
        double itemPrice = cart.getItemPrice(itemName);
        double totalPrice = itemPrice * quantity;
        return itemName + " - " + quantity + " pcs - $" + String.format("%.2f", totalPrice) + "\n";
    }
}