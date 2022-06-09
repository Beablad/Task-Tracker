package managers;

import org.junit.jupiter.api.Test;
import tasks.Statuses;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager hm = Managers.getDefaultHistoryManager();
    Task task1 = new Task("1", "2", Statuses.NEW, null, 0);
    Task task2 = new Task("2", "2", Statuses.NEW, null, 0);
    Task task3 = new Task("3", "3", Statuses.NEW, null, 0);
    Task task4 = new Task("4", "4", Statuses.NEW, null, 0);
    Task task5 = new Task("5", "5", Statuses.NEW, null, 0);
    Task task6 = new Task("6", "6", Statuses.NEW, null, 0);
    Task task7 = new Task("7", "7", Statuses.NEW, null, 0);
    Task task8 = new Task("8", "8", Statuses.NEW, null, 0);
    Task task9 = new Task("9", "9", Statuses.NEW, null, 0);
    Task task10 = new Task("10", "10", Statuses.NEW, null, 0);
    Task task11 = new Task("11", "11", Statuses.NEW, null, 0);


    @Test
    void addInEmptyHistory() {
        hm.add(task1);
        List<Task> list = List.of(task1);
        assertEquals(list, hm.getHistory());
    }

    @Test
    void addInNotEmptyHistory() {
        addInHistory();
        hm.add(task10);
        List<Task> list = List.of(task10, task9, task8, task7, task6, task5, task4, task3, task2, task1);
        assertEquals(list, hm.getHistory());
    }

    @Test
    void addInFullHistory(){
        addInHistory();
        hm.add(task10);
        hm.add(task11);
        List<Task> list = List.of(task11, task10, task9, task8, task7, task6, task5, task4, task3, task2);
        assertEquals(list, hm.getHistory());
    }

    @Test
    void addDoublesInHistory(){
        addInHistory();
        hm.add(task10);
        hm.add(task2);
        List<Task> list = List.of(task2, task10, task9, task8, task7, task6, task5, task4, task3, task1);
        assertEquals(list, hm.getHistory());
    }

    @Test
    void removeFromNotEmptyHistory() {
        addInHistory();
        hm.remove(9);
        List<Task> list = List.of(task8, task7, task6, task5, task4, task3, task2, task1);
        assertEquals(list, hm.getHistory());
    }

    @Test
    void removeFromEmptyHistory() {
        hm.remove(1);
        List<Task> list = List.of();
        assertEquals(list, hm.getHistory());
    }

    private void addInHistory(){
        InMemoryTaskManager tm = new InMemoryTaskManager();
        tm.addTask(task1);
        tm.addTask(task2);
        tm.addTask(task3);
        tm.addTask(task4);
        tm.addTask(task5);
        tm.addTask(task6);
        tm.addTask(task7);
        tm.addTask(task8);
        tm.addTask(task9);
        tm.addTask(task10);
        tm.addTask(task11);
        hm.add(task1);
        hm.add(task2);
        hm.add(task3);
        hm.add(task4);
        hm.add(task5);
        hm.add(task6);
        hm.add(task7);
        hm.add(task8);
        hm.add(task9);
    }
}