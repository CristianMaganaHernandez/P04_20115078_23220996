/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.onlineshopping;
import java.util.*;

/**
 *
 * @author Cristian Alejandro Magana Hernandez
 * @author Santiago Viscarra
 */

class Cart implements CartInterface {

    private final Map<Product, Integer> items;

    public Cart() {
        this.items = new HashMap<>();
    }

    @Override
    public void addItem(Product item) {
        items.put(item, items.getOrDefault(item, 0) + 1);
    }

    @Override
    public void removeItem(String productName) {
        Product productToRemove = findProductByName(productName);
        if (productToRemove != null) {
            int quantity = items.get(productToRemove);
            if (quantity > 1) {
                items.put(productToRemove, quantity - 1);
            } else {
                items.remove(productToRemove);
            }
        } else {
            throw new NoSuchElementException("Product not found in cart: " + productName);
        }
    }

    @Override
    public void deleteItem(String productName) {
        Product productToDelete = findProductByName(productName);
        if (productToDelete != null) {
            items.remove(productToDelete);
        } else {
            throw new NoSuchElementException("Product not found in cart: " + productName);
        }
    }

    @Override
    public Map<Product, Integer> getItemsWithQuantities() {
        return items;
    }

    @Override
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

    @Override
    public Map<String, Integer> getItemFrequency() {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            frequencyMap.put(entry.getKey().getProductName(), entry.getValue());
        }
        return frequencyMap;
    }

    @Override
    public int getItemQuantity(Product product) {
        return items.getOrDefault(product, 0);
    }

    @Override
    public double getItemPrice(String itemName) {
        for (Product item : items.keySet()) {
            if (item.getProductName().equals(itemName)) {
                return item.getProductPrice();
            }
        }
        throw new NoSuchElementException("Item not found in cart: " + itemName);
    }

    // Helper method to find a product by its name
    private Product findProductByName(String productName) {
        for (Product product : items.keySet()) {
            if (product.getProductName().equals(productName)) {
                return product;
            }
        }
        return null;
    }
}
