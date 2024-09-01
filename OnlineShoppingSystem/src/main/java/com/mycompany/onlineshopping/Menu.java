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
import java.util.Scanner;

class Menu {
    // This class represents the menu for the online shopping system.
    private final Cart cart;
    private final Scanner scanner;
    private Product[] products;

    public Menu(Cart cart) {
        this.cart = cart;
        this.scanner = new Scanner(System.in);
        initializeProducts();
    }

    private void initializeProducts() {
        products = new Product[] {
            new Item("Margherita Pizza", 8.99),
            new Item("Pepperoni Pizza", 9.99),
            new Item("Space Special Pizza", 12.99),
            new Item("Gluten-Free Pizza", 10.99),
            new Item("Vegan Pizza", 11.99),
            new Item("Meteor Fries", 3.99),
            new Item("Moon Cheesecake", 4.99)
        };
    }
    
    public Product[] getProducts(){
        return products;
    }

    public void showMenu() {
        System.out.println("*** Welcome to Pizza Planet Pizzeria ***");
        System.out.println("----- Menu -----");
        for (int index = 0; index < products.length; index++) {
            Product currentItem = products[index];
            System.out.println((index + 1) + ". " + currentItem.getDescription());
        }
    }

    public void handleUserInput() {
        while (true) {
            showMenu();
            System.out.print("Please select an option ('x' to checkout): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("x")) {
                checkout();
                return; // Exit after checkout
            }

            try {
                int choice = Integer.parseInt(input);

                if (choice > 0 && choice <= products.length) {
                    cart.addItem(products[choice - 1]);
                    System.out.println("Item added to cart.");
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private void checkout() {
        System.out.println("Proceeding to checkout...");
        // Checkout logic will be handled in Main class
    }
}

