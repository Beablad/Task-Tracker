package managers;

import org.junit.jupiter.api.Test;
import tasks.Statuses;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryManagerTest {

    @Test
    void emptyHistoryAdd(){
        HistoryManager hm = Managers.getDefaultHistoryManager();
        Task task = new Task("a", "b",
                LocalDateTime.of(2022, 5, 28, 10, 0), 180);
        hm.add(task);
        List<Task> tasks = List.of(task);
        assertEquals(tasks, hm.getHistory());
    }

    @Test
    void emptyHistoryGetHistory(){
        HistoryManager hm = Managers.getDefaultHistoryManager();
        assertEquals(new ArrayList<>(), hm.getHistory());
    }

    @Test
    void checkDoublesHistoryManager(){
        HistoryManager hm = Managers.getDefaultHistoryManager();
        Task task = new Task("a", "b");
        hm.add(task);
        hm.add(task);
        List<Task> tasks = List.of(task);
        System.out.println(hm.getHistory());
        assertEquals(tasks, hm.getHistory());
    }

    //так как для задач ид присваиваются в момент их добавления в мапу, проверить метод удаления по ид в самом
    // HistoryManager не получится

    @Test
    void checkRemoveStart(){
        FileBackedTaskManager bm = new FileBackedTaskManager();
        Task task1 = new Task("a", "b");
        Task task2 = new Task("a", "b");
        Task task3 = new Task("a", "b");
        Task task4 = new Task("a", "b");
        Task task5 = new Task("a", "b");
        bm.putTask(task1, Statuses.NEW);
        bm.putTask(task2, Statuses.NEW);
        bm.putTask(task3, Statuses.NEW);
        bm.putTask(task4, Statuses.NEW);
        bm.putTask(task5, Statuses.NEW);
        bm.returnTaskById(1);
        bm.returnTaskById(2);
        bm.returnTaskById(3);
        bm.returnTaskById(4);
        bm.returnTaskById(5);
        bm.removeHistory(1);
        List<Task> list = List.of(task5, task4, task3, task2);
        assertEquals(list, bm.getHistory());
    }

    @Test
    void checkRemoveMiddle(){
        FileBackedTaskManager bm = new FileBackedTaskManager();
        Task task1 = new Task("a", "b");
        Task task2 = new Task("a", "b");
        Task task3 = new Task("a", "b");
        Task task4 = new Task("a", "b");
        Task task5 = new Task("a", "b");
        bm.putTask(task1, Statuses.NEW);
        bm.putTask(task2, Statuses.NEW);
        bm.putTask(task3, Statuses.NEW);
        bm.putTask(task4, Statuses.NEW);
        bm.putTask(task5, Statuses.NEW);
        bm.returnTaskById(1);
        bm.returnTaskById(2);
        bm.returnTaskById(3);
        bm.returnTaskById(4);
        bm.returnTaskById(5);
        bm.removeHistory(3);
        List<Task> list = List.of(task5, task4, task2, task1);
        assertEquals(list, bm.getHistory());
    }

    @Test
    void checkRemoveEnd(){
        FileBackedTaskManager bm = new FileBackedTaskManager();
        Task task1 = new Task("a", "b");
        Task task2 = new Task("a", "b");
        Task task3 = new Task("a", "b");
        Task task4 = new Task("a", "b");
        Task task5 = new Task("a", "b");
        bm.putTask(task1, Statuses.NEW);
        bm.putTask(task2, Statuses.NEW);
        bm.putTask(task3, Statuses.NEW);
        bm.putTask(task4, Statuses.NEW);
        bm.putTask(task5, Statuses.NEW);
        bm.returnTaskById(1);
        bm.returnTaskById(2);
        bm.returnTaskById(3);
        bm.returnTaskById(4);
        bm.returnTaskById(5);
        bm.removeHistory(5);
        List<Task> list = List.of(task4, task3, task2, task1);
        assertEquals(list, bm.getHistory());
    }
}
