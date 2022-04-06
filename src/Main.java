public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task = new Task("task", "info");
        inMemoryTaskManager.putTask(task, Statuses.NEW);
        System.out.println(inMemoryTaskManager.returnTaskInfo());

        Epic epic = new Epic("Epic", "Info");
        inMemoryTaskManager.putEpic(epic);

        inMemoryTaskManager.putSubtask(new Subtask("Sub", "Info"), epic, Statuses.DONE);
        inMemoryTaskManager.putSubtask(new Subtask("Sub2", "Info"), epic, Statuses.DONE);
        System.out.println(inMemoryTaskManager.returnEpicInfo());
        System.out.println(inMemoryTaskManager.returnSubtaskInfo());

        inMemoryTaskManager.returnEpicById(2);
        inMemoryTaskManager.returnTaskById(1);
        inMemoryTaskManager.returnSubtaskById(3);
        System.out.println(inMemoryTaskManager.getHistory());
    }
}