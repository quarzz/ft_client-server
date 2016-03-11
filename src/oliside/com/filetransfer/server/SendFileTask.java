package oliside.com.filetransfer.server;

import java.io.IOException;

public class SendFileTask implements ServerTask {

    private byte[] fileAsByteArray;
    private String sender;

    public SendFileTask(byte[] fileAsByteArray, String sender) {
        this.fileAsByteArray = fileAsByteArray;
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    @Override
    public void process(ClientProcessor processor) {
        System.out.println("send file in " + processor.getUsername() + "thread");
        try {
            processor.getOutputStream().writeObject("SEND_FILE");
            System.out.println("SEND_FILE server task");
            String response = (String) processor.getInputStream().readObject();
            System.out.println("response: " + response);
            if (response.equalsIgnoreCase("CANCEL")) {
                processor.getProcessors().get(sender).getTasks().
                        add(new ShowMessageTask(String.format(
                                "%s cancelled file transfer", processor.getUsername())));
                return;
            }
            processor.getOutputStream().writeInt(fileAsByteArray.length);
            processor.getOutputStream().write(fileAsByteArray, 0, fileAsByteArray.length);
            processor.getOutputStream().flush();
            System.out.println("file sent");
            response = (String)processor.getInputStream().readObject();
            if (response.equals("RECEIVED"))
                processor.getProcessors().get(sender).getTasks().
                        add(new ShowMessageTask(String.format(
                                "File received by %s", processor.getUsername())));
            else
                processor.getProcessors().get(sender).getTasks().
                        add(new ShowMessageTask(String.format(
                                "Error. File was not received by %s", processor.getUsername())));

        } catch (IOException | ClassNotFoundException exc) {
            System.out.println("bad things in sending to " + processor.getUsername());
            processor.getProcessors().get(sender).getTasks().
                    add(new ShowMessageTask(String.format(
                            "Error. File was not received by %s", processor.getUsername())));
        }
    }
}
