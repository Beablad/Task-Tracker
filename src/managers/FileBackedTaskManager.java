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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Formatter;
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

    private static FileBackedTaskManager loadFromFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        fileBackedTaskManager.load();
        return fileBackedTaskManager;
    }

    private void load() {
        List<String> list = csv.fromString();
        for (String string : list) {
            if (!string.contains("id,type,name,status,description,epic, startTime,duration")) {
                String[] array = string.split(",");
                if (array[1].equals("TASK")) {
                    Task task = new Task(array[2], array[4]);
                    task.setTaskId(Integer.parseInt(array[0]));
                    task.setTaskStatus(statuses(array[3]));
                    task.setTaskInfo(array[4]);
                    task.setStartTime(LocalDateTime.parse(array[6]));
                    task.setDuration(Long.parseLong(array[7]));
                    taskList.put(Integer.parseInt(array[0]), task);
                } else if (array[1].equals("SUBTASK")) {
                    Subtask subtask = new Subtask(array[2], array[4]);
                    subtask.setTaskId(Integer.parseInt(array[0]));
                    subtask.setTaskStatus(statuses(array[3]));
                    subtask.setTaskInfo(array[4]);
                    subtask.setIdEpic(Integer.parseInt(array[5]));
                    subtask.setStartTime(LocalDateTime.parse(array[6]));
                    subtask.setDuration(Long.parseLong(array[7]));
                    subtaskList.put(Integer.parseInt(array[0]), subtask);

                } else {
                    Epic epic = new Epic(array[2], array[4]);
                    epic.setTaskId(Integer.parseInt(array[0]));
                    epic.setTaskStatus(statuses(array[3]));
                    epic.setTaskInfo(array[4]);
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
        if (!historyList.isEmpty()){
            for (String str: historyList){
                int id = Integer.parseInt(str);
                if(taskList.containsKey(id)){
                    inMemoryHistoryManager.add(taskList.get(id));
                } else if (epicList.containsKey(id)){
                    inMemoryHistoryManager.add(epicList.get(id));
                } else if (subtaskList.containsKey(id)){
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
    public void putTask(Task task, Statuses status, LocalDateTime startTime, long duration) {
        super.putTask(task, status, startTime, duration);
        save();
    }

    @Override
    public void putEpic(Epic epic) {
        super.putEpic(epic);
        save();
    }

    @Override
    public void putSubtask(Subtask subtask, Epic epic, Statuses status, LocalDateTime startTime, long duration) {
        super.putSubtask(subtask, epic, status, startTime, duration);
        save();
    }

    @Override
    public void updateTask(Task task, Statuses status, LocalDateTime startTime, long duration) {
        super.updateTask(task, status, startTime, duration);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask, Statuses status, LocalDateTime startTime, long duration) {
        super.updateSubtask(subtask, status, startTime, duration);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Task returnTaskById(int id) {
        Task task = super.returnTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic returnEpicById(int id) {
        Epic epic = super.returnEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask returnSubtaskById(int id) {
        Subtask subtask = super.returnSubtaskById(id);
        save();
        return subtask;
    }

    public static void main(String[] args) {
        FileBackedTaskManager fb = new FileBackedTaskManager();
        Task task = new Task("a", "b");
        Task task1 = new Task("a", "b");
        fb.putTask(task, Statuses.IN_PROGRESS, null,
                0);
        fb.putTask(task1, Statuses.IN_PROGRESS, LocalDateTime.of(2022, 5, 30, 19, 30),
                60);
        Epic epic = new Epic("Epic", "Epic");
        fb.putEpic(epic);
        Subtask subtask1 = new Subtask("1", "1");
        Subtask subtask2 = new Subtask("2", "2");
        Subtask subtask3 = new Subtask("3", "3");
        fb.putSubtask(subtask1, epic, Statuses.NEW,
                LocalDateTime.of(2022, 5, 30, 9, 0), 60);
        fb.putSubtask(subtask2, epic, Statuses.DONE,
                LocalDateTime.of(2022, 5, 30, 13, 0), 60);
        fb.putSubtask(subtask3, epic, Statuses.DONE,
                LocalDateTime.of(2022, 5, 30, 11, 0), 60);
        System.out.println(fb.returnEpicInfo());
        System.out.println(fb.returnEpicById(2));
        System.out.println(fb.getAllSubtasksOfEpic(2));
        System.out.println(fb.getPrioritizedTask());
    }
}
