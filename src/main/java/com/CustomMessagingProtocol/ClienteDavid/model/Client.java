package com.CustomMessagingProtocol.ClienteDavid.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private final Socket socket;
    private final DataInputStream entrada;
    private final DataOutputStream salida;

    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.entrada = new DataInputStream(socket.getInputStream());
        this.salida = new DataOutputStream(socket.getOutputStream());
    }

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getEntrada() {
        return entrada;
    }

    public DataOutputStream getSalida() {
        return salida;
    }

    public void close() throws IOException {
        socket.close();
    }
}
