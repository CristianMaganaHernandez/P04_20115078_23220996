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
import java.io.*;

class Introduction {
    private final String filePath;

    public Introduction(String filePath) {
        this.filePath = filePath;
    }

    public void displayIntroduction() {
        // This class is responsible or reading and displaying the introduction text from a file
        try {
            // Contructor initializing the file path for the introduction text.
            File file = new File(filePath);
            BufferedReader bufferedFile = new BufferedReader(new FileReader(file));
            String content;
            while ((content = bufferedFile.readLine()) != null) {
                System.out.println(content);
            }
            bufferedFile.close(); // Close the reader to release the resource
        } catch (IOException e) {
            System.out.println("Error reading introduction file: " + e.getMessage());
        }
    }
}

