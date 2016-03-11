package oliside.com.filetransfer.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class ClientProcessor extends Thread {
    private ConcurrentHashMap<String, ClientProcessor> processors;
    private Socket clientSocket;
    private Queue<ServerTask> tasks;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String username;

    public ClientProcessor(ConcurrentHashMap<String, ClientProcessor> processors, Socket clientSocket) {
        this.processors = processors;
        this.clientSocket = clientSocket;
        this.tasks = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {
        try {
            this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException exc) {
            return;
        }

        try {
            new LoginTask().process(this);

            while (true) {

                new CheckFileTask().process(this);
                if (!tasks.isEmpty()) {
                    tasks.poll().process(this);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException exc) {
                    System.out.println("interrupted processor");
                }
            }
        } catch (IOException | ClassNotFoundException exc) {
            if (username != null) {
                tasks.forEach(task -> {
                    if (task instanceof SendFileTask) {
                        SendFileTask sendFileTask = (SendFileTask)task;
                        try {
                            processors.get(sendFileTask.getSender()).getTasks().
                                    add(new ShowMessageTask(String.format(
                                            "Error. File was not received by %s", username)));
                            System.out.println("i am not useless code");
                        } catch (NullPointerException e) {

                        }
                    }
                });
                System.out.println("removing clients");
                processors.remove(username);
                processors.forEach((u, p) -> p.getTasks().add(new ShowUsersTask()));
                System.out.println("updating clients");
                //exc.printStackTrace();
            }
        } finally {
            try {
                clientSocket.close();
                outputStream.close();
                inputStream.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
    }

    public ConcurrentMap<String, ClientProcessor> getProcessors() {
        return processors;
    }

    public Queue<ServerTask> getTasks() {
        return tasks;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
