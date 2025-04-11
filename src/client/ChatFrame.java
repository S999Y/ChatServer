package client;

import javax.swing.*;
import javax.swing.text.*;
import visual.Visual;
import java.awt.*;
import java.awt.event.*;
import java.util.Base64;
import java.util.List;

public class ChatFrame extends JFrame {
    private ChatClient client;
    private DefaultListModel<String> userListModel;
    private JTextPane chatPane;
    private JTextField messageField;
    private StyleContext styleContext;
    private Style leftStyle, rightStyle;
    private Visual encryption;

    public ChatFrame(ChatClient client) {
        this.client = client;
        this.encryption = new Visual("Encryption");
        client.setChatFrame(this);

        setTitle("Chat Application - " + client.getClientName());
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 400));

        // Main panel with border color and padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 120, 215), 3),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        setContentPane(mainPanel);

        // Initialize styles for message alignment
        styleContext = new StyleContext();
        leftStyle = styleContext.addStyle("Left", null);
        StyleConstants.setAlignment(leftStyle, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(leftStyle, Color.DARK_GRAY);
        StyleConstants.setFontFamily(leftStyle, "SansSerif");
        StyleConstants.setFontSize(leftStyle, 14);

        rightStyle = styleContext.addStyle("Right", null);
        StyleConstants.setAlignment(rightStyle, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(rightStyle, new Color(0, 128, 0));
        StyleConstants.setFontFamily(rightStyle, "SansSerif");
        StyleConstants.setFontSize(rightStyle, 14);
        StyleConstants.setBold(rightStyle, true);

        // User list panel
        userListModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(userListModel);
        userList.setPreferredSize(new Dimension(200, 0));
        userList.setBackground(new Color(240, 240, 240));
        userList.setFont(new Font("SansSerif", Font.BOLD, 14));
        userList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Chat pane
        chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setContentType("text/html");
        chatPane.setEditorKit(new WrapEditorKit());
        chatPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane chatScrollPane = new JScrollPane(chatPane);

        // Message field with modern design
        messageField = new JTextField();
        messageField.setPreferredSize(new Dimension(0, 50));
        messageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 120, 215), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        messageField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageField.setBackground(new Color(245, 245, 245));
        messageField.setForeground(Color.DARK_GRAY);
        messageField.addActionListener(e -> sendMessage());

        // Send button with modern design
        JButton sendButton = createModernButton("Send", new Color(0, 122, 255));
        sendButton.addActionListener(e -> sendMessage());

        // Bottom panel with message field and send button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        // Split panel for user list and chat area
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(userList), chatScrollPane);
        splitPane.setDividerLocation(200);

        // Adding components to main panel
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Start message listening thread
        new Thread(client::listenForMessages).start();
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            appendMessage("You: " + message, true);
            byte[] encryptedMsg = this.encryption.encrypt(message);
            client.sendMessage(Base64.getEncoder().encodeToString(encryptedMsg));
            messageField.setText("");
        }

    }

    public void appendMessage(String message, boolean isSent) {
        SwingUtilities.invokeLater(() -> {
            try {
                Document doc = chatPane.getDocument();
                SimpleAttributeSet set = new SimpleAttributeSet();

                StyleConstants.setAlignment(set, isSent ? StyleConstants.ALIGN_RIGHT : StyleConstants.ALIGN_LEFT);
                StyleConstants.setForeground(set, isSent ? new Color(0, 128, 0) : Color.DARK_GRAY);
                StyleConstants.setFontFamily(set, "SansSerif");
                StyleConstants.setFontSize(set, 16);
                StyleConstants.setBold(set, isSent);

                doc.insertString(doc.getLength(), message + "\n", set);
                chatPane.setCaretPosition(doc.getLength());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateUserList(List<String> users) {
        SwingUtilities.invokeLater(() -> {
            userListModel.clear();
            for (String user : users) {
                userListModel.addElement(user);
            }
        });
    }

    private static class WrapEditorKit extends StyledEditorKit {
        public ViewFactory getViewFactory() {
            return new WrapColumnFactory();
        }
    }

    private static class WrapColumnFactory implements ViewFactory {
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                switch (kind) {
                    case AbstractDocument.ContentElementName:
                        return new LabelView(elem);
                    case AbstractDocument.ParagraphElementName:
                        return new ParagraphView(elem);
                    case AbstractDocument.SectionElementName:
                        return new BoxView(elem, View.Y_AXIS);
                    case StyleConstants.ComponentElementName:
                        return new ComponentView(elem);
                    case StyleConstants.IconElementName:
                        return new IconView(elem);
                    default:
                        return new LabelView(elem);
                }
            }
            return new LabelView(elem);
        }
    }

    private JButton createModernButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
        button.setOpaque(false);

        return button;
    }
}
