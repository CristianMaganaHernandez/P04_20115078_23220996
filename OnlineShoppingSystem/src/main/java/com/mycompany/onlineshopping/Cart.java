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
    // This classs represents a shooping cart that can hold a list of products
    private final List<Product> items;

    public Cart() {
        // constructor initialzing an empty cart
        this.items = new ArrayList<>();
    }

    public void addItem(Product item) {
        items.add(item);
    }
    
    public void removeItem(String productName){
        for(int i=items.size()-1;i>=0;i--){
            if (items.get(i).getProductName().equals(productName)){
                items.remove(i);
            }
        }
    }

    public List<Product> getItems() {
        return items;
    }
    
    public double getTotalPrice() {
        if (items.isEmpty()) {
            return 0.0;
        }
        double totalPrice = 0.0;
        Map<String, Integer> frequencyMap = getItemFrequency();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            totalPrice += entry.getValue() * getItemPrice(entry.getKey());
        }
        return totalPrice;
    }

    public Map<String, Integer> getItemFrequency() {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (Product item : items) {
            frequencyMap.put(item.getProductName(), frequencyMap.getOrDefault(item.getProductName(), 0) + 1);
        }
        return frequencyMap;
    }

    public double getItemPrice(String itemName) {
        for (Product item : items) {
            if (item.getProductName().equals(itemName)) {
                return item.getProductPrice();
            }
        }
        throw new NoSuchElementException("Item not found in cart: " + itemName);
    }
}

