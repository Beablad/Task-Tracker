package managers;

import api.KVTaskClient;
import com.google.gson.Gson;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTaskManager {

    Gson gson = Managers.getDefaultGson();
    KVTaskClient kvTaskClient;
    URI url = URI.create("http://localhost:8078/");

    public HttpTaskManager() {
        super();
        this.kvTaskClient = new KVTaskClient(url);
        recoveryData();
    }

    public void recoveryData() {
        List tasks = gson.fromJson(kvTaskClient.load("tasks/"), ArrayList.class);
        System.out.println(tasks);
        if (tasks!=null){
            for (Object task : tasks) {
                addTask(gson.fromJson(task.toString(), Task.class));
            }
        }
        List epics = gson.fromJson(kvTaskClient.load("epics/"), ArrayList.class);
        if (epics!=null){
            for (Object epic : epics) {
                addEpic(gson.fromJson(epic.toString(), Epic.class));
            }
        }
        List subtasks = gson.fromJson(kvTaskClient.load("subtasks/"), ArrayList.class);
        if (subtasks!=null){
            for (Object subtask : subtasks) {
                addSubtask(gson.fromJson(subtask.toString(), Subtask.class));
            }
        }
        String history = gson.fromJson(kvTaskClient.load("history/"), String.class);
        if (history!=null){
            String[] historyArray = history.split(",");
            for (String s: historyArray){
                int id = Integer.parseInt(s);
                getTaskById(id);
                getEpicById(id);
                getSubtaskById(id);
            }
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        kvTaskClient.put("tasks", gson.toJson(getTasks()));
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        kvTaskClient.put("epics", gson.toJson(getEpics()));
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        kvTaskClient.put("subtasks", gson.toJson(getSubtasks()));
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        kvTaskClient.put("tasks", gson.toJson(getTasks()));
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        kvTaskClient.put("epics", gson.toJson(getEpics()));
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        kvTaskClient.put("subtasks", gson.toJson(getSubtasks()));
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        kvTaskClient.put("history", gson.toJson(getHistory()));
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        kvTaskClient.put("history", gson.toJson(getHistory()));
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        kvTaskClient.put("history", gson.toJson(getHistory()));
        return subtask;
    }
}
