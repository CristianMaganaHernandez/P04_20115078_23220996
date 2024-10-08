/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.onlineshopping;

/**
 *
 * @author crist
 */



import javax.swing.*;
import java.awt.*;

public class CustomerInfoPanel extends JPanel {
    public CustomerInfoPanel(PizzaPlanetGUI gui) {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel nameLabel = new JLabel("Name: *");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(nameLabel, gbc);

        gbc.gridy++;
        gui.nameField = new JTextField();
        gui.nameField.setPreferredSize(new Dimension(200, 30));
        add(gui.nameField, gbc);

        gbc.gridy++;
        JLabel addressLabel = new JLabel("Address: *");
        addressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(addressLabel, gbc);

        gbc.gridy++;
        gui.addressField = new JTextField();
        gui.addressField.setPreferredSize(new Dimension(200, 30));
        add(gui.addressField, gbc);

        gbc.gridy++;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            String name = gui.nameField.getText().trim();
            String address = gui.addressField.getText().trim();

            if (name.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(gui, "Please enter both name and address.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                gui.customer = new Customer(name, address);
                gui.customerId = gui.orderController.createCustomer(name, address);

                if (gui.customerId != -1) {
                    gui.cartId = gui.orderController.createCart(gui.customerId);
                    gui.cardLayout.show(gui.mainPanel, "Menu");
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> gui.cardLayout.show(gui.mainPanel, "Welcome"));

        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);

        gbc.gridy++;
        add(buttonPanel, gbc);
    }
}
