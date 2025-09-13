package com.CustomMessagingProtocol.ClienteDavid.test;



import com.CustomMessagingProtocol.ClienteDavid.controller.ClientController;
import com.CustomMessagingProtocol.ClienteDavid.model.Client;
import com.CustomMessagingProtocol.ClienteDavid.view.ClientView;

import javax.swing.*;

public class ClientTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Client client = new Client("127.0.0.1", 1234); // mismo puerto que el servidor
                ClientView view = new ClientView();
                new ClientController(client, view, "David");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
