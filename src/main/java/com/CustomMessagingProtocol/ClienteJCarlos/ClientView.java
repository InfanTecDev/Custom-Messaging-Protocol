/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.CustomMessagingProtocol.ClienteJCarlos;

/**
 *
 * @author jcmot
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ClientView extends JFrame {

    private final JTextField hostField, portField, idField, usernameField, toField, messageField;
    private final JTextArea chatArea;
    private final JButton loginButton, sendButton, logoutButton;
    private final DefaultListModel<String> usersModel;
    private final JList<String> usersList;

    public ClientView() {
        setTitle("Cliente Chat");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel northPanel = new JPanel(new GridLayout(3, 4));
        hostField = new JTextField("192.168.56.1");
        portField = new JTextField("1234");
        idField = new JTextField("1");
        usernameField = new JTextField("User1");
        northPanel.add(new JLabel("Host:"));
        northPanel.add(hostField);
        northPanel.add(new JLabel("Puerto:"));
        northPanel.add(portField);
        northPanel.add(new JLabel("ID:"));
        northPanel.add(idField);
        northPanel.add(new JLabel("Usuario:"));
        northPanel.add(usernameField);

        loginButton = new JButton("Login");
        logoutButton = new JButton("Logout");
        northPanel.add(loginButton);
        northPanel.add(logoutButton);

        add(northPanel, BorderLayout.NORTH);

        // Área de chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Panel inferior
        JPanel southPanel = new JPanel(new BorderLayout());
        toField = new JTextField("DestinoID");
        messageField = new JTextField();
        sendButton = new JButton("Enviar");
        southPanel.add(new JLabel("Para:"), BorderLayout.WEST);
        southPanel.add(toField, BorderLayout.CENTER);
        southPanel.add(messageField, BorderLayout.SOUTH);
        southPanel.add(sendButton, BorderLayout.EAST);

        add(southPanel, BorderLayout.SOUTH);

        // Panel lateral (lista de usuarios conectados)
        usersModel = new DefaultListModel<>();
        usersList = new JList<>(usersModel);
        add(new JScrollPane(usersList), BorderLayout.EAST);
    }

    // Métodos para controlador
    public String getHost() { return hostField.getText(); }
    public int getPort() { return Integer.parseInt(portField.getText()); }
    public String getUserId() { return idField.getText(); }
    public String getUsername() { return usernameField.getText(); }
    public String getToUser() { return toField.getText(); }
    public String getMessage() { return messageField.getText(); }

    public void setLoginAction(ActionListener listener) { loginButton.addActionListener(listener); }
    public void setSendAction(ActionListener listener) { sendButton.addActionListener(listener); }
    public void setLogoutAction(ActionListener listener) { logoutButton.addActionListener(listener); }

    public void addMessage(String msg) {
        chatArea.append(msg + "\n");
    }

    public void updateUsers(List<String> users) {
        usersModel.clear();
        for (String user : users) {
            usersModel.addElement(user);
        }
    }
}

