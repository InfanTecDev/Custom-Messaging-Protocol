package com.CustomMessagingProtocol.ClienteDavid.view;

import javax.swing.*;
import java.awt.*;

public class ClientView extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    public ClientView() {
        super("Cliente - David");
        initComponents();
    }

    private void initComponents() {
        this.setSize(400, 500);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        sendButton = new JButton("Enviar");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(panel, BorderLayout.SOUTH);
    }

    public JTextArea getChatArea() { return chatArea; }
    public JTextField getInputField() { return inputField; }
    public JButton getSendButton() { return sendButton; }
}
