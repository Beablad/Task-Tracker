package tasks;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    TaskManager fb = Managers.getDefault();
    Epic epic = new Epic("a", "b");
    Subtask subtask = new Subtask("a", "b");
    Subtask subtask1 = new Subtask("a", "b");
    Subtask subtask2 = new Subtask("a", "b");

    @Test
    void checkStatusWithoutSubtask() {
        fb.addEpic(epic);
        assertEquals(Statuses.NEW, epic.getTaskStatus());
    }

    @Test
    void checkStatusWithNewSubtasks() {
        fb.addEpic(epic);
        fb.addSubtask(subtask, epic, Statuses.NEW, LocalDateTime.of(2022, 1, 1, 10, 0), 10);
        fb.addSubtask(subtask1, epic, Statuses.NEW, LocalDateTime.of(2022, 1, 1, 12, 0), 10);
        fb.addSubtask(subtask2, epic, Statuses.NEW, LocalDateTime.of(2022, 1, 1, 14, 0), 10);
        assertEquals(Statuses.NEW, epic.getTaskStatus());
    }

    @Test
    void checkStatusWithDoneSubtasks() {
        fb.addEpic(epic);
        fb.addSubtask(subtask, epic, Statuses.DONE, LocalDateTime.of(2022, 1, 1, 10, 0), 10);
        fb.addSubtask(subtask1, epic, Statuses.DONE, LocalDateTime.of(2022, 1, 1, 12, 0), 10);
        fb.addSubtask(subtask2, epic, Statuses.DONE, LocalDateTime.of(2022, 1, 1, 14, 0), 10);
        assertEquals(Statuses.DONE, epic.getTaskStatus());
    }

    @Test
    void  checkStatusWithDoneAndNewSubtasks (){
        fb.addEpic(epic);
        fb.addSubtask(subtask, epic, Statuses.DONE, LocalDateTime.of(2022, 1, 1, 10, 0), 10);
        fb.addSubtask(subtask1, epic, Statuses.DONE, LocalDateTime.of(2022, 1, 1, 12, 0), 10);
        fb.addSubtask(subtask2, epic, Statuses.NEW, LocalDateTime.of(2022, 1, 1, 14, 0), 10);
        assertEquals(Statuses.NEW, epic.getTaskStatus());
    }

    @Test
    void checkStatusWithDInProgressSubtasks (){
        fb.addEpic(epic);
        fb.addSubtask(subtask, epic, Statuses.IN_PROGRESS, LocalDateTime.of(2022, 1, 1, 10, 0), 10);
        fb.addSubtask(subtask1, epic, Statuses.IN_PROGRESS, LocalDateTime.of(2022, 1, 1, 12, 0), 10);
        fb.addSubtask(subtask2, epic, Statuses.IN_PROGRESS, LocalDateTime.of(2022, 1, 1, 14, 0), 10);
        assertEquals(Statuses.IN_PROGRESS, epic.getTaskStatus());
    }
}