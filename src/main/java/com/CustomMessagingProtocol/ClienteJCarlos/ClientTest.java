/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.CustomMessagingProtocol.ClienteJCarlos;

/**
 *
 * @author jcmot
 */
    import javax.swing.*;

public class ClientTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientView view = new ClientView();
            new ClientController(view);
            view.setVisible(true);
        });
    }
}
