package managers;

import tasks.Epic;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;
import utility.CSVSerializator;
import utility.ManagerSaveException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private CSVSerializator csv = new CSVSerializator();


    private void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("tracker.csv"))) {
            bufferedWriter.append("id,type,name,status,description,epic,startTime,duration \n");
            for (Task task : taskList.values()) {
                bufferedWriter.append(csv.toString(task) + "\n");
            }
            for (Subtask subtask : subtaskList.values()) {
                bufferedWriter.append(csv.toString(subtask) + "\n");
            }
            for (Epic epic : epicList.values()) {
                bufferedWriter.append(csv.toString(epic) + "\n");
            }
            bufferedWriter.append("\n" + CSVSerializator.historyToString(inMemoryHistoryManager));
        } catch (IOException e) {
            throw new ManagerSaveException("В указанной директории нет файла");
        }
    }

    public static FileBackedTaskManager loadFromFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        fileBackedTaskManager.load();
        return fileBackedTaskManager;
    }

    private void load() {
        List<String> list = csv.fromString();
        for (String string : list) {
            if (!string.contains("id,type,name,status,description,epic,startTime,duration")) {
                String[] array = string.split(",");
                if (array[1].equals("TASK")) {
                    Task task = new Task(array[2], array[4], statuses(array[3]), LocalDateTime.parse(array[6]),
                            Long.parseLong(array[7]));
                    task.setTaskId(Integer.parseInt(array[0]));
                    taskList.put(task.getTaskId(), task);
                } else if (array[1].equals("SUBTASK")) {
                    Subtask subtask = new Subtask(array[2], array[4], statuses(array[3]), LocalDateTime.parse(array[6]),
                            Long.parseLong(array[7]), Integer.parseInt(array[5]));
                    subtask.setTaskId(Integer.parseInt(array[0]));
                    subtaskList.put(subtask.getTaskId(), subtask);
                } else {
                    Epic epic = new Epic(array[2], array[4]);
                    epic.setTaskId(Integer.parseInt(array[0]));
                    epic.setTaskStatus(statuses(array[3]));
                    epic.setStartTime();
                    epic.setDuration();
                    for (Subtask subtask : subtaskList.values()) {
                        if (subtask.getTaskId() == Integer.parseInt(array[0])) {
                            epic.addSubtaskInEpic(subtask);
                        }
                    }
                    epicList.put(Integer.parseInt(array[0]), epic);
                }
            }
        }
        List<String> historyList = CSVSerializator.historyFromString();
        if (!historyList.isEmpty()) {
            for (String str : historyList) {
                int id = Integer.parseInt(str);
                if (taskList.containsKey(id)) {
                    inMemoryHistoryManager.add(taskList.get(id));
                } else if (epicList.containsKey(id)) {
                    inMemoryHistoryManager.add(epicList.get(id));
                } else if (subtaskList.containsKey(id)) {
                    inMemoryHistoryManager.add(subtaskList.get(id));
                }
            }
        }
    }

    @Override
    public void clearTaskList() {
        super.clearTaskList();
        save();
    }

    @Override
    public void clearEpicList() {
        super.clearEpicList();
        save();
    }

    @Override
    public void clearSubtaskList() {
        super.clearSubtaskList();
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeHistory(int id) {
        super.removeHistory(id);
        save();
    }

    private static Statuses statuses(String status) {
        if (status.equals("NEW")) {
            return Statuses.NEW;
        } else if (status.equals("IN_PROGRESS")) {
            return Statuses.IN_PROGRESS;
        } else {
            return Statuses.DONE;
        }
    }


    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    public static void main(String[] args) {
        FileBackedTaskManager fb = new FileBackedTaskManager();
        Task task = new Task("a", "b", Statuses.NEW, null, 0);
        Task task1 = new Task("a", "b", Statuses.NEW, null, 0);
        fb.addTask(task);
        fb.addTask(task1);
        Epic epic = new Epic("Epic", "Epic");
        fb.addEpic(epic);
        Subtask subtask1 = new Subtask("1", "1", Statuses.NEW, null, 0, epic.getTaskId());
        Subtask subtask2 = new Subtask("2", "2", Statuses.NEW, null, 0, epic.getTaskId());
        Subtask subtask3 = new Subtask("3", "3", Statuses.NEW, null, 0, epic.getTaskId());
        fb.addSubtask(subtask1);
        fb.addSubtask(subtask2);
        fb.addSubtask(subtask3);
        System.out.println(fb.getEpics());
        System.out.println(fb.getEpicById(2));
        System.out.println(fb.getAllSubtasksOfEpic(2));
        System.out.println(fb.getPrioritizedTask());
    }
}
