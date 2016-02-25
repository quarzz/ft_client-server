package oliside.com.filetransfer.server;

import java.net.Socket;
import java.util.Map;
import java.util.Queue;

public class ClientsPool {
    private TaskList taskList;

    public void add(Socket clientSocket) {
        new ClientProcessor(taskList, clientSocket).start();
    }
}
