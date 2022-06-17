package tasks;

import api.HttpTaskServer;
import api.KVServer;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    TaskManager taskManager = Managers.getDefault();
    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    Epic epic = new Epic("a", "b");
    Subtask subtask;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    void start() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        taskManager.addEpic(epic);
        subtask = new Subtask("a", "b", Statuses.NEW, null, 0, epic);
        subtask1  = new Subtask("a", "b", Statuses.NEW, null, 0, epic);
        subtask2 = new Subtask("a", "b", Statuses.NEW, null, 0, epic);
    }

    @AfterEach
    void stop(){
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    void checkStatusWithoutSubtask() {
        assertEquals(Statuses.NEW, epic.getTaskStatus());
    }

    @Test
    void checkStatusWithNewSubtasks() {
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(Statuses.NEW, epic.getTaskStatus());
    }

    @Test
    void checkStatusWithDoneSubtasks() {
        subtask.setTaskStatus(Statuses.DONE);
        subtask1.setTaskStatus(Statuses.DONE);
        subtask2.setTaskStatus(Statuses.DONE);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(Statuses.DONE, epic.getTaskStatus());
    }

    @Test
    void checkStatusWithDoneAndNewSubtasks() {
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(Statuses.NEW, epic.getTaskStatus());
    }

    @Test
    void checkStatusWithDInProgressSubtasks() {
        subtask.setTaskStatus(Statuses.IN_PROGRESS);
        subtask1.setTaskStatus(Statuses.IN_PROGRESS);
        subtask2.setTaskStatus(Statuses.IN_PROGRESS);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(Statuses.IN_PROGRESS, epic.getTaskStatus());
    }
}