package managers;

import tasks.Epic;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;
import utility.IntersectionException;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> taskMap;
    protected HashMap<Integer, Epic> epicMap;
    protected HashMap<Integer, Subtask> subtaskMap;
    private static int taskId = 1;
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
        this.taskMap = new HashMap<>();
        this.epicMap = new HashMap<>();
        this.subtaskMap = new HashMap<>();
        taskId = 1;
    }

    public int getId() {
        return taskId++;
    }

    @Override
    public void addTask(Task task) throws IntersectionException {
        if (checkIntersections(task)) {
            task.setTaskId(getId());
            taskMap.put(task.getTaskId(), task);
            putInPrioritizedTask(task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setTaskId(getId());
        checkEpicStatus(epic);
        epicMap.put(epic.getTaskId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (checkIntersections(subtask)) {
            subtask.setTaskId(getId());
            Epic epic = epicMap.get(subtask.getIdEpic());
            epic.addSubtaskInEpic(subtask);
            epic.setStartTime();
            epic.setDuration();
            subtaskMap.put(subtask.getTaskId(), subtask);
            checkEpicStatus(epic);
            putInPrioritizedTask(subtask);
        }
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public void clearTaskList() {
        taskMap.clear();
    }

    @Override
    public void clearEpicList() {
        subtaskMap.clear();
        epicMap.clear();
    }

    @Override
    public void clearSubtaskList() {
        subtaskMap.clear();
    }

    @Override
    public void removeTaskById(int id) {
        taskMap.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epicMap.get(id);
        ArrayList<Subtask> subtasks = epic.getListOfSubtask();
        for (Subtask subtask : subtasks) {
            int subId = subtask.getTaskId();
            subtaskMap.remove(subId);
        }
        epicMap.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtaskMap.get(id);
        Epic epic = epicMap.get(subtask.getIdEpic());
        epic.removeSubtaskInEpic(subtask);
        subtaskMap.remove(id);
    }

    @Override
    public void updateTask(Task task) {
        Task currentTask = taskMap.get(task.getTaskId());
        if (currentTask != null && checkIntersections(task)) {
            currentTask.setTaskName(task.getTaskName());
            currentTask.setTaskInfo(task.getTaskInfo());
            currentTask.setTaskStatus(task.getTaskStatus());
            currentTask.setStartTime(task.getStartTime());
            currentTask.setDuration(task.getDuration());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask currentSubtask = subtaskMap.get(subtask.getTaskId());
        if (currentSubtask != null && checkIntersections(subtask)) {
            currentSubtask.setTaskName(subtask.getTaskName());
            currentSubtask.setTaskInfo(subtask.getTaskInfo());
            currentSubtask.setTaskStatus(subtask.getTaskStatus());
            currentSubtask.setStartTime(subtask.getStartTime());
            currentSubtask.setDuration(subtask.getDuration());
            Epic epic = epicMap.get(subtask.getIdEpic());
            epic.setStartTime();
            epic.setDuration();
            checkEpicStatus(epic);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic currentEpic = epicMap.get(epic.getTaskId());
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
                if (subtask.getTaskStatus()!=null){
                    listOfStatuses.add(subtask.getTaskStatus());
                }
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
        Epic epic = epicMap.get(id);
        if (epic != null) {
            return epic.getListOfSubtask();
        }
        return null;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = taskMap.get(id);
        if (task != null) {
            inMemoryHistoryManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicMap.get(id);
        if (epic != null) {
            inMemoryHistoryManager.add(epic);
            return epic;
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtaskMap.get(id);
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

    private boolean checkIntersections(Task task) throws IntersectionException {
        if (task.getStartTime() != null) {
            LocalDateTime startTime = task.getStartTime();
            LocalDateTime end = startTime.plusMinutes(task.getDuration());
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

    @Override
    public HashMap<Integer, Task> taskMap() {
        return taskMap;
    }

    @Override
    public HashMap<Integer, Epic> epicMap() {
        return epicMap;
    }

    @Override
    public HashMap<Integer, Subtask> subtaskMap() {
        return subtaskMap;
    }
}
