package tasks;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    TaskManager fb = Managers.getDefault();
    Epic epic = new Epic("a", "b");
    Subtask subtask = new Subtask("a", "b", Statuses.NEW, null, 0, epic);
    Subtask subtask1 = new Subtask("a", "b", Statuses.NEW, null, 0, epic);
    Subtask subtask2 = new Subtask("a", "b", Statuses.NEW, null, 0, epic);

    @Test
    void checkStatusWithoutSubtask() {
        fb.addEpic(epic);
        assertEquals(Statuses.NEW, epic.getTaskStatus());
    }

    @Test
    void checkStatusWithNewSubtasks() {
        fb.addEpic(epic);
        fb.addSubtask(subtask);
        fb.addSubtask(subtask1);
        fb.addSubtask(subtask2);
        assertEquals(Statuses.NEW, epic.getTaskStatus());
    }

    @Test
    void checkStatusWithDoneSubtasks() {
        fb.addEpic(epic);
        fb.addSubtask(subtask);
        fb.addSubtask(subtask1);
        fb.addSubtask(subtask2);
        assertEquals(Statuses.DONE, epic.getTaskStatus());
    }

    @Test
    void checkStatusWithDoneAndNewSubtasks() {
        fb.addEpic(epic);
        fb.addSubtask(subtask);
        fb.addSubtask(subtask1);
        fb.addSubtask(subtask2);
        assertEquals(Statuses.NEW, epic.getTaskStatus());
    }

    @Test
    void checkStatusWithDInProgressSubtasks() {
        fb.addEpic(epic);
        fb.addSubtask(subtask);
        fb.addSubtask(subtask1);
        fb.addSubtask(subtask2);
        assertEquals(Statuses.IN_PROGRESS, epic.getTaskStatus());
    }
}