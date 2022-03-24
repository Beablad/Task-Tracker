import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    HashMap<Integer, Task> taskList;
    HashMap<Integer, Epic> epicList;
    HashMap<Integer, Subtask> subtaskList;
    int taskId;

    public Manager() {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.subtaskList = new HashMap<>();
        this.taskId = 1;
    }


    public int setTaskId() {
        return taskId++;
    }

    void putTask(Task task) {
        taskList.put(setTaskId(), task);
    }

    void putEpic(Epic epic) {
        epicList.put(setTaskId(), epic);
    }

    void putSubtask(Subtask subtask) {
        subtaskList.put(setTaskId(), subtask);
    }

    void printTaskInfo() {
        System.out.println(taskList);
    }

    void printEpicInfo() {
        System.out.println(epicList);
    }

    void printSubtaskInfo() {
        System.out.println(subtaskList);
    }

    Task printTaskById(int id) {
        Task task = taskList.get(id);
        if (task != null) {
            return task;
        }
        return null;
    }

    Epic printEpicById(int id) {
        Epic epic = epicList.get(id);
        if (epic != null) {
            return epic;
        }
        return null;
    }

    Subtask printSubtaskById(int id) {
        Subtask subtask = subtaskList.get(id);
        if (subtask != null) {
            return subtask;
        }
        return null;
    }

    void clearTaskList() {
        taskList.clear();
    }

    void clearEpicList() {
        epicList.clear();
    }

    void clearSubtaskList() {
        taskList.clear();
    }

    void removeTask(int id) {
        subtaskList.remove(id);
    }

    void removeEpic(int id) {
        epicList.remove(id);
    }

    void removeSubtask(int id) {
        subtaskList.remove(id);
    }

    void updateTask(Task task, int id) {
        Task currentTask = taskList.get(id);
        if (currentTask != null) {
            currentTask.setTaskName(task.getTaskName());
            currentTask.setTaskInfo(task.getTaskInfo());
            currentTask.setTaskStatus(task.getTaskStatus());
        }
    }

    void updateSubtask(Subtask subtask, int id) {
        Subtask currentSubtask = subtaskList.get(id);
        if (currentSubtask != null) {
            currentSubtask.setTaskName(subtask.getTaskName());
            currentSubtask.setTaskInfo(subtask.getTaskInfo());
            currentSubtask.setTaskStatus(subtask.getTaskStatus());
        }
    }

    void updateEpic(Epic epic, int id) {
        Epic currentEpic = epicList.get(id);
        if (currentEpic != null) {
            currentEpic.setTaskName(epic.getTaskName());
            currentEpic.setTaskInfo(epic.getTaskInfo());
            currentEpic.setTaskStatus(checkEpicStatus(currentEpic));
        }
    }

    String checkEpicStatus(Epic epic) {
        ArrayList<Subtask> subtasks = epic.getListOfSubtask();
        if (subtasks.size()!=0) {
            ArrayList<String> listOfStatuses = new ArrayList<>();
            String currentStatus = "NEW";
            int newCounter = 0;
            int doneCounter = 0;
            for (Subtask subtask : subtasks) {
                listOfStatuses.add(subtask.taskStatus);
            }
            for (String status : listOfStatuses) {
                if (status.equals("DONE")) {
                    doneCounter++;
                } else if (status.equals("NEW")) {
                    newCounter++;
                }
            }
            if (newCounter == 0) {
                currentStatus = "IN_PROGRESS";
            } else if (doneCounter == listOfStatuses.size()) {
                currentStatus = "DONE";
            }
            return currentStatus;
        } else return "NEW";
    }


    ArrayList<Subtask> getAllSubtasksOfEpic(int id) {
        Epic epic = epicList.get(id);
        if (epic != null) {
            return epic.getListOfSubtask();
        }
        return null;
    }
}
