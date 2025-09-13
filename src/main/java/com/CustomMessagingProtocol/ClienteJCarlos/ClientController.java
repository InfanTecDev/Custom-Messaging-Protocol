/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.CustomMessagingProtocol.ClienteJCarlos;

/**
 *
 * @author jcmot
 */
import com.CustomMessagingProtocol.GenericUser.User;
import com.CustomMessagingProtocol.Network.Protocol.MessageType;
import com.CustomMessagingProtocol.Network.Protocol.ProtocolMessage;
import com.CustomMessagingProtocol.Network.Protocol.ProtocolParser;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ClientController {

    private final ClientView view;
    private User user;
    private DataInputStream input;
    private DataOutputStream output;
    private Socket socket;

    public ClientController(ClientView view) {
        this.view = view;

        // Eventos de la vista
        this.view.setLoginAction(e -> login());
        this.view.setSendAction(e -> sendMessage());
        this.view.setLogoutAction(e -> logout());
    }

    private void login() {
        try {
            String host = view.getHost();
            int port = view.getPort();
            String id = view.getUserId();
            String username = view.getUsername();

            socket = new Socket(host, port);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            user = new User(id, username, socket);

            // Enviar LOGIN
            String loginMessage = ProtocolParser.format(
                    MessageType.LOGIN.name(),
                    user.getId(),
                    user.getUser(),
                    "",
                    "",
                    "Conectado"
            );
            output.writeUTF(loginMessage);

            // Hilo para escuchar mensajes del servidor
            Thread listenThread = new Thread(this::listenServer);
            listenThread.setDaemon(true);
            listenThread.start();

            JOptionPane.showMessageDialog(view, "Conectado al servidor!");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view, "Error al conectar: " + ex.getMessage());
        }
    }

    private void sendMessage() {
        try {
            String to = view.getToUser();
            String messageBody = view.getMessage();

            ProtocolMessage message = new ProtocolMessage(
                    MessageType.PRIVATE_MESSAGE,
                    Map.of(
                            "Id", user.getId(),
                            "Username", user.getUser(),
                            "To", to,
                            "From", user.getId()
                    ),
                    messageBody
            );

            output.writeUTF(ProtocolParser.serialize(message));
            view.addMessage("Yo -> " + to + ": " + messageBody);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view, "Error al enviar mensaje: " + ex.getMessage());
        }
    }

    private void logout() {
        try {
            if (socket != null && !socket.isClosed()) {
                String logoutMsg = ProtocolParser.format(
                        MessageType.LOGOUT.name(),
                        user.getId(),
                        user.getUser(),
                        "",
                        user.getId(),
                        "Desconectado"
                );
                output.writeUTF(logoutMsg);
                socket.close();
                view.addMessage("Has cerrado sesión.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view, "Error al desconectarse: " + ex.getMessage());
        }
    }

    private void listenServer() {
        try {
            while (!socket.isClosed()) {
                String serverMsg = input.readUTF();
                ProtocolMessage msg = ProtocolParser.toProtocolMessage(serverMsg);

                if (msg.body().startsWith("[") && msg.body().endsWith("]")) {
                    // Es la lista de usuarios conectados
                    String clean = msg.body().substring(1, msg.body().length() - 1);
                    List<String> users = Arrays.asList(clean.split(", "));
                    SwingUtilities.invokeLater(() -> view.updateUsers(users));
                } else {
                    // Es un mensaje normal
                    SwingUtilities.invokeLater(() ->
                            view.addMessage(msg.getFrom() + " -> " + msg.getTo() + ": " + msg.body())
                    );
                }
            }
        } catch (IOException ex) {
            SwingUtilities.invokeLater(() -> view.addMessage("Desconectado del servidor."));
        }
    }
}

