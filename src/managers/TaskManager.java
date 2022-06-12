package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {


    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void clearTaskList();

    void clearEpicList();

    void clearSubtaskList();

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    ArrayList<Subtask> getAllSubtasksOfEpic(int id);

    ArrayList<Task> getHistory();

    void removeHistory(int id);

    List<Task> getPrioritizedTask();
}
