/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.onlineshopping;

/**
 *
 * @author crist
 */

public class Dessert extends Product {

    // Constructor for initializing a Dessert with its name and price
    public Dessert(String name, double price) {
        super(name, price);
    }

    @Override
    public String getDescription() {
        return getProductName() + " (Dessert) - $" + getProductPrice();
    }
}
