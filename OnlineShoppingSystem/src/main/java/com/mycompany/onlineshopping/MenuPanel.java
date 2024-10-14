package com.mycompany.onlineshopping;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class MenuPanel extends JPanel {
    private Image backgroundImage;

    public MenuPanel(PizzaPlanetGUI gui) {
        // Cargar la imagen de fondo
        try {
            URL imgURL = getClass().getClassLoader().getResource("Menu_Background.jpg");
            if (imgURL != null) {
                backgroundImage = ImageIO.read(imgURL);
            } else {
                System.err.println("Background image not found: Menu_Background.jpg");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Configurar el layout y el borde del panel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Etiqueta del menú en la parte superior
        JLabel menuLabel = new JLabel("Menu", JLabel.CENTER);
        menuLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(menuLabel, BorderLayout.NORTH);

        // Panel del carrito en la esquina superior derecha
        JPanel cartPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        URL cartIconURL = getClass().getClassLoader().getResource("CART.jpeg");
        if (cartIconURL != null) {
            ImageIcon cartIcon = new ImageIcon(new ImageIcon(cartIconURL)
                    .getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH));
            JLabel cartIconLabel = new JLabel(cartIcon);
            cartPanel.add(cartIconLabel);
        } else {
            System.err.println("Cart icon not found: CART.jpeg");
        }

        gui.cartCounterLabel = new JLabel(String.valueOf(gui.cartCounter));
        gui.cartCounterLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gui.cartCounterLabel.setForeground(Color.BLACK);
        cartPanel.add(gui.cartCounterLabel);
        add(cartPanel, BorderLayout.NORTH);

        // Panel para mostrar los productos del menú
        JPanel itemsPanel = new JPanel(new GridLayout(0, 2, 10, 10));  // GridLayout con 2 columnas

        // Añadir productos al menú
        for (Product product : gui.menu.getProducts()) {
            // Crear un panel para cada producto
            JPanel productPanel = new JPanel();
            productPanel.setBorder(BorderFactory.createTitledBorder(product.getDescription()));
            productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

            // Añadir la imagen del producto al panel
            JLabel imageLabel = new JLabel();
            String imageName = getImageNameForProduct(product.getProductName());
            if (imageName != null) {
                URL productIconURL = getClass().getClassLoader().getResource(imageName);
                if (productIconURL != null) {
                    ImageIcon productIcon = new ImageIcon(new ImageIcon(productIconURL)
                            .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    imageLabel.setIcon(productIcon);
                } else {
                    System.err.println("Product image not found: " + imageName);
                }
            }

            // Botón para añadir el producto al carrito
            JButton addButton = new JButton("Add to Cart");

            // Acción para añadir el producto al carrito
            addButton.addActionListener(e -> {
                int productId = gui.orderController.getProductIdFromDatabase(product.getProductName());
                if (productId != -1) {
                    // Añadir ítem al carrito en memoria
                    gui.cart.addItem(product);
                    // Añadir ítem al carrito en la base de datos
                    gui.orderController.addItemToCart(gui.cartId, productId, 1);
                    // Actualizar el contador del carrito y el precio total
                    gui.updateCartCounter(true, 1);  // Incrementar el contador en 1 por cada ítem añadido
                    gui.updateTotalPrice();
                } else {
                    JOptionPane.showMessageDialog(gui, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Añadir componentes al panel de productos
            productPanel.add(imageLabel);
            productPanel.add(Box.createVerticalStrut(5));
            productPanel.add(addButton);

            // Añadir el panel de producto al itemsPanel
            itemsPanel.add(productPanel);
        }

        // Añadir itemsPanel al scroll pane
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con los botones 'View Cart' y 'Back'
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton cartButton = new JButton("View Cart");
        cartButton.addActionListener(e -> {
            gui.updateCartPanel();
            gui.cardLayout.show(gui.mainPanel, "Cart");
        });
        bottomPanel.add(cartButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> gui.cardLayout.show(gui.mainPanel, "CustomerInformation"));
        bottomPanel.add(backButton);

        // Añadir bottomPanel al MenuPanel
        add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibujar la imagen de fondo, ajustándola al tamaño del panel
        if (backgroundImage != null) {
            int width = getWidth();
            int height = getHeight();
            g.drawImage(backgroundImage, 0, 0, width, height, this);
        }
    }

    // Método auxiliar para obtener el nombre del archivo de imagen basado en el nombre del producto
    private String getImageNameForProduct(String productName) {
        switch (productName.toLowerCase()) {
            case "meteor fries":
                return "Fries.jpg";
            case "lemonade":
                return "Lemonade.jpg";
            case "moon cheesecake":
                return "OreoCheesecake.jpg";
            case "vegan pizza":
                return "Vegan_pizza.jpg";
            case "cola":
                return "cola.jpeg";
            case "gluten-free pizza":
                return "glutten free.jpg";
            case "margherita pizza":
                return "margherita pizza.png";
            case "pepperoni pizza":
                return "pepperoni.jpg";
            case "space special pizza":
                return "space_special_pizza.jpg";
            default:
                return null;
        }
    }
}
