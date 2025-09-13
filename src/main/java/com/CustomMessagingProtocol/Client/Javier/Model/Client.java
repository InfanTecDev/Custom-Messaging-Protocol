package com.CustomMessagingProtocol.Client.Javier.Model;

import com.CustomMessagingProtocol.Network.Protocol.ProtocolParser;
import com.CustomMessagingProtocol.Network.Protocol.MessageType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Modelo que maneja la conexión y envíos/recepción básicos.
 * Adapta los nombres de los campos si tu ProtocolParser/ProtocolMessage cambia.
 */
public class Client {
    private final String host;
    private final int port;
    private final String id;
    private final String username;

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private Thread listenThread;

    public interface MessageListener {
        void onMessage(String message);
    }

    public Client(String host, int port, String id, String username) {
        this.host = host;
        this.port = port;
        this.id = id;
        this.username = username;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        sendLogin();
    }

    public void startListening(MessageListener listener) {
        listenThread = new Thread(() -> {
            try {
                while (!socket.isClosed()) {
                    String msg = input.readUTF();
                    if (msg == null || msg.isBlank()) continue;
                    listener.onMessage(msg);
                }
            } catch (IOException ex) {
                // se desconectó o error de I/O
            }
        });
        listenThread.setDaemon(true);
        listenThread.start();
    }

    public void sendLogin() throws IOException {
        // Basado en el uso en ServerController: ProtocolParser.format(type, ..)
        String formatted = ProtocolParser.format(MessageType.LOGIN.name(), "", "", id, username, "");
        output.writeUTF(formatted);
        output.flush();
    }

    public void sendPrivateMessage(String toId, String message) throws IOException {
        String formatted = ProtocolParser.format(MessageType.PRIVATE_MESSAGE.name(), message, toId, id, username, message);
        output.writeUTF(formatted);
        output.flush();
    }

    public void sendLogout() {
        try {
            String formatted = ProtocolParser.format(MessageType.LOGOUT.name(), "", "", id, username, "");
            output.writeUTF(formatted);
            output.flush();
        } catch (IOException ignored) { }
        try { if (socket != null) socket.close(); } catch (IOException ignored) { }
    }

    public boolean isConnected() {
        return socket != null && !socket.isClosed();
    }

    public String getUsername() { return username; }
    public String getId() { return id; }
}
