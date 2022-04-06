package managers;

import tasks.Epic;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    public void putTask(Task task, Statuses status);

    public void putEpic(Epic epic);

    public void putSubtask(Subtask subtask, Epic epic, Statuses status);

    public HashMap returnTaskInfo();

    public HashMap returnEpicInfo();

    public HashMap returnSubtaskInfo();

    public Task returnTaskById(int id);

    public Epic returnEpicById(int id);

    public Subtask returnSubtaskById(int id);

    public void clearTaskList();

    public void clearEpicList();

    public void clearSubtaskList();

    public void removeTaskById(int id);

    public void removeEpicById(int id);

    public void removeSubtaskById(int id);

    public void updateTask(Task task, Statuses status);

    public void updateSubtask(Subtask subtask);

    public void updateEpic(Epic epic);

    public ArrayList<Subtask> getAllSubtasksOfEpic(int id);

    public ArrayList<Task> getHistory();
}
