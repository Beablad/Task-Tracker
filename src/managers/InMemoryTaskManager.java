package managers;

import tasks.Epic;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> taskList;
    private HashMap<Integer, Epic> epicList;
    private HashMap<Integer, Subtask> subtaskList;
    private int taskId;
    HistoryManager inMemoryHistoryManager = Managers.getDefaultHistoryManager();
    int[] a = new int[2];

    public InMemoryTaskManager() {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.subtaskList = new HashMap<>();
        this.taskId = 1;
    }

    private int getId() {
        return taskId++;
    }

    @Override
    public void putTask(Task task, Statuses status) {
        task.setTaskStatus(status);
        task.setTaskId(getId());
        taskList.put(task.getTaskId(), task);
        Arrays.binarySearch(a, 0);
    }

    @Override
    public void putEpic(Epic epic) {
        checkEpicStatus(epic);
        epic.setTaskId(getId());
        epicList.put(epic.getTaskId(), epic);
    }

    @Override
    public void putSubtask(Subtask subtask, Epic epic, Statuses status) {
        subtask.setTaskStatus(status);
        subtask.setTaskId(getId());
        subtask.setIdEpic(epic.getTaskId());
        epic.addSubtaskInEpic(subtask);
        subtaskList.put(subtask.getTaskId(), subtask);
        checkEpicStatus(epic);
    }

    @Override
    public HashMap returnTaskInfo() {
        return taskList;
    }

    @Override
    public HashMap returnEpicInfo() {
        return epicList;
    }

    @Override
    public HashMap returnSubtaskInfo() {
        return subtaskList;
    }

    @Override
    public void clearTaskList() {
        taskList.clear();
    }

    @Override
    public void clearEpicList() {
        subtaskList.clear();
        epicList.clear();
    }

    @Override
    public void clearSubtaskList() {
        subtaskList.clear();
    }

    @Override
    public void removeTaskById(int id) {
        taskList.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epicList.get(id);
        ArrayList<Subtask> subtasks = epic.getListOfSubtask();
        for (Subtask subtask : subtasks) {
            int subId = subtask.getTaskId();
            subtaskList.remove(subId);
        }
        epicList.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtaskList.get(id);
        Epic epic = epicList.get(subtask.getIdEpic());
        epic.removeSubtaskInEpic(subtask);
        subtaskList.remove(id);
    }

    @Override
    public void updateTask(Task task, Statuses status) {
        Task currentTask = taskList.get(task.getTaskId());
        if (currentTask != null) {
            currentTask.setTaskName(task.getTaskName());
            currentTask.setTaskInfo(task.getTaskInfo());
            currentTask.setTaskStatus(status);
        }
    }

    @Override
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

    @Override
    public void updateEpic(Epic epic) {
        Epic currentEpic = epicList.get(epic.getTaskId());
        if (currentEpic != null) {
            currentEpic.setTaskName(epic.getTaskName());
            currentEpic.setTaskInfo(epic.getTaskInfo());
        }
    }

    private void checkEpicStatus(Epic epic) {
        ArrayList<Subtask> subtasks = epic.getListOfSubtask();
        if (subtasks.size() != 0) {
            ArrayList<Statuses> listOfStatuses = new ArrayList<>();
            Statuses currentStatus = Statuses.NEW;
            int newCounter = 0;
            int doneCounter = 0;
            for (Subtask subtask : subtasks) {
                listOfStatuses.add(subtask.getTaskStatus());
            }
            for (Statuses status : listOfStatuses) {
                if (status.equals(Statuses.DONE)) {
                    doneCounter++;
                } else if (status.equals(Statuses.NEW)) {
                    newCounter++;
                }
            }
            if (doneCounter == listOfStatuses.size()) {
                currentStatus = Statuses.DONE;
            } else if (newCounter == 0) {
                currentStatus = Statuses.IN_PROGRESS;
            }
            epic.setTaskStatus(currentStatus);
        } else epic.setTaskStatus(Statuses.NEW);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksOfEpic(int id) {
        Epic epic = epicList.get(id);
        if (epic != null) {
            return epic.getListOfSubtask();
        }
        return null;
    }

    @Override
    public Task returnTaskById(int id) {
        Task task = taskList.get(id);
        inMemoryHistoryManager.add(task);
        if (task != null) {
            return task;
        }
        return null;
    }

    @Override
    public Epic returnEpicById(int id) {
        Epic epic = epicList.get(id);
        inMemoryHistoryManager.add(epic);
        if (epic != null) {
            return epic;
        }
        return null;
    }

    @Override
    public Subtask returnSubtaskById(int id) {
        Subtask subtask = subtaskList.get(id);
        inMemoryHistoryManager.add(subtask);
        if (subtask != null) {
            return subtask;
        }
        return null;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public void removeHistory(int id){
        inMemoryHistoryManager.remove(id);
    }
}
