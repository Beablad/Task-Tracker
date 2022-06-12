package managers;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;

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
        task = new Task("T1", "T1", Statuses.NEW, null, 0);
        epic = new Epic("E1", "E1");
        subtask1 = new Subtask("S1", "S1", Statuses.NEW, null, 0, epic.getTaskId());
        subtask2 = new Subtask("S2", "S2", Statuses.NEW, null, 0, epic.getTaskId());
        subtask3 = new Subtask("S3", "S3", Statuses.NEW, null, 0, epic.getTaskId());
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
    }

    @Test
    void addTask() {
        assertEquals(task, taskManager.getTasks().get(1));
    }

    @Test
    void addEpic() {
        assertEquals(epic, taskManager.getEpics().get(2));
    }

    @Test
    void addSubtask() {
        assertEquals(subtask1, taskManager.getSubtasks().get(3));
    }

    @Test
    void returnTasks() {
        List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(1));
    }

    @Test
    void returnEpics() {
        List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics);
        assertEquals(1, epics.size());
        assertEquals(epic, epics.get(0));
    }

    @Test
    void returnSubtasks() {
        List<Subtask> subtasks = taskManager.getSubtasks();
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
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void clearEpicList() {
        taskManager.clearEpicList();
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void clearSubtaskList() {
        taskManager.clearSubtaskList();
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void removeTaskById() {
        taskManager.removeTaskById(1);
        assertEquals(0, taskManager.getTasks().size());
        assertNotEquals(task, taskManager.getTasks().get(0));
    }

    @Test
    void removeEpicById() {
        taskManager.removeEpicById(2);
        System.out.println(taskManager.getSubtasks());

        assertEquals(0, taskManager.getEpics().size());
        assertNotEquals(task, taskManager.getEpics().get(2));
    }

    @Test
    void removeSubtaskById() {
        taskManager.removeSubtaskById(3);
        assertEquals(2, taskManager.getSubtasks().size());
        assertNotEquals(subtask1, taskManager.getSubtasks().get(3));
    }

    @Test
    void updateTask() {
        taskManager.updateTask(task);
        assertEquals(task, taskManager.getTasks().get(1));
    }

    @Test
    void updateSubtask() {
        taskManager.updateSubtask(subtask1
        );
        assertEquals(subtask1, taskManager.getSubtasks().get(3));
    }

    @Test
    void updateEpic() {
        taskManager.updateEpic(epic);
        assertEquals(epic, taskManager.getEpics().get(2));
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
        List<Task> list = List.of(task, subtask1, subtask2, subtask3);
        assertEquals(list, prioritizedList);
    }
}