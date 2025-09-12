package com.MessagingWithSockets.GenericUser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class  User {
    private String id, user;
    private Socket socket;
    private DataInputStream entrada;
    private DataOutputStream salida;

    public User(String id, String user, Socket socket) {
        this.id = id;
        this.user = user;
        this.socket = socket;
        try {
            entrada = new DataInputStream(socket.getInputStream());
            salida = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public DataOutputStream getSalida() {
        return salida;
    }

    public DataInputStream getEntrada() {
        return entrada;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUser() {
        return user;
    }
}
