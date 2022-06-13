package managers;

import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
    Epic epic = new Epic("a", "b");

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        init();
    }

    @Test
    @BeforeEach
    void checkSaveEpicToFile() {
        addEpics();
        try (BufferedReader fr = new BufferedReader(new FileReader("tracker.csv"))) {
            fr.readLine();
            String string = fr.readLine();
            assertEquals(epic.getTaskId() + "," + taskType(epic) + "," + epic.getTaskName() + "," +
                    epic.getTaskStatus() + "," + epic.getTaskInfo() + ",-," + epic.getStartTime() + "," +
                    epic.getDuration(), string);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    @Test
    void checkSaveEmpty() {
        fileBackedTaskManager.clearEpicList();
        try (BufferedReader fr = new BufferedReader(new FileReader("tracker.csv"))) {
            assertEquals("id,type,name,status,description,epic,startTime,duration ", fr.readLine());
            assertEquals("", fr.readLine());
        } catch (IOException e) {
            e.getMessage();
        }
    }

    @Test
    void checkLoadFromFile (){
        FileBackedTaskManager fbtm = FileBackedTaskManager.loadFromFile();
        Epic epic = fbtm.epicMap.get(1);
        try (BufferedReader fr = new BufferedReader(new FileReader("tracker.csv"))){
            fr.readLine();
            String string = fr.readLine();
            assertEquals(string,epic.getTaskId() + "," + taskType(epic) + "," + epic.getTaskName() + "," +
                    epic.getTaskStatus() + "," + epic.getTaskInfo() + ",-," + epic.getStartTime() + "," +
                    epic.getDuration());
        } catch (IOException e){
            e.getMessage();
        }
    }

    protected void addEpics() {
        fileBackedTaskManager.addEpic(epic);
    }

    private static String taskType(Task task) {
        if (task instanceof Epic) {
            return "EPIC";
        } else if (task instanceof Subtask) {
            return "SUBTASK";
        } else {
            return "TASK";
        }
    }
}