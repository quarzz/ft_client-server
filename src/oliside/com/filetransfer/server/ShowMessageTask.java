package oliside.com.filetransfer.server;

import java.io.IOException;

public class ShowMessageTask implements ServerTask {

    private String message;

    public ShowMessageTask(String message) {
        this.message = message;
    }

    @Override
    public void process(ClientProcessor processor) throws IOException, ClassNotFoundException{
        processor.getOutputStream().writeObject("MESSAGE");
        processor.getOutputStream().writeObject(message);
    }
}