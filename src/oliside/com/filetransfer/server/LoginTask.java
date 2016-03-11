package oliside.com.filetransfer.server;

import java.io.IOException;

public class LoginTask implements ServerTask {

    @Override
    public void process(ClientProcessor processor) throws IOException, ClassNotFoundException{
        processor.getOutputStream().writeObject("LOGIN");
        processor.getOutputStream().writeObject("Enter your username: ");

        String username = (String)processor.getInputStream().readObject();

        while (processor.getProcessors().containsKey(username)) {
            processor.getOutputStream().writeObject("This name is already taken, choose another one, please: ");
            username = (String)processor.getInputStream().readObject();
        }

        processor.getOutputStream().writeObject("LOGIN_END");

        processor.getProcessors().put(username, processor);
        processor.setUsername(username);
        processor.getProcessors().forEach((u, p) -> p.getTasks().add(new ShowUsersTask()));
    }
}
