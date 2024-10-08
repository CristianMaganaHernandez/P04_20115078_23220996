/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.onlineshopping;

/**
 *
 * @author crist
 */
import java.util.*;

public interface CartInterface {
    void addItem(Product item);
    void removeItem(String productName);
    void deleteItem(String productName);
    Map<Product, Integer> getItemsWithQuantities();
    double getTotalPrice();
    Map<String, Integer> getItemFrequency();
    int getItemQuantity(Product product);
    double getItemPrice(String itemName);
}
