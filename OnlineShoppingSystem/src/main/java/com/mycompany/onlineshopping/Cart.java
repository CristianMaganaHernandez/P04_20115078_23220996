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
import java.util.*;

class Cart {

    // This class represents a shopping cart that holds a list of products and their quantities
    private final Map<Product, Integer> items;  // Use a Map to track product and its quantity

    public Cart() {
        // Constructor initializing an empty cart
        this.items = new HashMap<>();
    }

    // Add an item to the cart, increase quantity if it already exists
    public void addItem(Product item) {
        items.put(item, items.getOrDefault(item, 0) + 1);
    }

    // Remove one instance of a product from the cart or completely if quantity becomes 0
    public void removeItem(String productName) {
        Product productToRemove = findProductByName(productName);
        if (productToRemove != null) {
            int quantity = items.get(productToRemove);
            if (quantity > 1) {
                items.put(productToRemove, quantity - 1);  // Reduce the quantity
            } else {
                items.remove(productToRemove);  // Remove the product if quantity is 1
            }
        } else {
            throw new NoSuchElementException("Product not found in cart: " + productName);
        }
    }

    // Completely delete the product from the cart, no matter the quantity
    public void deleteItem(String productName) {
        Product productToDelete = findProductByName(productName);
        if (productToDelete != null) {
            items.remove(productToDelete);  // Remove the product entirely
        } else {
            throw new NoSuchElementException("Product not found in cart: " + productName);
        }
    }

    // Get all items in the cart along with their quantities
    public Map<Product, Integer> getItemsWithQuantities() {
        return items;
    }

    // Calculate the total price of the cart
    public double getTotalPrice() {
        if (items.isEmpty()) {
            return 0.0;
        }
        double totalPrice = 0.0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            totalPrice += entry.getKey().getProductPrice() * entry.getValue();
        }
        return totalPrice;
    }

    // Get the frequency of each item (i.e., how many of each product is in the cart)
    public Map<String, Integer> getItemFrequency() {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            frequencyMap.put(entry.getKey().getProductName(), entry.getValue());
        }
        return frequencyMap;
    }

    // Find a product by its name in the cart (helper method)
    private Product findProductByName(String productName) {
        for (Product product : items.keySet()) {
            if (product.getProductName().equals(productName)) {
                return product;
            }
        }
        return null;  // Return null if the product is not found
    }

    // Get the quantity of a specific product in the cart
    public int getItemQuantity(Product product) {
        return items.getOrDefault(product, 0);
    }

    // Get the price of a specific product by name
    public double getItemPrice(String itemName) {
        for (Product item : items.keySet()) {
            if (item.getProductName().equals(itemName)) {
                return item.getProductPrice();
            }
        }
        throw new NoSuchElementException("Item not found in cart: " + itemName);
    }
}
