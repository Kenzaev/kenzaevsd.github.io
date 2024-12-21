/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.example.gradleproject2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class LuckyJetGame extends JFrame {
    private JPanel gamePanel;
    private JButton betButton;
    private JTextField betField;
    private JLabel multiplierLabel;
    private JLabel balanceLabel;
    private double multiplier = 1.0;
    private double balance = 1000.0; // Начальный баланс игрока
    private Timer timer;
    private boolean isGameRunning = false;
    private double betAmount = 0.0;
    private int rocketX = 10; // Начальная позиция ракеты по оси X (левая сторона экрана)
    private int rocketY = 300; // Начальная позиция ракеты по оси Y (чуть ниже центра)
    private final int rocketWidth = 50; // Ширина ракеты
    private final int rocketHeight = 50; // Высота ракеты
    private final int horizontalSpeed = 10; // Горизонтальная скорость
    private int verticalSpeed = 10; // Вертикальная скорость
    private Random random = new Random();
    private double[] lastMultipliers = new double[10]; // Массив для хранения последних множителей
    private double autoCashOutMultiplier = 2.0; // Множитель для автоматического вывода
    private boolean autoBetEnabled = false;
    private boolean autoCashOutEnabled = false;
    private Image rocketImage;

    public LuckyJetGame() {
        setTitle("LuckyJet Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGameElements(g);
            }
        };
        gamePanel.setBackground(Color.BLACK);

        betField = new JTextField(10);
        betButton = new JButton("СТАВКА");
        multiplierLabel = new JLabel("x" + multiplier);
        multiplierLabel.setForeground(Color.WHITE);
        balanceLabel = new JLabel("Баланс: руб " + balance);
        balanceLabel.setForeground(Color.WHITE);

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Ставка:"));
        controlPanel.add(betField);
        controlPanel.add(betButton);
        controlPanel.add(multiplierLabel);
        controlPanel.add(balanceLabel);

        JButton autoBetButton = new JButton("Авто ставка");
        JButton autoCashOutButton = new JButton("Авто вывод");
        JTextField autoCashOutField = new JTextField("2.0", 5);

        JButton increaseBetButton = new JButton("+");
        JButton decreaseBetButton = new JButton("-");

        increaseBetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                increaseBet();
            }
        });

        decreaseBetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decreaseBet();
            }
        });

        autoBetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleAutoBet();
            }
        });

        autoCashOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleAutoCashOut();
            }
        });

        controlPanel.add(decreaseBetButton);
        controlPanel.add(increaseBetButton);
        controlPanel.add(autoBetButton);
        controlPanel.add(new JLabel("x"));
        controlPanel.add(autoCashOutField);
        controlPanel.add(autoCashOutButton);

        setLayout(new BorderLayout());
        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        betButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isGameRunning) {
                    cashOut();
                } else {
                    startGame();
                }
            }
        });

        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
            }
        });

        // Загрузка изображения ракеты
        try {
            rocketImage = new ImageIcon(getClass().getResource("Resources/bin.png")).getImage();
        } catch (Exception e) {
            System.err.println("Изображение ракеты не найдено: " + e.getMessage());
            rocketImage = null;
        }
    }

    private void startGame() {
        try {
            betAmount = Double.parseDouble(betField.getText());
            if (betAmount <= 0) {
                JOptionPane.showMessageDialog(this, "Ставка должна быть положительным числом.");
                return;
            }
            if (betAmount > balance) {
                JOptionPane.showMessageDialog(this, "Недостаточно средств на балансе.");
                return;
            }
            balance -= betAmount;
            balanceLabel.setText("Баланс: руб " + String.format("%.2f", balance));
            betButton.setText("ЗАБРАТЬ");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Некорректная ставка. Введите число.");
            return;
        }

        multiplier = 1.0;
        rocketX = 50; // Сброс позиции ракеты по оси X (левая сторона экрана)
        rocketY = 300; // Сброс позиции ракеты по оси Y (чуть ниже центра)
        verticalSpeed = 5; // Сброс вертикальной скорости
        isGameRunning = true;
        timer.start();
    }

    private void cashOut() {
        isGameRunning = false;
        timer.stop();
        balance += betAmount * multiplier;
        balanceLabel.setText("Баланс: руб" + String.format("%.2f", balance));
        JOptionPane.showMessageDialog(this, "Вы выиграли! Ваш выигрыш: руб: " + String.format("%.2f", betAmount * multiplier));
        updateLastMultipliers();
        betButton.setText("СТАВКА");
    }

    private void increaseBet() {
        try {
            betAmount = Double.parseDouble(betField.getText());
            betAmount += 1.0;
            betField.setText(String.format("%.2f", betAmount));
        } catch (NumberFormatException ex) {
            betField.setText("1.0");
        }
    }

    private void decreaseBet() {
        try {
            betAmount = Double.parseDouble(betField.getText());
            if (betAmount > 1.0) {
                betAmount -= 1.0;
                betField.setText(String.format("%.2f", betAmount));
            }
        } catch (NumberFormatException ex) {
            betField.setText("1.0");
        }
    }

    private void toggleAutoBet() {
        autoBetEnabled = !autoBetEnabled;
        if (autoBetEnabled) {
            betField.setText("1.0"); // Автоматическая ставка на 1.0
            startGame();
        }
    }

    private void toggleAutoCashOut() {
        autoCashOutEnabled = !autoCashOutEnabled;
        if (autoCashOutEnabled) {
            try {
                autoCashOutMultiplier = Double.parseDouble(autoCashOutField.getText());
            } catch (NumberFormatException ex) {
                autoCashOutField.setText("1.0");
            }
        }
    }

    private void updateGame() {
      if (isGameRunning) {
            double randomChange = (random.nextDouble() - 0.5) * 0.1;
            multiplier += randomChange;
            if (multiplier < 1.0) {
                multiplier = 1.0;
            }
            multiplier += random.nextDouble() * 0.500; // Увеличение множителя случайным образом
            rocketX += horizontalSpeed ; // Перемещение ракеты вправо
            rocketY -= verticalSpeed; // Перемещение ракеты вверх
            verticalSpeed += 1; // Увеличение вертикальной скорости
            multiplierLabel.setText("x" + String.format("%.2f", multiplier));
            gamePanel.repaint();

            if (autoCashOutEnabled && multiplier >= autoCashOutMultiplier) {
                cashOut();
            } else if (rocketX >= getWidth() - 50) { // Ракета достигает правой границы окна
                loseGame();
            }
        }
    }  

    private void loseGame() {
        isGameRunning = false;
        timer.stop();
        JOptionPane.showMessageDialog(this, "Вы проиграли! Ваша ракета взорвалась.");
        betButton.setText("СТАВКА");
    }

    private void endGame() {
        isGameRunning = false;
        timer.stop();
        updateLastMultipliers();
        JOptionPane.showMessageDialog(this, "Игра завершена! Ваш множитель: x" + String.format("%.2f", multiplier));
        betButton.setText("СТАВКА");
    }

    private void updateLastMultipliers() {
        for (int i = lastMultipliers.length - 1; i > 0; i--) {
            lastMultipliers[i] = lastMultipliers[i - 1];
        }
        lastMultipliers[0] = multiplier;
    }

    private void drawGameElements(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("LuckyJet Game", 350, 50);
        if (rocketImage != null) {
            g.drawImage(rocketImage, rocketX, rocketY, rocketWidth, rocketHeight, this); // Рисование ракеты
        } else {
            g.setColor(Color.YELLOW);
            g.fillOval(rocketX, rocketY, rocketWidth, rocketHeight); // Рисование шарика, если изображение не загружено
        }
        g.setColor(Color.MAGENTA);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("x" + String.format("%.2f", multiplier), rocketX + 60, rocketY - 10); // Отображение множителя

        // Отображение последних множителей
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        int x = 50;
        int y = 70;
        for (double m : lastMultipliers) {
            if (m != 0) {
                g.drawString("x" + String.format("%.2f", m), x, y);
                x += 70;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LuckyJetGame().setVisible(true);
            }
        });
    }

    private static class autoCashOutField {

        private static void setText(String string) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static String getText() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        public autoCashOutField() {
        }
   
 public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LuckyJetGame().setVisible(true);
            }
        });
    }
  }
}