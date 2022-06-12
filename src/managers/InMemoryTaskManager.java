package managers;

import tasks.Epic;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;
import utility.IntersectionException;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> taskList;
    protected HashMap<Integer, Epic> epicList;
    protected HashMap<Integer, Subtask> subtaskList;
    private static int taskId;
    HistoryManager inMemoryHistoryManager = Managers.getDefaultHistoryManager();
    TreeSet<Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
        if (o1.getStartTime() == (null)) {
            return 1;
        } else if (o2.getStartTime() == null) {
            return -1;
        } else if (o1.getStartTime().isAfter(o2.getStartTime())) {
            return 1;
        } else if (o1.getStartTime().isBefore(o2.getStartTime())) {
            return -1;
        } else return 0;
    });

    public InMemoryTaskManager() {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.subtaskList = new HashMap<>();
        taskId = 1;
    }

    public static int getId() {
        return taskId++;
    }

    @Override
    public void addTask(Task task) throws IntersectionException {
        if (checkIntersections(task.getStartTime(), task.getDuration())) {
            taskList.put(task.getTaskId(), task);
            putInPrioritizedTask(task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        checkEpicStatus(epic);
        epicList.put(epic.getTaskId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (checkIntersections(subtask.getStartTime(), subtask.getDuration())) {
            Epic epic = epicList.get(subtask.getIdEpic());
            epic.addSubtaskInEpic(subtask);
            epic.setStartTime();
            epic.setDuration();
            subtaskList.put(subtask.getTaskId(), subtask);
            checkEpicStatus(epic);
            putInPrioritizedTask(subtask);
        }
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(taskList.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epicList.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskList.values());
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
    public void updateTask(Task task) {
        Task currentTask = taskList.get(task.getTaskId());
        if (currentTask != null && checkIntersections(task.getStartTime(), task.getDuration())) {
            currentTask.setTaskName(task.getTaskName());
            currentTask.setTaskInfo(task.getTaskInfo());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask currentSubtask = subtaskList.get(subtask.getTaskId());
        if (currentSubtask != null && checkIntersections(subtask.getStartTime(), subtask.getDuration())) {
            currentSubtask.setTaskName(subtask.getTaskName());
            currentSubtask.setTaskInfo(subtask.getTaskInfo());
            Epic epic = epicList.get(subtask.getIdEpic());
            epic.setStartTime();
            epic.getDuration();
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
        if (subtasks != null && subtasks.size() != 0) {
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
    public Task getTaskById(int id) {
        Task task = taskList.get(id);
        if (task != null) {
            inMemoryHistoryManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicList.get(id);
        if (epic != null) {
            inMemoryHistoryManager.add(epic);
            return epic;
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtaskList.get(id);
        if (subtask != null) {
            inMemoryHistoryManager.add(subtask);
            return subtask;
        }
        return null;
    }

    private void putInPrioritizedTask(Task task) {
        prioritizedTasks.add(task);
    }

    @Override
    public List<Task> getPrioritizedTask() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean checkIntersections(LocalDateTime startTime, long duration) throws IntersectionException {
        if (startTime != null) {
            LocalDateTime end = startTime.plusMinutes(duration);
            for (Task checkTask : prioritizedTasks) {
                if (checkTask.getStartTime() != null) {
                    LocalDateTime startCheck = checkTask.getStartTime();
                    LocalDateTime endCheck = checkTask.getEndTime();
                    if (startTime.isAfter(startCheck) && end.isBefore(endCheck)) {
                        throw new IntersectionException("Данное время уже занято");
                    } else if (end.isAfter(startCheck) && (end.isBefore(endCheck) || end.isEqual(endCheck))) {
                        throw new IntersectionException("Данное время уже занято");
                    } else if ((startTime.isAfter(startCheck) || startTime.isEqual(startCheck)) && startTime.isBefore(endCheck)) {
                        throw new IntersectionException("Данное время уже занято");
                    } else if (startCheck.isAfter(startTime) && endCheck.isBefore(end)) {
                        throw new IntersectionException("Данное время уже занято");
                    }
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public void removeHistory(int id) {
        inMemoryHistoryManager.remove(id);
    }
}
