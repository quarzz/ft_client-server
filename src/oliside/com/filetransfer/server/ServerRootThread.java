package oliside.com.filetransfer.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerRootThread extends Thread {
    private ServerModel serverModel;

    public ServerRootThread(ServerModel serverModel) {
        this.serverModel = serverModel;
    }

    @Override
    public void run() {
        try (
            ServerSocket serverSocket =
                    new ServerSocket(serverModel.getPortNumber())
            ) {
            while (serverModel.isRunning()) {
                serverModel.getClientsPool().add(serverSocket.accept());
            }
        } catch (IOException exc) {

        }
    }
}
