package com.mycompany.onlineshopping;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CustomerInfoPanel extends JPanel {
    private Image backgroundImage;

    public CustomerInfoPanel(PizzaPlanetGUI gui) {
        // Load the background image
        try {
            backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("Rick_MortyCustomerInfoPanelBackground_2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Creating a label with semi-transparent background
        JLabel nameLabel = new JLabel("Name: *");
        nameLabel.setOpaque(true);
        nameLabel.setBackground(new Color(0, 0, 0, 100)); // Black semi-transparent background
        nameLabel.setForeground(Color.WHITE); // White text
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(nameLabel, gbc);

        gbc.gridy++;
        gui.nameField = new JTextField();
        gui.nameField.setPreferredSize(new Dimension(200, 30));
        add(gui.nameField, gbc);

        gbc.gridy++;
        JLabel addressLabel = new JLabel("Address: *");
        addressLabel.setOpaque(true);
        addressLabel.setBackground(new Color(0, 0, 0, 100)); // Black semi-transparent background
        addressLabel.setForeground(Color.WHITE); // White text
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image, scaling it to the size of the panel
        if (backgroundImage != null) {
            int width = getWidth();
            int height = getHeight();
            g.drawImage(backgroundImage, 0, 0, width, height, this);
        }
    }
}
