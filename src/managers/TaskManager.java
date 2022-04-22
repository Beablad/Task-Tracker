package managers;

import tasks.Epic;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    void putTask(Task task, Statuses status);

    void putEpic(Epic epic);

    void putSubtask(Subtask subtask, Epic epic, Statuses status);

    HashMap returnTaskInfo();

    HashMap returnEpicInfo();

    HashMap returnSubtaskInfo();

    Task returnTaskById(int id);

    Epic returnEpicById(int id);

    Subtask returnSubtaskById(int id);

    void clearTaskList();

    void clearEpicList();

    void clearSubtaskList();

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    void updateTask(Task task, Statuses status);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    ArrayList<Subtask> getAllSubtasksOfEpic(int id);

    ArrayList<Task> getHistory();

    void removeHistory(int id);
}
