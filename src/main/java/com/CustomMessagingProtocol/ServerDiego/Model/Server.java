package com.CustomMessagingProtocol.ServerDiego.Model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Server {

    private final ServerSocket serverSocket;

    public Server(int port, int users, InetAddress ip)  throws IOException{
        this.serverSocket = new ServerSocket(port, users, ip);
    }

    public Server(int port, int users)  throws IOException{
        this.serverSocket = new ServerSocket(port, users, null);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public int getPort() {
        return this.serverSocket.getLocalPort();
    }

    public InetAddress getIpAddress() {
        return this.serverSocket.getInetAddress();
    }
}
