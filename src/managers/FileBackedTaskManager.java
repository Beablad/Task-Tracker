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
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private CSVSerializator csv = new CSVSerializator();


    private void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("tracker.csv"))) {
            bufferedWriter.append("id,type,name,status,description,epic \n");
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
            if (!string.contains("id,type,name,status,description,epic")) {
                String[] array = string.split(",");
                if (array[1].equals("TASK")) {
                    Task task = new Task(array[2], array[4]);
                    task.setTaskId(Integer.parseInt(array[0]));
                    task.setTaskStatus(statuses(array[3]));
                    task.setTaskInfo(array[4]);
                    taskList.put(Integer.parseInt(array[0]), task);
                } else if (array[1].equals("EPIC")) {
                    Epic epic = new Epic(array[2], array[4]);
                    epic.setTaskId(Integer.parseInt(array[0]));
                    epic.setTaskStatus(statuses(array[3]));
                    epic.setTaskInfo(array[4]);
                    for (Subtask subtask : subtaskList.values()) {
                        if (subtask.getIdEpic() == Integer.parseInt(array[0])) {
                            epic.addSubtaskInEpic(subtask);
                        }
                    }
                    epicList.put(Integer.parseInt(array[0]), epic);
                } else {
                    Subtask subtask = new Subtask(array[2], array[4]);
                    subtask.setTaskId(Integer.parseInt(array[0]));
                    subtask.setTaskStatus(statuses(array[3]));
                    subtask.setTaskInfo(array[4]);
                    subtask.setIdEpic(Integer.parseInt(array[5]));
                    subtaskList.put(Integer.parseInt(array[0]), subtask);
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
    public void putTask(Task task, Statuses status) {
        super.putTask(task, status);
        save();
    }

    @Override
    public void putEpic(Epic epic) {
        super.putEpic(epic);
        save();
    }

    @Override
    public void putSubtask(Subtask subtask, Epic epic, Statuses status) {
        super.putSubtask(subtask, epic, status);
        save();
    }

    @Override
    public void updateTask(Task task, Statuses status) {
        super.updateTask(task, status);
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
        TaskManager inMemoryTaskManager = loadFromFile();
        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        System.out.println(inMemoryTaskManager.getAllSubtasksOfEpic(3));
        /*System.out.println(inMemoryTaskManager.getHistory());
        Task task = new Task("Task1", "info");// id=1
        Task task2 = new Task("Task2", "info");// id=2
        Epic epic = new Epic("Epic", "info");// id=3
        Subtask subtask = new Subtask("Subtask", "info");// id=4
        Subtask subtask2 = new Subtask("Subtask2", "info");// id=5
        Subtask subtask3 = new Subtask("Subtask3", "info");// id=6
        Epic epic1 = new Epic("Epic1", "info");// id=7
        inMemoryTaskManager.putTask(task, Statuses.NEW);
        inMemoryTaskManager.putTask(task2, Statuses.NEW);
        inMemoryTaskManager.putEpic(epic);
        inMemoryTaskManager.putSubtask(subtask, epic, Statuses.NEW);
        inMemoryTaskManager.putSubtask(subtask2, epic, Statuses.NEW);
        inMemoryTaskManager.putSubtask(subtask3, epic, Statuses.NEW);
        inMemoryTaskManager.putEpic(epic1);
        inMemoryTaskManager.returnTaskById(2);
        inMemoryTaskManager.returnTaskById(1);
        inMemoryTaskManager.returnTaskById(2);
        inMemoryTaskManager.returnTaskById(1);
        System.out.println("История после добавления дубликатов: " + inMemoryTaskManager.getHistory());
        //inMemoryTaskManager.removeHistory(1);
        System.out.println("История после удаления задачи: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.returnSubtaskById(5);
        inMemoryTaskManager.returnSubtaskById(4);
        inMemoryTaskManager.returnEpicById(3);
        System.out.println("История после добавления эпика с подзадачами: " + inMemoryTaskManager.getHistory());
        //inMemoryTaskManager.removeHistory(3);
        System.out.println("История после удаления эпика: " + inMemoryTaskManager.getHistory());*/
    }
}
