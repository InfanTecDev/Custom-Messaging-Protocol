package com.CustomMessagingProtocol.ClienteDavid.controller;

import com.CustomMessagingProtocol.ClienteDavid.model.Client;
import com.CustomMessagingProtocol.ClienteDavid.view.ClientView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ClientController {
    private final Client model;
    private final ClientView view;
    private final String username;

    public ClientController(Client model, ClientView view, String username) {
        this.model = model;
        this.view = view;
        this.username = username;

        init();
    }

    private void init() {
        view.setVisible(true);

        // Enviar mensaje al presionar botón o Enter
        view.getSendButton().addActionListener(e -> sendMessage());
        view.getInputField().addActionListener(e -> sendMessage());

        // Cerrar sesión cuando se cierre la ventana
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    model.getSalida().writeUTF(username + " LOGOUT");
                    model.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Hilo para escuchar mensajes del servidor
        new Thread(() -> {
            try {
                while (true) {
                    String msg = model.getEntrada().readUTF();
                    SwingUtilities.invokeLater(() -> view.getChatArea().append(msg + "\n"));
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> view.getChatArea().append("Conexión cerrada.\n"));
            }
        }).start();

        // Enviar LOGIN al conectar
        try {
            model.getSalida().writeUTF(username + " LOGIN");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String msg = view.getInputField().getText().trim();
        if (!msg.isEmpty()) {
            try {
                model.getSalida().writeUTF(username + ": " + msg);
                view.getInputField().setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
