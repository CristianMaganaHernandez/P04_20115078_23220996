/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.onlineshopping;

/**
 *
 * @author crist
 */
public class ProductFactory {

    // Factory method to create products based on type
    public static Product createProduct(String type, String name, double price) {
        switch (type.toLowerCase()) {
            case "pizza":
                return new Item(name, price);  // Use Item as a general Pizza product
            case "drink":
                return new Drink(name, price);  // Create a Drink product
            case "dessert":
                return new Dessert(name, price);  // Create a Dessert product
            default:
                throw new IllegalArgumentException("Unknown product type: " + type);
        }
    }
}
