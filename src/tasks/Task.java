package tasks;

public class Task {
    protected String taskName;
    protected String taskInfo;
    protected Statuses taskStatus;
    protected int taskId;

    public Task(String taskName, String taskInfo) {
        this.taskName = taskName;
        this.taskInfo = taskInfo;
        this.taskStatus = Statuses.NEW;
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
