package com.CustomMessagingProtocol.Server.Test;

import com.CustomMessagingProtocol.Server.Controller.ServerController;
import com.CustomMessagingProtocol.Server.Model.Server;
import com.CustomMessagingProtocol.Network.NetworksUtilities;
import com.CustomMessagingProtocol.Server.View.ServerView;

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
