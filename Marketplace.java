package com.example.gradleproject2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Marketplace extends javax.swing.JFrame {

    private List<String[]> cartItems;

    public Marketplace() {
        initComponents();
        setLocationRelativeTo(null);
        cartItems = new ArrayList<>();
        showItems("", "All"); // Initial display of items
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Marketplace");
        setSize(800, 600);

        jTextField1.setText("Search...");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Electronics", "Clothes", "Home" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton2.setText("View Cart");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Checkout");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(jTextField1);
        topPanel.add(jComboBox1);
        topPanel.add(jButton2);
        topPanel.add(jButton3);

        jPanel3.setLayout(new GridLayout(0, 3, 10, 10)); // 3 columns with spacing
        jPanel7.setLayout(new GridLayout(0, 1, 5, 5));

        JScrollPane scrollPane3 = new JScrollPane(jPanel3);
        JScrollPane scrollPane7 = new JScrollPane(jPanel7);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane3, BorderLayout.CENTER);
        mainPanel.add(scrollPane7, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        pack();
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        String query = jTextField1.getText();
        String category = jComboBox1.getSelectedItem().toString();
        showItems(query, category);
    }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        String query = jTextField1.getText();
        String category = jComboBox1.getSelectedItem().toString();
        showItems(query, category);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        showCart();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        checkout();
    }

    private void showItems(String query, String category) {
        jPanel3.removeAll();
        jPanel3.setLayout(new GridLayout(0, 3, 10, 10)); // 3 columns with spacing

        // Example items with image paths
        String[][] items = {
            {"Item 1", "303 ₽", "resources/images/image1.jpg", "Electronics"},
            {"Item 2", "102 ₽", "resources/images/image2.jpg", "Clothes"},
            {"Item 3", "16 302 ₽", "resources/images/image3.jpg", "Electronics"},
            {"Item 4", "601 ₽", "resources/images/image4.jpg", "Home"},
            {"Item 5", "2 213 ₽", "resources/images/image5.jpg", "Clothes"},
            {"Item 6", "703 ₽", "resources/images/image6.jpg", "Home"}
        };

        for (String[] item : items) {
            if ((query.isEmpty() || item[0].toLowerCase().contains(query.toLowerCase())) &&
                (category.equals("All") || item[3].equals(category))) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createLineBorder(java.awt.Color.GRAY));

                JLabel imageLabel = new JLabel();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource(item[2]));
                Image img = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
                itemPanel.add(imageLabel, BorderLayout.NORTH);

                JLabel nameLabel = new JLabel(item[0], SwingConstants.CENTER);
                itemPanel.add(nameLabel, BorderLayout.CENTER);

                JPanel priceBuyPanel = new JPanel(new FlowLayout());
                JLabel priceLabel = new JLabel(item[1]);
                priceBuyPanel.add(priceLabel);

                JButton buyButton = new JButton("Buy");
                buyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addToCart(item);
                    }
                });
                priceBuyPanel.add(buyButton);

                itemPanel.add(priceBuyPanel, BorderLayout.SOUTH);

                jPanel3.add(itemPanel);
            }
        }

        jPanel3.revalidate();
        jPanel3.repaint();
    }

    private void addToCart(String[] item) {
        cartItems.add(item);
        showCart();
    }

    private void showCart() {
        jPanel7.removeAll();
        jPanel7.setLayout(new GridLayout(0, 1, 5, 5));
        for (String[] item : cartItems) {
            JPanel cartItemPanel = new JPanel(new BorderLayout());
            cartItemPanel.setBorder(BorderFactory.createLineBorder(java.awt.Color.GRAY));

            JLabel nameLabel = new JLabel(item[0]);
            cartItemPanel.add(nameLabel, BorderLayout.CENTER);

            JLabel priceLabel = new JLabel(item[1]);
            cartItemPanel.add(priceLabel, BorderLayout.SOUTH);

            jPanel7.add(cartItemPanel);
        }
        jPanel7.revalidate();
        jPanel7.repaint();
    }

    private void checkout() {
        JOptionPane.showMessageDialog(this, "Checkout functionality is not implemented yet.");
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Marketplace.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Marketplace.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Marketplace.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Marketplace.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Marketplace().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration
}
