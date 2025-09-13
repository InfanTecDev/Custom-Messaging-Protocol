package com.CustomMessagingProtocol.Client.Javier.Controller;

import com.CustomMessagingProtocol.Client.Javier.Model.Client;
import com.CustomMessagingProtocol.Client.Javier.View.ClientView;
import com.CustomMessagingProtocol.Network.Protocol.ProtocolParser;
import com.CustomMessagingProtocol.Network.Protocol.ProtocolMessage;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador que conecta Vista <-> Modelo.
 * - Envía mensajes cuando el usuario hace clic
 * - Escucha mensajes entrantes y actualiza la vista
 * - Envía LOGOUT cuando la ventana se cierra
 */
public class ClientController {
    private final Client model;
    private final ClientView view;

    public ClientController(Client model, ClientView view) {
        this.model = model;
        this.view = view;
        setup();
    }

    private void setup() {
        // Cuando se pulsa enviar (o Enter)
        view.addSendListener(e -> {
            String text = view.getMessageText().trim();
            if (text.isEmpty()) return;
            String toId = view.getSelectedUserId();
            try {
                model.sendPrivateMessage(toId, text);
                view.appendMessage("Yo -> " + (toId.isEmpty() ? "Todos" : toId) + ": " + text);
                view.clearInput();
            } catch (IOException ex) {
                view.appendMessage("Error enviando mensaje: " + ex.getMessage());
            }
        });

        // Cuando se cierra la ventana: enviar LOGOUT
        view.addWindowCloseListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                model.sendLogout();
                // pequeño delay para permitir envío; opcional
                try { Thread.sleep(150); } catch (InterruptedException ignored) {}
                System.exit(0);
            }
        });

        // Escucha mensajes del servidor
        model.startListening(raw -> {
            // Intentamos parsear con ProtocolParser (si existe)
            try {
                ProtocolMessage pm = ProtocolParser.toProtocolMessage(raw);
                // ADAPTA ESTO según getters reales de ProtocolMessage:
                String from = pm.getFrom();          // tal vez getFrom() / getIdUser()
                String username = pm.getUsername(); // tal vez getUsername()
                String message = pm.body();  // body() es el campo de contenido en un record
                String display = (username != null && !username.isBlank() ? username : from) + ": " + message;
                view.appendMessage(display);

                // Si el servidor envía la lista de usuarios (como en ServerController),
                // el mensaje puede contener "[id1-name1, id2-name2]" — lo detectamos y actualizamos
                if (message != null && message.startsWith("[") && message.contains("-")) {
                    String cleaned = message.replace("[", "").replace("]","").trim();
                    List<String> list = new ArrayList<>();
                    if (!cleaned.isEmpty()) {
                        for (String part : cleaned.split(",")) list.add(part.trim());
                    }
                    view.updateUserList(list);
                }
            } catch (Exception ex) {
                // Si no podemos parsear, mostramos el mensaje crudo
                view.appendMessage("RAW: " + raw);
            }
        });

        SwingUtilities.invokeLater(() -> {
            view.setStatus("Conectado como " + model.getUsername());
            view.setVisible(true);
        });
    }
}
