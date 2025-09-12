package com.CustomMessagingProtocol.Server.View;

import com.CustomMessagingProtocol.GenericUser.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class ServerView extends JFrame {

    private JTable usersTable;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private GridBagConstraints gbc;
    public Vector<String> columns;

    public ServerView() {
        super("ServerView");
        this.model = new DefaultTableModel();
        this.usersTable = new JTable(model);
        this.scrollPane = new JScrollPane(usersTable);
        this.gbc = new GridBagConstraints();
        columns = new Vector<>(List.of("ID", "Username"));
        this.initComponents();
    }

    private void initComponents() {
        // Configuraciones generales de la ventana del servidor
        this.setSize(500, 700);
        //this.setResizable(false);
        this.setLayout(new GridBagLayout());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configuración de la tabla de usuarios
        scrollPane.setPreferredSize(new Dimension(400, 600));
        usersTable.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 18));
        model.setColumnIdentifiers(columns);
        usersTable.setModel(model);

        // Configuración de la tabla
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Agregar elementos a la ventana
        this.add(scrollPane, gbc);

        // Hacer visible la ventana del servidor
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ServerView::new);
    }

    public DefaultTableModel getModel() {
        return this.model;
    }

    public void addUserToView(Object[] data) {
        DefaultTableModel model = (DefaultTableModel) this.usersTable.getModel();
        model.addRow(data);
    }

    public void updateView(List<User> users) {
        DefaultTableModel model = (DefaultTableModel) this.usersTable.getModel();
        model.setRowCount(0);
        for (User user : users) {
            model.addRow(new Object[]{user.getId(), user.getUser()});
        }
    }
}
