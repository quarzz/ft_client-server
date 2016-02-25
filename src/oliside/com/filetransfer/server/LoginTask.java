package oliside.com.filetransfer.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginTask implements ServerTask {

    @Override
    public void process(ClientProcessor processor) {
        ObjectOutputStream outputStream = processor.getOutputStream();
        ObjectInputStream inputStream = processor.getInputStream();


    }
}
