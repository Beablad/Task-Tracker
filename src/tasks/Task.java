package tasks;

import java.time.LocalDateTime;

public class Task {
    protected String taskName;
    protected String taskInfo;
    protected Statuses taskStatus;
    protected int taskId;
    protected LocalDateTime startTime;
    protected long duration;


    public Task(String taskName, String taskInfo, LocalDateTime startTime, long duration) {
        this.taskName = taskName;
        this.taskInfo = taskInfo;
        this.taskStatus = Statuses.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalDateTime getEndTime(){
        return startTime.plusMinutes(duration);
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public Statuses getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Statuses taskStatus) {
        this.taskStatus = taskStatus;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Tasks.Task{" +
                "taskName='" + taskName + '\'' +
                ", taskInfo='" + taskInfo + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", taskId=" + taskId +
                '}';
    }
}
