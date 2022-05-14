package utility;

import managers.InMemoryTaskManager;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TasksType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVSerializator {


    public String toString(Task task) {
        StringBuilder stringBuilder = new StringBuilder();
        if (task instanceof Epic) {
            stringBuilder.append(task.getTaskId() + "," + TasksType.EPIC + "," + task.getTaskName() + "," + task.getTaskStatus() +
                    "," + task.getTaskInfo());
        } else if (task instanceof Subtask) {
            stringBuilder.append(task.getTaskId() + "," + TasksType.SUBTASK + "," + task.getTaskName() + "," + task.getTaskStatus() +
                    "," + task.getTaskInfo() + "," + ((Subtask) task).getIdEpic());
        } else if (task instanceof Task) {
            stringBuilder.append(task.getTaskId() + "," + TasksType.TASK + "," + task.getTaskName() + "," + task.getTaskStatus() +
                    "," + task.getTaskInfo());
        }
        return stringBuilder.toString();
    }

    public List<String> fromString() {
        List<String> list = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("tracker.csv"))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.equals("")) {
                    break;
                }
                list.add(line);
            }
            return list;
        } catch (IOException e) {
            throw new ManagerSaveException("");
        }
    }

    public static String historyToString() {
        TaskManager manager = new InMemoryTaskManager();
        List<Task> list = manager.getHistory();
        StringBuilder sb = new StringBuilder();
        for (Task task : list) {
            sb.append(task.getTaskId()+",");
        }
        return sb.toString();
    }

    public static List<String> historyFromString() {
        List<String> list = new ArrayList<>();
        try (BufferedReader bw = new BufferedReader(new FileReader("tracker.csv"))){
            String str = bw.readLine();
            while (!str.equals("")){
                bw.readLine();
            }
            str = bw.readLine();
            String[] array = str.split(",");
            for (int i = 0; i < array.length; i++) {
                list.add(array[i]);
            }
        } catch (IOException e){
            System.out.println("");
        }
        return list;
    }
}

