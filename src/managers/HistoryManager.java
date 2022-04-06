package managers;

import tasks.Task;

import java.util.ArrayList;



public interface HistoryManager {

    public void addHistory(Task task);

    public ArrayList<Task> getHistory();
}
