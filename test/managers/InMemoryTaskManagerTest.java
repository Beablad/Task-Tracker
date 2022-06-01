package managers;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {

    InMemoryTaskManager tm = new InMemoryTaskManager();
    Task task = new Task("a", "b");
    Epic epic = new Epic("a", "b");
    Subtask subtask1 = new Subtask("a", "b");
    Subtask subtask2 = new Subtask("a", "b");
    Subtask subtask3 = new Subtask("a", "b");


    @Test
    void putTask() {
        addTasks();
        assertEquals(task, tm.taskList.get(task.getTaskId()));
    }


    @Test
    void putEpic() {
        addEpics();
        assertEquals(epic, tm.epicList.get(epic.getTaskId()));
    }


    @Test
    void putSubtask() {
        addEpics();
        addEpicsAndSubtasks();
        assertEquals(subtask1, tm.subtaskList.get(subtask1.getTaskId()));
        assertEquals(subtask2, tm.subtaskList.get(subtask2.getTaskId()));
        assertEquals(subtask3, tm.subtaskList.get(subtask3.getTaskId()));
    }


    @Test
    void returnTaskInfoWithoutTasks() {
        tm.taskList.clear();
        assertEquals(new HashMap<Integer, Task>(), tm.returnTaskInfo());
    }

    @Test
    void returnTaskInfoWithTasks() {
        assertEquals(tm.taskList, tm.returnTaskInfo());
    }

    @Test
    void returnEpicInfoWithoutEpics() {
        tm.epicList.clear();
        assertEquals(new HashMap<Integer, Epic>(), tm.returnTaskInfo());
    }

    @Test
    void returnEpicInfoWithEpics() {
        assertEquals(tm.epicList, tm.returnEpicInfo());
    }

    @Test
    void returnSubtaskInfoWithoutSubtasks() {
        tm.subtaskList.clear();
        assertEquals(new HashMap<Integer, Subtask>(), tm.returnSubtaskInfo());
    }

    @Test
    void returnSubtaskInfoWithSubtasks() {
        assertEquals(tm.subtaskList, tm.returnSubtaskInfo());
    }

    @Test
    void clearTaskList() {
        tm.clearTaskList();
        assertEquals(new HashMap<Integer, Task>(), tm.taskList);
    }

    @Test
    void clearEpicList() {
        tm.clearEpicList();
        assertEquals(new HashMap<Integer, Epic>(), tm.epicList);
        assertEquals(new HashMap<Integer, Subtask>(), tm.subtaskList);
    }

    @Test
    void clearSubtaskList() {
        tm.clearSubtaskList();
        assertEquals(new HashMap<Integer, Subtask>(), tm.subtaskList);
    }

    @Test
    void updateTask() {
        addTasks();
        tm.updateTask(task, Statuses.DONE, LocalDateTime.of(2022, 1, 1, 0, 0), 60);
        assertEquals(task, tm.taskList.get(1));
    }

    @Test
    void updateSubtask() {
        addEpicsAndSubtasks();
        tm.updateSubtask(subtask1, Statuses.DONE, LocalDateTime.of(2022, 1, 1, 1, 1), 60);
        tm.updateSubtask(subtask2, Statuses.DONE, LocalDateTime.of(2022, 1, 1, 5, 1), 60);
        tm.updateSubtask(subtask3, Statuses.DONE, LocalDateTime.of(2022, 1, 1, 3, 1), 60);
        assertEquals(subtask1, tm.subtaskList.get(2));
        assertEquals(subtask2, tm.subtaskList.get(3));
        assertEquals(subtask3, tm.subtaskList.get(4));
    }

    @Test
    void updateEpic() {
        addEpics();
        tm.updateEpic(epic);
        assertEquals(epic, tm.epicList.get(1));
    }

    @Test
    void getAllSubtasksOfEpic() {
        addEpicsAndSubtasks();
        List<Subtask> list = epic.getListOfSubtask();
        assertEquals(list, tm.getAllSubtasksOfEpic(1));
    }

    @Test
    void returnTaskById() {
        addTasks();
        assertEquals(task, tm.returnTaskById(1));
    }

    @Test
    void returnTaskByIncorrectId() {
        addTasks();
        assertEquals(null, tm.returnTaskById(2));
    }

    @Test
    void returnEpicById() {
        addEpics();
        assertEquals(epic, tm.returnEpicById(1));
    }

    @Test
    void returnEpicByIncorrectId() {
        addEpics();
        assertEquals(null, tm.returnEpicById(2));
    }

    @Test
    void returnSubtaskById() {
        addEpicsAndSubtasks();
        assertEquals(subtask1, tm.returnSubtaskById(2));
        assertEquals(subtask2, tm.returnSubtaskById(3));
        assertEquals(subtask3, tm.returnSubtaskById(4));
    }

    @Test
    void returnSubtaskByIncorrectId() {
        addEpicsAndSubtasks();
        assertEquals(null, tm.returnSubtaskById(6));
        assertEquals(null, tm.returnSubtaskById(7));
        assertEquals(null, tm.returnSubtaskById(8));
    }

    @Test
    void getPrioritizedTask() {
        addTasks();
        addEpicsAndSubtasks();
        List<Task> list = List.of(task, subtask1, subtask3, subtask2);
        assertEquals(list, tm.getPrioritizedTask());
    }

    @Test
    void getHistory() {
        addTasks();
        addEpicsAndSubtasks();
        tm.returnEpicById(2);
        tm.returnSubtaskById(3);
        tm.returnSubtaskById(5);
        tm.returnSubtaskById(4);
        tm.returnTaskById(1);
        List<Task> list = List.of(task, subtask2, subtask3, subtask1, epic);
        assertEquals(list, tm.getHistory());
    }

    @Test
    void removeHistory() {
        addTasks();
        addEpicsAndSubtasks();
        tm.returnEpicById(2);
        tm.returnSubtaskById(3);
        tm.returnSubtaskById(5);
        tm.returnSubtaskById(4);
        tm.returnTaskById(1);
        tm.removeHistory(1);
        List<Task> list = List.of(subtask2, subtask3, subtask1, epic);
        assertEquals(list, tm.getHistory());
    }

    private void addTasks() {
        tm.putTask(task, Statuses.NEW, LocalDateTime.of(2022, 1, 1, 0, 0), 60);
    }

    private void addEpics() {
        tm.putEpic(epic);
    }

    private void addEpicsAndSubtasks (){
        tm.putEpic(epic);
        tm.putSubtask(subtask1, epic, Statuses.NEW, LocalDateTime.of(2022, 1, 1, 1, 0),
                60);
        tm.putSubtask(subtask2, epic, Statuses.NEW, LocalDateTime.of(2022, 1, 1, 4, 0),
                60);
        tm.putSubtask(subtask3, epic, Statuses.NEW, LocalDateTime.of(2022, 1, 1, 3, 0),
                60);
    }
}