package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

public class Epic extends Task {
    private ArrayList<Subtask> listOfSubtask;

    public Epic(String taskName, String taskInfo, LocalDateTime startTime, long duration) {
        super(taskName, taskInfo, startTime, duration);
        this.listOfSubtask = new ArrayList<>();
    }

    public ArrayList<Subtask> getListOfSubtask() {
        return listOfSubtask;
    }

    public void addSubtaskInEpic(Subtask subtask) {
        listOfSubtask.add(subtask);
    }

    public void removeSubtaskInEpic(Subtask subtask) {
        listOfSubtask.remove(subtask);
    }

    @Override
    public String toString() {
        return "Tasks.Epic{" +
                "taskName='" + taskName + '\'' +
                ", taskInfo='" + taskInfo + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", taskId=" + taskId +
                '}';
    }
}
