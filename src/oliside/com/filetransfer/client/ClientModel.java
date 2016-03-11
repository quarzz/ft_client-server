package oliside.com.filetransfer.client;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class ClientModel extends JFrame {
    private int portNumber;
    private Socket socket;
    private ClientThread thread;
    private File fileToSend;
    private ReentrantLock lockBusy;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    private boolean isBusy;
    private String fileReceiver;

    private JList<String> usersList;
    private DefaultListModel<String> usersModel;

    public ClientModel(int portNumber) {
        this.portNumber = portNumber;
        this.lockBusy = new ReentrantLock();

        this.usersModel = new DefaultListModel<>();
        this.usersList = new JList<>(this.usersModel);
        usersList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {
                    JFileChooser fileChooser = new JFileChooser();
                    if (fileChooser.showOpenDialog(e.getComponent()) != JFileChooser.APPROVE_OPTION)
                        return;


                    int index = usersList.locationToIndex(e.getPoint());
                    String fileReceiver = usersModel.elementAt(index);
                    if (sendFile(fileChooser.getSelectedFile(), fileReceiver));
                }
            }
        });

        this.add(usersList);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.start();
    }

    public void start() {
        try {
            this.socket = new Socket("127.0.0.1", portNumber);
            this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
            this.inputStream = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException exc) {
            System.out.println("exc const clmodel");
        }
        this.isBusy = false;
        this.thread = new ClientThread(this);
        this.thread.start();
    }

    public String getFileReceiver() {
        return fileReceiver;
    }

    public boolean sendFile(File fileToSend, String username) {

        lockBusy.lock();

        if (isBusy) {
            lockBusy.unlock();
            return false;
        }

        this.fileReceiver = username;
        this.fileToSend = fileToSend;
        isBusy = true;
        lockBusy.unlock();
        return true;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public DefaultListModel<String> getUsersModel() {
        return usersModel;
    }

    public ReentrantLock getLockBusy() {
        return lockBusy;
    }

    public File getFileToSend() {
        return fileToSend;
    }

    public boolean isBusy() {
        return isBusy;
    }
}
