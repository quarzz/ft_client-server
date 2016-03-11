package oliside.com.filetransfer.server;

import java.io.IOException;
import java.util.HashSet;

public class ShowUsersTask implements ServerTask {

    @Override
    public void process(ClientProcessor processor) throws IOException{
        processor.getOutputStream().writeObject("USERS");
        processor.getOutputStream().writeObject(new HashSet<>(processor.getProcessors().keySet()));
    }
}
