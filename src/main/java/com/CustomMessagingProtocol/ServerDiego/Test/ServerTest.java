package com.CustomMessagingProtocol.ServerDiego.Test;

import com.CustomMessagingProtocol.ServerDiego.Controller.ServerController;
import com.CustomMessagingProtocol.ServerDiego.Model.Server;
import com.CustomMessagingProtocol.Network.NetworksUtilities;
import com.CustomMessagingProtocol.ServerDiego.View.ServerView;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;

public class ServerTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
            try {
                InetAddress address = NetworksUtilities.getActuallyIp();
                System.out.println(address.getHostAddress());
                Server server = new Server(1234, 50, address);
                ServerView view = new ServerView();
                ServerController controller = new ServerController(server, view);
                controller.init();
            } catch(IOException err) {
                System.err.println("Error al crear sockets: " + err.getMessage());
                System.exit(0);
            }
        });
    }
}
