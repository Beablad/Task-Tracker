import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> taskList;
    private HashMap<Integer, Epic> epicList;
    private HashMap<Integer, Subtask> subtaskList;
    private int taskId;

    public Manager() {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.subtaskList = new HashMap<>();
        this.taskId = 1;
    }


    public int getId() {
        return taskId++;
    }

    public void putTask(Task task, String status) {
        task.setTaskStatus(status);
        task.setTaskId(getId());
        taskList.put(task.getTaskId(), task);
    }

    public void putEpic(Epic epic) {
        checkEpicStatus(epic);
        epic.setTaskId(getId());
        epicList.put(epic.getTaskId(), epic);
    }

    public void putSubtask(Subtask subtask, Epic epic, String status) {
        subtask.setTaskStatus(status);
        subtask.setTaskId(getId());
        subtask.setIdEpic(epic.getTaskId());
        epic.addSubtaskInEpic(subtask);
        subtaskList.put(subtask.getTaskId(), subtask);
        checkEpicStatus(epic);
    }

    public HashMap returnTaskInfo() {
        return taskList;
    }

    public HashMap returnEpicInfo() {
        return epicList;
    }

    public HashMap returnSubtaskInfo() {
        return subtaskList;
    }

    public Task returnTaskById(int id) {
        Task task = taskList.get(id);
        if (task != null) {
            return task;
        }
        return null;
    }

    public Epic returnEpicById(int id) {
        Epic epic = epicList.get(id);
        if (epic != null) {
            return epic;
        }
        return null;
    }

    public Subtask returnSubtaskById(int id) {
        Subtask subtask = subtaskList.get(id);
        if (subtask != null) {
            return subtask;
        }
        return null;
    }

    public void clearTaskList() {
        taskList.clear();
    }

    public void clearEpicList() {
        subtaskList.clear();
        epicList.clear();
    }

    public void clearSubtaskList() {
        subtaskList.clear();
    }

    public void removeTaskById(int id) {
        taskList.remove(id);
    }

    public void removeEpicById(int id) {
        Epic epic = epicList.get(id);
        ArrayList<Subtask> subtasks = epic.getListOfSubtask();
        for (Subtask subtask : subtasks) {
            int subId = subtask.getTaskId();
            subtaskList.remove(subId);
        }
        epicList.remove(id);
    }

    public void removeSubtaskById(int id) {
        Subtask subtask = subtaskList.get(id);
        Epic epic = epicList.get(subtask.getIdEpic());
        epic.removeSubtaskInEpic(subtask);
        subtaskList.remove(id);
    }

    public void updateTask(Task task, String status) {
        Task currentTask = taskList.get(task.getTaskId());
        if (currentTask != null) {
            currentTask.setTaskName(task.getTaskName());
            currentTask.setTaskInfo(task.getTaskInfo());
            currentTask.setTaskStatus(status);
        }
    }

    public void updateSubtask(Subtask subtask) {
        Subtask currentSubtask = subtaskList.get(subtask.getTaskId());
        if (currentSubtask != null) {
            currentSubtask.setTaskName(subtask.getTaskName());
            currentSubtask.setTaskInfo(subtask.getTaskInfo());
            currentSubtask.setTaskStatus(subtask.getTaskStatus());
            Epic epic = epicList.get(subtask.getIdEpic());
            checkEpicStatus(epic);
        }
    }

    public void updateEpic(Epic epic) {
        Epic currentEpic = epicList.get(epic.getTaskId());
        if (currentEpic != null) {
            currentEpic.setTaskName(epic.getTaskName());
            currentEpic.setTaskInfo(epic.getTaskInfo());
        }
    }

    public void checkEpicStatus(Epic epic) {
        ArrayList<Subtask> subtasks = epic.getListOfSubtask();
        if (subtasks.size() != 0) {
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
            if (doneCounter == listOfStatuses.size()) {
                currentStatus = "DONE";
            } else if (newCounter == 0) {
                currentStatus = "IN_PROGRESS";
            }
            epic.setTaskStatus(currentStatus);
        } else epic.setTaskStatus("NEW");
    }


    public ArrayList<Subtask> getAllSubtasksOfEpic(int id) {
        Epic epic = epicList.get(id);
        if (epic != null) {
            return epic.getListOfSubtask();
        }
        return null;
    }
}
