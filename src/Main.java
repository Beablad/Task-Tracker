import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        /*Task task = new Task("task", "info");
        manager.putTask(task, "NEW");
        System.out.println(manager.returnTaskInfo());*/
        Epic epic = new Epic("Epic", "Info");
        manager.putEpic(epic);

        manager.putSubtask(new Subtask("Sub", "Info", 1), "IN_PROGRESS");
        manager.putSubtask(new Subtask("Sub2", "Info", 1), "DONE");
        System.out.println(manager.returnEpicInfo());
        System.out.println(manager.returnSubtaskInfo());
        manager.removeEpic(1);
        System.out.println(manager.returnEpicInfo());
        System.out.println(manager.returnSubtaskInfo());
    }
}