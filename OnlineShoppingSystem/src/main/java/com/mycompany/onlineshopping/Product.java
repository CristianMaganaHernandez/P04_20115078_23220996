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

abstract class Product {
    // The abstract class defines the blueprint fo products in the online shopping system
    private final String productName;
    private final double ProductPrice;

    public Product(String productName, double ProductPrice) {
        this.productName = productName;
        this.ProductPrice = ProductPrice;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return ProductPrice;
    }

    public abstract String getDescription();
}
