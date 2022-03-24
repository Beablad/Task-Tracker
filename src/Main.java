import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("test", "new", "new");
        manager.putTask(task1);
        manager.printTaskInfo();
        Task task2 = new Task("test", "old", "IN_PROGRESS");
        manager.updateTask(task2, 1);
        manager.printTaskInfo();
        Subtask sub1 = new Subtask("1", "2", "NEW", 6);
        manager.putSubtask(sub1);
        Subtask sub2 = new Subtask("12", "12", "NEW", 6);
        manager.putSubtask(sub2);
        Subtask sub5 = new Subtask("123", "123", "NEW", 6);
        manager.putSubtask(sub5);
        Subtask sub7 = new Subtask("1234", "1234", "IN_PROGRESS", 6);
        manager.putSubtask(sub7);
        ArrayList<Subtask> subs = new ArrayList<>();
        subs.add(sub1);
        subs.add(sub2);
        subs.add(sub5);
        subs.add(sub7);
        Epic epic = new Epic("2", "3", "NEW", subs);
        manager.putEpic(epic);
        manager.printEpicInfo();
        Subtask sub3 = new Subtask("1", "2", "DONE", 6);
        Subtask sub4 = new Subtask("12", "12", "DONE", 6);
        Subtask sub6 = new Subtask("123", "123", "DONE", 6);
        manager.updateSubtask(sub3, 2);
        manager.updateSubtask(sub4, 3);
        manager.updateSubtask(sub6, 4);
        manager.updateEpic(epic, 6);
        System.out.println(manager.getAllSubtasksOfEpic(6));
        manager.printEpicInfo();
    }
}