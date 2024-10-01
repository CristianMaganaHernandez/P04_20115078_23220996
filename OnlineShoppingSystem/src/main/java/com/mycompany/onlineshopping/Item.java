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
class Item extends Product {

    // This class extends the Product class, representing a specific intem in the store
    public Item(String name, double price) {
        // Constructor for initializing an item with its name and price.
        super(name, price);
    }

    @Override
    public String getDescription() {
        return getProductName() + " - $" + getProductPrice();
    }
}
