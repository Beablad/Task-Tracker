public class Subtask extends Task {
    int idEpic;
    public Subtask(String taskName, String taskInfo, String taskStatus, Integer idEpic) {
        super(taskName, taskInfo, taskStatus);
        this.idEpic = idEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idEpic=" + idEpic +
                ", taskName='" + taskName + '\'' +
                ", taskInfo='" + taskInfo + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                '}';
    }
}
