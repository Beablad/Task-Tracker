import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> listOfSubtask;

    public Epic(String taskName, String taskInfo) {
        super(taskName, taskInfo);
        this.listOfSubtask = new ArrayList<>();
    }

    public ArrayList<Subtask> getListOfSubtask() {
        return listOfSubtask;
    }

    public void setListOfSubtask(Subtask subtask) {
        listOfSubtask.add(subtask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskName='" + taskName + '\'' +
                ", taskInfo='" + taskInfo + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", taskId=" + taskId +
                '}';
    }
}
