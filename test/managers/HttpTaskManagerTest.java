package managers;

import org.junit.jupiter.api.BeforeEach;

class HttpTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        init();
    }
}