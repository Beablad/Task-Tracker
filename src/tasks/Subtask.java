package tasks;

import java.time.LocalDateTime;

public class Subtask extends Task {

    private int idEpic;

    public Subtask(String taskName, String taskInfo, Statuses status, LocalDateTime startTime, long duration, int idEpic) {
        super(taskName, taskInfo, status, startTime, duration);
        this.idEpic = idEpic;
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
                ", taskStatus=" + taskStatus +
                ", taskId=" + taskId +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + getEndTime() +
                '}';
    }
}