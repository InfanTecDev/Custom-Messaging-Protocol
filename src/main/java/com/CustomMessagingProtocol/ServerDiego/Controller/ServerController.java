package com.CustomMessagingProtocol.ServerDiego.Controller;

import com.CustomMessagingProtocol.ServerDiego.Model.Server;
import com.CustomMessagingProtocol.GenericUser.User;
import com.CustomMessagingProtocol.Network.Protocol.MessageType;
import com.CustomMessagingProtocol.Network.Protocol.ProtocolMessage;
import com.CustomMessagingProtocol.Network.Protocol.ProtocolParser;
import com.CustomMessagingProtocol.ServerDiego.View.ServerView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class ServerController {

    private final Server model;
    private final ServerView view;
    private Map<String, User> users;

    public ServerController(Server model, ServerView view) {
        this.model = model;
        this.view = view;
        users = new HashMap<>();
    }

    public Map<String, User> getUsers() {
        return this.users;
    }

    public ServerView getView() {
        return this.view;
    }

    public void init() throws IOException {
        SwingUtilities.invokeLater(() -> {
            view.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            view.setVisible(true);
        });

        Thread serverThread = new Thread(() -> {
            while (true) {
                try {
                    Socket socket = model.getServerSocket().accept();
                    // Cuando se acepta un nuevo cliente, inicia un nuevo hilo para manejarlo
                    Thread clientThread = new Thread(new ClientHandler(socket, this));
                    clientThread.start();
                } catch (IOException e) {
                    System.out.println("Error al aceptar una nueva conexión: " + e.getMessage());
                }
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
    }

    // Método para manejar la desconexión desde un hilo de cliente
    public synchronized void removeUser(String userId) {
        if (this.users.containsKey(userId)) {
            this.users.remove(userId);
            System.out.println("Usuario " + userId + " desconectado y eliminado.");
            this.view.updateView(new ArrayList<>(this.users.values()));
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ServerController controller;
    private String userId;
    private DataInputStream input;
    private DataOutputStream output;

    public ClientHandler(Socket socket, ServerController controller) throws IOException {
        this.clientSocket = socket;
        this.controller = controller;
        this.input = new DataInputStream(clientSocket.getInputStream());
        this.output = new DataOutputStream(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (!clientSocket.isClosed()) {
                String messageSocket = input.readUTF();
                if (messageSocket.isBlank()) continue;

                System.out.println("Mensaje recibido: " + messageSocket);
                ProtocolMessage message = ProtocolParser.toProtocolMessage(messageSocket);

                // Mantiene el ID de usuario para saber qué cliente es
                this.userId = message.getIdUser();
                Map<String, User> users = controller.getUsers();
                if (ProtocolParser.isLoginMessage(message)) {
                    // Lógica para LOGIN
                    User newUser = new User(message.getIdUser(), message.getUsername(), clientSocket);
                    synchronized (users) {
                        if (!users.containsKey(newUser.getId())) {
                            users.put(newUser.getId(), newUser);
                            SwingUtilities.invokeLater(() -> controller.getView().addUserToView(new Object[]{newUser.getId(), newUser.getUser()}));
                            List<String> usersList = new ArrayList<>();
                            for (User user: users.values()) {
                                usersList.add(user.getId() + "-" + user.getUser());
                            }
                            for (User user : users.values()) {
                                if (!user.getId().equals(newUser.getId())) {
                                    user.getSalida().writeUTF(ProtocolParser.format(MessageType.PRIVATE_MESSAGE.name(), "",
                                            "", user.getId(), "Server", "Nuevo usuario conectado"));
                                }
                                user.getSalida().writeUTF(ProtocolParser.format(MessageType.PRIVATE_MESSAGE.name(), "",
                                        "", user.getId(), "Server", usersList.toString()));
                            }
                        }
                    }
                } else if (ProtocolParser.isLogoutMessage(message)) {
                    // Lógica para LOGOUT
                    User userToLogout = users.get(message.getFrom());
                    if (userToLogout != null) {
                        controller.removeUser(userToLogout.getId());
                        break; // Sale del bucle para cerrar el hilo
                    }
                } else if (ProtocolParser.isPrivateMessage(message)) {
                    // Lógica para PRIVATE_MESSAGE
                    String to = message.getTo();
                    if (users.containsKey(to)) {
                        User userTo = users.get(to);
                        DataOutputStream userToOutput = new DataOutputStream(userTo.getSocket().getOutputStream());
                        userToOutput.writeUTF(ProtocolParser.serialize(message));
                    } else {
                        output.writeUTF(ProtocolParser.format(MessageType.PRIVATE_MESSAGE.name(), "", "", message.getFrom(), "Server", "No fue posible enviar su mensaje, usuario destino no encontrado"));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Cliente desconectado: " + (userId != null ? userId : "desconocido"));
        } finally {
            // Cierra el socket al terminar la conexión
            try {
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (userId != null) {
                controller.removeUser(userId);
            }
        }
    }
}