package oliside.com.filetransfer.server;

import java.util.HashMap;
import java.util.Queue;

public class TaskList extends HashMap<String, Queue<ServerTask>> {

    private void addToEach(ServerTask task) {
        this.forEach((login, tasks) -> tasks.add(task));
    }
}
