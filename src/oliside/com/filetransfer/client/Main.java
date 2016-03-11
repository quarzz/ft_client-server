package oliside.com.filetransfer.client;

import oliside.com.filetransfer.Constants;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientModel(Constants.SERVER_PORT));
    }
}
