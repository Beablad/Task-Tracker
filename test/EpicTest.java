import managers.FileBackedTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Statuses;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    @Test
    void epicStatusWithoutSubtask(){
        Epic epic = new Epic("a", "b");
        Assertions.assertEquals(Statuses.NEW, epic.getTaskStatus());
    }

    @Test
    void epicStatusWithNewSubtask(){
        FileBackedTaskManager fb = new FileBackedTaskManager();
        Epic epic = new Epic("a", "b");
        Subtask subtask1 = new Subtask("a", "b");
        Subtask subtask2 = new Subtask("a", "b");
        fb.putSubtask(subtask1, epic, Statuses.NEW);
        fb.putSubtask(subtask2, epic, Statuses.NEW);
        assertEquals(Statuses.NEW, epic.getTaskStatus());
    }

    @Test
    void epicStatusWithDoneSubtask(){
        FileBackedTaskManager fb = new FileBackedTaskManager();
        Epic epic = new Epic("a", "b");
        Subtask subtask1 = new Subtask("a", "b");
        Subtask subtask2 = new Subtask("a", "b");
        fb.putSubtask(subtask1, epic, Statuses.DONE);
        fb.putSubtask(subtask2, epic, Statuses.DONE);
        assertEquals(Statuses.DONE, epic.getTaskStatus());
    }

    @Test
    void epicStatusWithNewAndDoneSubtask(){
        FileBackedTaskManager fb = new FileBackedTaskManager();
        Epic epic = new Epic("a", "b");
        Subtask subtask1 = new Subtask("a", "b");
        Subtask subtask2 = new Subtask("a", "b");
        fb.putSubtask(subtask1, epic, Statuses.NEW);
        fb.putSubtask(subtask2, epic, Statuses.DONE);
        assertEquals(Statuses.NEW, epic.getTaskStatus());
    }

    @Test
    void epicStatusWithInProgressSubtask(){
        FileBackedTaskManager fb = new FileBackedTaskManager();
        Epic epic = new Epic("a", "b");
        Subtask subtask1 = new Subtask("a", "b");
        Subtask subtask2 = new Subtask("a", "b");
        fb.putSubtask(subtask1, epic, Statuses.IN_PROGRESS);
        fb.putSubtask(subtask2, epic, Statuses.IN_PROGRESS);
        assertEquals(Statuses.IN_PROGRESS, epic.getTaskStatus());
    }
}