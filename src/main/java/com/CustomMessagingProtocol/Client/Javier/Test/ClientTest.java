package com.CustomMessagingProtocol.Client.Javier.Test;

import com.CustomMessagingProtocol.Client.Javier.Model.Client;
import com.CustomMessagingProtocol.Client.Javier.View.ClientView;
import com.CustomMessagingProtocol.Client.Javier.Controller.ClientController;

import javax.swing.*;

/**
 * Clase simple para arrancar el cliente y probarlo.
 */
public class ClientTest {
    public static void main(String[] args) {
        String host = JOptionPane.showInputDialog("Servidor (host)", "192.168.101.42");
        String portStr = JOptionPane.showInputDialog("Puerto", "1234");
        String id = JOptionPane.showInputDialog("Tu ID (ej: u123)", "u" + (int)(Math.random() * 1000));
        String username = JOptionPane.showInputDialog("Tu nombre de usuario", "Javier");
        int port = Integer.parseInt(portStr);

        try {
            Client model = new Client(host, port, id, username);
            model.connect(); // hace LOGIN automáticamente
            ClientView view = new ClientView("Cliente - " + username);
            new ClientController(model, view);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar: " + e.getMessage());
            System.exit(1);
        }
    }
}
