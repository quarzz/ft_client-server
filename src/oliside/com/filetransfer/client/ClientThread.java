package oliside.com.filetransfer.client;

import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.*;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class ClientThread extends Thread {

    private ClientModel model;
    private String username;

    public ClientThread(ClientModel model) {
        this.model = model;
    }

    @Override
    public void run() {
        ObjectOutputStream os = model.getOutputStream();
        ObjectInputStream is = model.getInputStream();

        try {
            while (true) {
                String taskName = (String) is.readObject();
                switch (taskName) {
                    case "LOGIN":
                        String usernamePrompt = (String) is.readObject();
                        username = JOptionPane.showInputDialog(model, usernamePrompt);
                        os.writeObject(username);
                        String msg = (String) is.readObject();
                        while (!msg.trim().equals("LOGIN_END")) {
                            username = JOptionPane.showInputDialog(msg);
                            os.writeObject(username);
                            msg = (String)is.readObject();
                        }
                        break;
                    case "USERS":
                        Set<String> usernames = (HashSet<String>)is.readObject();
                        usernames.remove(username);
                        DefaultListModel<String> listModel = model.getUsersModel();
                        listModel.clear();
                        usernames.forEach(listModel::addElement);
                        break;
                    case "CHECK_FILE":
                        model.getLockBusy().lock();
                        if (model.isBusy()) {
                            os.writeObject("SEND");
                            os.writeObject(model.getFileReceiver());
                            File fileToSend = model.getFileToSend();

                            os.writeLong(fileToSend.length());
                            BufferedInputStream bis =
                                    new BufferedInputStream(new FileInputStream(fileToSend));
                            byte[] fileByteArray = new byte[(int)fileToSend.length()];
                            int readCount = 0;
                            while (readCount < fileToSend.length())
                                readCount += bis.read(fileByteArray, readCount, (int)fileToSend.length() - readCount);
                            bis.close();
                            os.write(fileByteArray, 0, fileByteArray.length);
                            os.flush();
                            model.setBusy(false);
                        } else {
                            os.writeObject("NO");
                        }
                        model.getLockBusy().unlock();
                        break;
                    case "SEND_FILE":
                        JFileChooser fileChooser = new JFileChooser();
                        if (fileChooser.showSaveDialog(model) != JFileChooser.APPROVE_OPTION) {
                            os.writeObject("CANCEL");
                            break;
                        }
                        os.writeObject("APPROVE");
                        File fileSave = fileChooser.getSelectedFile();
                        FileOutputStream fos = new FileOutputStream(fileSave);
                        int length = is.readInt();
                        byte[] fileAsByteArray = new byte[length];
                        int readCount = 0;
                        try {
                            while (readCount < length) {
                                readCount += is.read(fileAsByteArray, readCount, fileAsByteArray.length - readCount);
                                System.out.println("readcount " + readCount);
                            }
                            fos.write(fileAsByteArray, 0, fileAsByteArray.length);
                            fos.flush();
                            fos.close();
                        } catch (Exception exc) {
                            os.writeObject("ERROR");
                        }
                        os.writeObject("RECEIVED");
                        JOptionPane.showMessageDialog(model, "File saved");
                        break;
                    case "MESSAGE":
                        String message = (String)is.readObject();
                        JOptionPane.showMessageDialog(model, message);
                        break;
                }
            }
        } catch (SocketException | EOFException exc) {
            JOptionPane.showMessageDialog(model, "Server was disconnected");
            System.exit(0);
        } catch (ClassNotFoundException | IOException exc) {
            System.out.println("exc in client");
            exc.printStackTrace();
        }
    }
}
