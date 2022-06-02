package managers;

import tasks.Epic;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {


    void addTask(Task task, Statuses status, LocalDateTime startTime, long duration);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask, Epic epic, Statuses status, LocalDateTime startTime, long duration);

    HashMap returnTasks();

    HashMap returnEpic();

    HashMap returnSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void clearTaskList();

    void clearEpicList();

    void clearSubtaskList();

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    void updateTask(Task task, Statuses status, LocalDateTime startTime, long duration);

    void updateSubtask(Subtask subtask, Statuses status, LocalDateTime startTime, long duration);

    void updateEpic(Epic epic);

    ArrayList<Subtask> getAllSubtasksOfEpic(int id);

    ArrayList<Task> getHistory();

    void removeHistory(int id);

    List<Task> getPrioritizedTask();
}
