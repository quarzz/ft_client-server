package oliside.com.filetransfer.server;

import java.io.IOException;

public class CheckFileTask implements ServerTask {

    @Override
    public void process(ClientProcessor processor) throws IOException, ClassNotFoundException{
        processor.getOutputStream().writeObject("CHECK_FILE");
        String answer = (String)processor.getInputStream().readObject();
        if (answer.equals("SEND")) {
            String receiver = (String)processor.getInputStream().readObject();
            long fileLength = processor.getInputStream().readLong();
            byte[] fileAsByteArray = new byte[(int)fileLength];
            int readCount = 0;
            while (readCount < fileLength) {
                readCount += processor.getInputStream().read(fileAsByteArray, readCount, (int) fileLength - readCount);
            }
            try {
                processor.getProcessors().get(receiver).getTasks().
                        add(new SendFileTask(fileAsByteArray, processor.getUsername()));
            } catch (NullPointerException e) {
                System.out.println("bad");
                processor.getTasks().add(new ShowMessageTask(String.format(
                                "Error. File was not received by %s", processor.getUsername())));
            }
        }
    }
}