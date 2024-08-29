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

class PizzaPlanetMain {
    // This is the main class for running the Pizza PLanet Online shopping system
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            // Display Introduction
            Introduction intro = new Introduction("introduction.txt");
            intro.displayIntroduction();

            // Get Shopper Information
            String name = getValidInput(scanner,"Please enter your name: ");
            String address =getValidInput(scanner,"Please enter your address: ");
            Customer customer = new Customer(name, address);

            // Create Cart and Menu
            Cart cart = new Cart();
            Menu menu = new Menu(cart);

            // Handle Menu User Input
            menu.handleUserInput();

            // Create and Save Order after Checkout
            Order order = new Order(customer, cart);
            order.saveOrder("orders.txt");

            // Print Order Details
            PrintOrder.printOrderDetails(order);
            scanner.close();
        } catch(Exception e){
            System.out.println(" An Unexpected error has occured!");
        }
    }
    public static String getValidInput(Scanner scanner, String prompt){
        System.out.println(prompt);     
        String input = scanner.nextLine().trim();
        while(input.isEmpty()){
            System.out.println("Input cannot be empty please try again!");
            input = scanner.nextLine().trim();
        }
        return input;
    }
}

