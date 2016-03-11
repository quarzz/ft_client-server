package oliside.com.filetransfer.server;

import oliside.com.filetransfer.Constants;

public class Main {
    public static void main(String[] args) {
        new ServerModel(Constants.SERVER_PORT).start();
    }
}
