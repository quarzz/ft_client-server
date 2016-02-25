package oliside.com.filetransfer.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Queue;

public class ClientProcessor extends Thread {
    private TaskList taskList;
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ClientProcessor(TaskList taskList, Socket clientSocket) {
        this.taskList = taskList;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException exc) {
            return;
        }
        new LoginTask().process(this);
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }
}
