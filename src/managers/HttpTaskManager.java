package managers;

import api.KVTaskClient;
import com.google.gson.Gson;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.net.URI;

public class HttpTaskManager extends FileBackedTaskManager {

    Gson gson = Managers.getDefaultGson();
    URI url = URI.create("http://localhost:8078/");
    KVTaskClient kvTaskClient = new KVTaskClient(url);

    public void recoveryData(String key) {
        kvTaskClient.load(key);
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
