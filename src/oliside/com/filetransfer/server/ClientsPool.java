package oliside.com.filetransfer.server;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientsPool {
    private ConcurrentHashMap<String, ClientProcessor> processors;

    public ClientsPool() {
        processors = new ConcurrentHashMap<>();
    }

    public void add(Socket clientSocket) {
        new ClientProcessor(processors, clientSocket).start();
    }
}
