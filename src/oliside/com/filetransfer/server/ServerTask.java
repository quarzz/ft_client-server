package oliside.com.filetransfer.server;

import java.io.IOException;

public interface ServerTask {
    void process(ClientProcessor processor) throws IOException, ClassNotFoundException;
}
