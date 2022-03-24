import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Subtask> listOfSubtask;

    public Epic(String taskName, String taskInfo, String taskStatus, ArrayList<Subtask> listOfSubtask) {
        super(taskName, taskInfo, taskStatus);
        this.listOfSubtask = listOfSubtask;
    }

    public ArrayList<Subtask> getListOfSubtask() {
        return listOfSubtask;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskName='" + taskName + '\'' +
                ", taskInfo='" + taskInfo + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                '}';
    }
}
