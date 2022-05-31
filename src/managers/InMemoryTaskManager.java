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
    private int taskId;
    HistoryManager inMemoryHistoryManager = Managers.getDefaultHistoryManager();
    TreeSet<Task> prioritizedTasks = new TreeSet<>(new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            if (o1.getStartTime() == (null)) {
                return 1;
            } else if (o2.getStartTime() == null) {
                return -1;
            } else if (o1.getStartTime().isAfter(o2.getStartTime())) {
                return 1;
            } else if (o1.getStartTime().isBefore(o2.getStartTime())) {
                return -1;
            } else return 0;
        }
    });

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
    public void putTask(Task task, Statuses status, LocalDateTime startTime, long duration) {
        try {
            if(checkIntersections(startTime, duration)){
                task.setTaskStatus(status);
                task.setTaskId(getId());
                task.setStartTime(startTime);
                task.setDuration(duration);
                taskList.put(task.getTaskId(), task);
                putInPrioritizedTask(task);
            }
        } catch (IntersectionException e){
            e.getMessage();
        }
    }

    @Override
    public void putEpic(Epic epic) {
        checkEpicStatus(epic);
        epic.setTaskId(getId());
        epicList.put(epic.getTaskId(), epic);
    }

    @Override
    public void putSubtask(Subtask subtask, Epic epic, Statuses status, LocalDateTime startTime, long duration) {
        try {
            if (checkIntersections(startTime, duration)){
                subtask.setTaskStatus(status);
                subtask.setTaskId(getId());
                subtask.setIdEpic(epic.getTaskId());
                subtask.setStartTime(startTime);
                subtask.setDuration(duration);
                epic.addSubtaskInEpic(subtask);
                epic.setStartTime();
                epic.setDuration();
                subtaskList.put(subtask.getTaskId(), subtask);
                checkEpicStatus(epic);
                putInPrioritizedTask(subtask);
            }
        } catch (IntersectionException e){
            System.out.println(e.getMessage());
        }
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
    public void updateTask(Task task, Statuses status, LocalDateTime startTime, long duration) {
        Task currentTask = taskList.get(task.getTaskId());
        if (currentTask != null) {
            currentTask.setTaskName(task.getTaskName());
            currentTask.setTaskInfo(task.getTaskInfo());
            currentTask.setTaskStatus(status);
            currentTask.setStartTime(startTime);
            currentTask.setDuration(duration);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask, Statuses status, LocalDateTime startTime, long duration) {
        Subtask currentSubtask = subtaskList.get(subtask.getTaskId());
        if (currentSubtask != null) {
            currentSubtask.setTaskName(subtask.getTaskName());
            currentSubtask.setTaskInfo(subtask.getTaskInfo());
            currentSubtask.setTaskStatus(status);
            currentSubtask.setStartTime(startTime);
            currentSubtask.setDuration(duration);
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
        if (task != null) {
            inMemoryHistoryManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public Epic returnEpicById(int id) {
        Epic epic = epicList.get(id);
        if (epic != null) {
            inMemoryHistoryManager.add(epic);
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

    private void putInPrioritizedTask(Task task) {
        prioritizedTasks.add(task);
    }

    @Override
    public List<Task> getPrioritizedTask() {
        List<Task> list = new ArrayList<>();
        for (Task task : prioritizedTasks) {
            list.add(task);

        }
        return list;
    }

    private boolean checkIntersections(LocalDateTime startTime, long duration) {
        if (startTime != null) {
            LocalDateTime start = startTime;
            LocalDateTime end = startTime.plusMinutes(duration);
            for (Task checkTask : prioritizedTasks) {
                if (checkTask.getStartTime() != null) {
                    LocalDateTime startCheck = checkTask.getStartTime();
                    LocalDateTime endCheck = checkTask.getEndTime();
                    if (start.isAfter(startCheck) && end.isBefore(endCheck)) {
                        throw new IntersectionException("Данное время уже занято");
                    } else if (end.isAfter(startCheck)&&end.isBefore(endCheck)){
                        throw new IntersectionException("Данное время уже занято");
                     } else if (start.isAfter(startCheck)&&start.isBefore(endCheck)){
                        throw new IntersectionException("Данное время уже занято");
                    } else if (startCheck.isAfter(start)&&endCheck.isBefore(end)){
                        throw new IntersectionException("Данное время уже занято");
                    } else if (start.isEqual(startCheck)&&end.isEqual(endCheck)){
                        throw new IntersectionException("Данное время уже занято");
                    }
                } return true;
            }
        } return true;
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
