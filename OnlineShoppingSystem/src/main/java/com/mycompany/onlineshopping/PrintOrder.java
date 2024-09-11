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
import java.util.Map;
class PrintOrder {
    // This class is responsible for printing order details.
    public static void printOrderDetails(Order order) {
        System.out.println(String.format("Customer Name: %s",order.getCustomer().getName()));
        System.out.println(String.format("Customer Address: %s",order.getCustomer().getAddress()));
        System.out.println("Order Items:");

        Map<String, Integer> itemQuantities = order.getCart().getItemFrequency();
        for (Map.Entry<String, Integer> item : itemQuantities.entrySet()) {
            String productName = item.getKey();
            int quantity = item.getValue();
            double pricePerUnit = order.getCart().getItemPrice(productName);
            double totalItemCost = pricePerUnit * quantity;
            System.out.println(productName + " - " + quantity + " pcs - $" + totalItemCost);
        }

        System.out.println(String.format("Total Order Cost: $%.2f",order.getCart().getTotalPrice()));
        System.out.println(order.getOrderTimestamp().toString());
    }
}

