package oliside.com.filetransfer.server;

public class ServerModel {
    private int portNumber;
    private volatile boolean running;
    private ServerRootThread serverRootThread;
    private ClientsPool clientsPool;

    public ServerModel(int portNumber) {
        this.portNumber = portNumber;
        this.running = false;
    }

    public void start() {
        running = true;
        serverRootThread = new ServerRootThread(this);
        serverRootThread.start();
    }

    public int getPortNumber() {
        return portNumber;
    }

    public boolean isRunning() {
        return running;
    }

    public ClientsPool getClientsPool() {
        return clientsPool;
    }
}
