package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Statuses;
import tasks.Task;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        init();
    }

    @Test
    public void createInMemoryTaskManager() {  // тут достаточно одного теста, проверить также эпики и сабтаски
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        final HashMap<Integer, Task> taskList = taskManager.returnTasks();
        assertNotNull(taskList, "Возвращает пустой список задач");
        assertEquals(0, taskList.size(), "Возвращает пустой список задач");
    }

    @Test
    public void checkIdEpicInSubtask() {
        assertEquals(2, subtask1.getIdEpic());
        assertEquals(2, subtask2.getIdEpic());
        assertEquals(2, subtask3.getIdEpic());
    }

    @Test
    public void checkEpicStatus() {
        assertEquals(Statuses.NEW, epic.getTaskStatus());
    }
}