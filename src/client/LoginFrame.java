package client;

import javax.swing.*;
import javax.swing.border.*;
import client.ChatClient;
import client.ChatFrame;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import server.ChatServer;

public class LoginFrame extends JFrame {
    private JTextField nameField;
    private JTextField serverField;
    private JTextField portField;
    private JButton joinButton, createButton, themeToggleButton;
    private String serverName = "localhost";
    private boolean isDarkMode = false;
    private JPanel panel;
    private JLabel userName;
    private JLabel serverAddress;
    private JLabel portName;
    private JLabel titleLabel;

    public LoginFrame() {
        setTitle("Chat Application - Login");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Main panel with card-like appearance
        panel = new JPanel(new GridBagLayout());
        panel.setBorder(new CompoundBorder(
                new EmptyBorder(25, 25, 25, 25),
                new MatteBorder(1, 1, 1, 1, new Color(230, 230, 230))));
        panel.setBackground(new Color(250, 250, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Custom font styling
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 18);

        // Title label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        titleLabel = new JLabel("Chat Application Login");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(60, 60, 60));
        panel.add(titleLabel, gbc);

        // Name field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        userName = new JLabel("Your Name:", JLabel.LEFT);
        panel.add(userName, gbc);

        gbc.gridx = 1;
        nameField = createStyledTextField(15);
        panel.add(nameField, gbc);

        // Server field
        gbc.gridx = 0;
        gbc.gridy = 2;
        serverAddress = new JLabel("Server Address:", JLabel.LEFT);
        panel.add(serverAddress, gbc);

        gbc.gridx = 1;
        serverField = createStyledTextField(15);
        serverField.setText(serverName);
        // change this to true for user input
        serverField.setEditable(true);
        serverField.setBackground(new Color(240, 240, 240));
        panel.add(serverField, gbc);

        // Port field
        gbc.gridx = 0;
        gbc.gridy = 3;
        portName = new JLabel("Port Number:", JLabel.LEFT);
        panel.add(portName, gbc);

        gbc.gridx = 1;
        portField = createStyledTextField(15);
        portField.setText("8080");
        panel.add(portField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        joinButton = createStyledButton("Join Server", new Color(0, 120, 215), buttonFont);
        createButton = createStyledButton("Create Server", new Color(0, 120, 215), buttonFont);
        themeToggleButton = createStyledButton("Toggle Theme", new Color(100, 100, 100), buttonFont);

        joinButton.addActionListener(this::joinServer);
        createButton.addActionListener(this::createServer);
        themeToggleButton.addActionListener(e -> toggleTheme());

        buttonPanel.add(joinButton);
        buttonPanel.add(createButton);
        buttonPanel.add(themeToggleButton);
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            panel.setBackground(new Color(45, 45, 45));
            joinButton.setBackground(new Color(0, 90, 180));
            createButton.setBackground(new Color(0, 90, 180));
            themeToggleButton.setBackground(new Color(70, 70, 70));
            userName.setForeground(Color.WHITE);
            serverAddress.setForeground(Color.WHITE);
            portName.setForeground(Color.white);
            titleLabel.setForeground(Color.white);

        } else {
            panel.setBackground(new Color(250, 250, 250));
            joinButton.setBackground(new Color(0, 120, 215));
            createButton.setBackground(new Color(0, 120, 215));
            themeToggleButton.setBackground(new Color(100, 100, 100));
            userName.setForeground(Color.black);
            serverAddress.setForeground(Color.black);
            portName.setForeground(Color.black);
            titleLabel.setForeground(Color.black);
        }
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(8, 12, 8, 12)));
        field.setBackground(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
        return button;
    }

    // Connection Methods (unchanged)
    private void joinServer(ActionEvent e) {
        String name = nameField.getText().trim();
        String server = serverField.getText().trim();
        String portText = portField.getText().trim();

        if (name.isEmpty() || server.isEmpty() || portText.isEmpty()) {
            showErrorDialog("Please fill all fields");
            return;
        }

        try {
            int port = Integer.parseInt(portText);
            Socket socket = new Socket(server, port);
            ChatClient client = new ChatClient(name, socket);
            new ChatFrame(client).setVisible(true);
            dispose();
        } catch (NumberFormatException ex) {
            showErrorDialog("Invalid port number");
        } catch (IOException ex) {
            showErrorDialog("Could not connect to server:\n" + ex.getMessage());
        }
    }

    private void createServer(ActionEvent e) {
        String name = nameField.getText().trim();
        String server = serverField.getText().trim();
        String portText = portField.getText().trim();

        if (name.isEmpty() || server.isEmpty() || portText.isEmpty()) {
            showErrorDialog("Please fill all fields");
            return;
        }

        try {
            int port = Integer.parseInt(portText);
            ChatServer chatServer = new ChatServer(port);
            new Thread(chatServer::start).start();

            // Small delay to ensure server is ready
            Thread.sleep(200);

            Socket socket = new Socket("localhost", port);
            ChatClient client = new ChatClient(name, socket);
            new ChatFrame(client).setVisible(true);
            dispose();
        } catch (NumberFormatException ex) {
            showErrorDialog("Invalid port number");
        } catch (IOException ex) {
            showErrorDialog("Could not create server:\n" + ex.getMessage());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            showErrorDialog("Server startup interrupted");
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this,
                "<html><div style='width:200px;'>" + message + "</div></html>",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}