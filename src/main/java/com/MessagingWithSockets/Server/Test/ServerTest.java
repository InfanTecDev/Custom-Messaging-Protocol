package com.MessagingWithSockets.Server.Test;

import com.MessagingWithSockets.Server.Controller.ServerController;
import com.MessagingWithSockets.Server.Model.Server;
import com.MessagingWithSockets.Network.NetworksUtilities;
import com.MessagingWithSockets.Server.View.ServerView;

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
