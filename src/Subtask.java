public class Subtask extends Task {
    private int idEpic;

    public Subtask(String taskName, String taskInfo, Integer idEpic) {
        super(taskName, taskInfo);
        String subtaskStatus = "";
        this.idEpic = idEpic;
        taskId = 0;
    }

    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }


    @Override
    public String toString() {
        return "Subtask{" +
                "idEpic=" + idEpic +
                ", taskName='" + taskName + '\'' +
                ", taskInfo='" + taskInfo + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", taskId=" + taskId +
                '}';
    }
}
