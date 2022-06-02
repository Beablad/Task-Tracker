package managers;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected TaskManager taskManager = new InMemoryTaskManager();
    protected Task task;
    protected Epic epic;
    protected Subtask subtask1;
    protected Subtask subtask2;
    protected Subtask subtask3;


    protected void init() {
        task = new Task("T1", "T1");
        epic = new Epic("E1", "E1");
        subtask1 = new Subtask("S1", "S1");
        subtask2 = new Subtask("S2", "S2");
        subtask3 = new Subtask("S3", "S3");
        taskManager.addTask(task, Statuses.NEW, LocalDateTime.of(2022, 1, 1, 0, 0), 60);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1, epic, Statuses.NEW, LocalDateTime.of(2022, 1, 1, 1, 0),
                60);
        taskManager.addSubtask(subtask2, epic, Statuses.NEW, LocalDateTime.of(2022, 1, 1, 4, 0),
                60);
        taskManager.addSubtask(subtask3, epic, Statuses.NEW, LocalDateTime.of(2022, 1, 1, 3, 0),
                60);
    }

    @Test
    void addTask() {
        assertEquals(task, taskManager.returnTasks().get(1));
    }

    @Test
    void addEpic() {
        assertEquals(epic, taskManager.returnEpic().get(2));
    }

    @Test
    void addSubtask() {
        assertEquals(subtask1, taskManager.returnSubtasks().get(3));
    }

    @Test
    void returnTasks() {
        HashMap<Integer, Task> tasks = taskManager.returnTasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(1));
    }

    @Test
    void returnEpics() {
        HashMap<Integer, Epic> epics = taskManager.returnEpic();
        assertNotNull(epics);
        assertEquals(1, epics.size());
        assertEquals(epic, epics.get(2));
    }

    @Test
    void returnSubtasks() {
        HashMap<Integer, Subtask> subtasks = taskManager.returnSubtasks();
        assertNotNull(subtasks);
        assertEquals(3, subtasks.size());
        assertEquals(subtask1, subtasks.get(3));
        assertEquals(subtask2, subtasks.get(4));
        assertEquals(subtask3, subtasks.get(5));
    }

    @Test
    void getTaskById() {
        assertEquals(task, taskManager.getTaskById(1));
        assertNotEquals(task, taskManager.getTaskById(2));
    }

    @Test
    void getEpicById() {
        assertEquals(epic, taskManager.getEpicById(2));
        assertNotEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    void getSubtaskById() {
        assertEquals(subtask1, taskManager.getSubtaskById(3));
        assertEquals(subtask2, taskManager.getSubtaskById(4));
        assertEquals(subtask3, taskManager.getSubtaskById(5));
        assertNotEquals(subtask1, taskManager.getSubtaskById(2));
        assertNotEquals(subtask2, taskManager.getSubtaskById(2));
        assertNotEquals(subtask3, taskManager.getSubtaskById(2));
    }

    @Test
    void clearTaskList() {
        taskManager.clearTaskList();
        assertEquals(0, taskManager.returnTasks().size());
    }

    @Test
    void clearEpicList() {
        taskManager.clearEpicList();
        assertEquals(0, taskManager.returnEpic().size());
        assertEquals(0, taskManager.returnSubtasks().size());
    }

    @Test
    void clearSubtaskList() {
        taskManager.clearSubtaskList();
        assertEquals(0, taskManager.returnSubtasks().size());
    }

    @Test
    void removeTaskById() {
        taskManager.removeTaskById(1);
        assertEquals(0, taskManager.returnTasks().size());
        assertNotEquals(task, taskManager.returnTasks().get(1));
    }

    @Test
    void removeEpicById() {
        taskManager.removeEpicById(2);
        System.out.println(taskManager.returnSubtasks());

        assertEquals(0, taskManager.returnEpic().size());
        assertNotEquals(task, taskManager.returnEpic().get(2));
    }

    @Test
    void removeSubtaskById() {
        taskManager.removeSubtaskById(3);
        assertEquals(2, taskManager.returnSubtasks().size());
        assertNotEquals(subtask1, taskManager.returnSubtasks().get(3));
    }

    @Test
    void updateTask() {
        taskManager.updateTask(task, Statuses.DONE, null, 0);
        assertEquals(task, taskManager.returnTasks().get(1));
    }

    @Test
    void updateSubtask() {
        taskManager.updateSubtask(subtask1, Statuses.IN_PROGRESS,
                LocalDateTime.of(2022, 1, 1, 10, 0), 60);
        assertEquals(subtask1, taskManager.returnSubtasks().get(3));
    }

    @Test
    void updateEpic() {
        taskManager.updateEpic(epic);
        assertEquals(epic, taskManager.returnEpic().get(2));
    }

    @Test
    void getAllSubtasksOfEpic() {
        List<Subtask> list = taskManager.getAllSubtasksOfEpic(2);
        assertEquals(3, list.size());
    }

    @Test
    void getHistory() {
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(3);
        taskManager.getEpicById(2);
        assertEquals(3, taskManager.getHistory().size());
    }

    @Test
    void removeHistory() {
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(3);
        taskManager.getEpicById(2);
        taskManager.removeHistory(2);
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void getPrioritizedTask() {
        List<Task> prioritizedList = taskManager.getPrioritizedTask();
        List<Task> list = List.of(task, subtask1, subtask3, subtask2);
        assertEquals(list, prioritizedList);
    }
}