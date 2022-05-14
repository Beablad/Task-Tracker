import managers.*;
import tasks.Epic;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {


        /*TaskManager inMemoryTaskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistoryManager();
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
        inMemoryTaskManager.removeHistory(1);
        System.out.println("История после удаления задачи: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.returnSubtaskById(5);
        inMemoryTaskManager.returnSubtaskById(4);
        inMemoryTaskManager.returnEpicById(3);
        System.out.println("История после добавления эпика с подзадачами: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.removeHistory(3);
        System.out.println("История после удаления эпика: " + inMemoryTaskManager.getHistory());*/

    }
}