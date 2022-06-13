package tasks;

import managers.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Epic extends Task {
    private ArrayList<Subtask> listOfSubtask;

    public Epic(String taskName, String taskInfo) {
        this.taskName = taskName;
        this.taskInfo = taskInfo;
        this.taskId = Managers.getDefault().getId();
        listOfSubtask = new ArrayList<>();
    }

    public void setStartTime() {
        if (sortedSubtaskList().size() != 0) {
            this.startTime = sortedSubtaskList().get(0).getStartTime();
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        if (startTime==null){
            return null;
        }
        return sortedSubtaskList().get(listOfSubtask.size() - 1).getEndTime();
    }

    public void setDuration() {
        if (sortedSubtaskList().size() >= 2) {
            this.duration = Duration.between(sortedSubtaskList().get(0).getStartTime(),
                    sortedSubtaskList().get(sortedSubtaskList().size() - 1).getEndTime()).toMinutes();
        } else if (listOfSubtask.size() == 1) {
            this.duration = listOfSubtask.get(0).getDuration();
        } else duration = 0;
    }
//
    private List<Subtask> sortedSubtaskList() {
        return listOfSubtask.stream().filter(subtask -> subtask.getStartTime() != null).sorted((o1, o2) -> {
            if (o1.getStartTime().isAfter(o2.getStartTime())) {
                return 1;
            } else if (o1.getStartTime().isBefore(o2.getStartTime())) {
                return -1;
            } else return 0;
        }).collect(Collectors.toList());
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
        return "Epic{" +
                "taskName='" + taskName + '\'' +
                ", taskInfo='" + taskInfo + '\'' +
                ", taskStatus=" + taskStatus +
                ", taskId=" + taskId +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + getEndTime() +
                '}';
    }
}
