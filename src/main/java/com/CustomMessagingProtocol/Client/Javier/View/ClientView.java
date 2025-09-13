package com.CustomMessagingProtocol.Client.Javier.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.List;

/**
 * Interfaz sencilla con:
 * - JTextArea para mensajes
 * - JComboBox para elegir destinatario
 * - JTextField + Botón enviar
 */
public class ClientView extends JFrame {
    private final JTextArea chatArea = new JTextArea(18, 50);
    private final JTextField inputField = new JTextField(36);
    private final JButton sendButton = new JButton("Enviar");
    private final JComboBox<String> usersCombo = new JComboBox<>();
    private final JLabel statusLabel = new JLabel("Desconectado");

    public ClientView(String title) {
        super(title);
        initUI();
    }

    private void initUI() {
        chatArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(chatArea);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(new JLabel("Para:"));
        bottom.add(usersCombo);
        bottom.add(inputField);
        bottom.add(sendButton);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(statusLabel, BorderLayout.NORTH);
        c.add(scroll, BorderLayout.CENTER);
        c.add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // cerramos con evento para enviar LOGOUT
    }

    public void addSendListener(ActionListener a) {
        sendButton.addActionListener(a);
        inputField.addActionListener(a); // Enter también envía
    }

    public void addWindowCloseListener(WindowAdapter adapter) {
        addWindowListener(adapter);
    }

    public void appendMessage(String s) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(s + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public void updateUserList(List<String> users) {
        SwingUtilities.invokeLater(() -> {
            usersCombo.removeAllItems();
            for (String u : users) usersCombo.addItem(u);
        });
    }

    public String getMessageText() { return inputField.getText(); }
    public void clearInput() { inputField.setText(""); }
    public String getSelectedUserId() {
        Object item = usersCombo.getSelectedItem();
        if (item == null) return "";
        // Asumo formato "id-username" como hace el servidor al llenar la lista
        String s = item.toString();
        if (s.contains("-")) return s.split("-")[0];
        return s;
    }

    public void setStatus(String s) { SwingUtilities.invokeLater(() -> statusLabel.setText(s)); }
}
