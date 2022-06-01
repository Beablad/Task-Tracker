package utility;

import managers.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TasksType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVSerializator {


    public String toString(Task task) {
        StringBuilder stringBuilder = new StringBuilder();
        if (task instanceof Epic) {
            stringBuilder.append(task.getTaskId() + "," + TasksType.EPIC + "," + task.getTaskName() + "," +
                    task.getTaskStatus() + "," + task.getTaskInfo() + ",-," + task.getStartTime() +
                    "," + task.getDuration());
        } else if (task instanceof Subtask) {
            stringBuilder.append(task.getTaskId() + "," + TasksType.SUBTASK + "," + task.getTaskName() + "," +
                    task.getTaskStatus() + "," + task.getTaskInfo() + "," + ((Subtask) task).getIdEpic() + "," +
                    task.getStartTime() + "," + task.getDuration());
        } else if (task instanceof Task) {
            stringBuilder.append(task.getTaskId() + "," + TasksType.TASK + "," + task.getTaskName() + "," +
                    task.getTaskStatus() + "," + task.getTaskInfo() + ",-," + task.getStartTime() +
                    "," + task.getDuration());
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
            throw new ManagerSaveException("В указанной директории нет файла");
        }
    }

    public static String historyToString(HistoryManager historyManager) {
        List<Task> list = historyManager.getHistory();
        StringBuilder sb = new StringBuilder();
        for (Task task : list) {
            sb.append(task.getTaskId() + ",");
        }
        return sb.toString();
    }

    public static List<String> historyFromString() {
        List<String> list = new ArrayList<>();
        try (BufferedReader bw = new BufferedReader(new FileReader("tracker.csv"))) {
            while (bw.ready()) {
                if (bw.readLine().equals("")) {
                    break;
                }
            }
            String str = bw.readLine();
            if (str != null) {
                StringBuilder sb = new StringBuilder(str);
                sb.reverse();
                sb.deleteCharAt(0);
                String[] array = sb.toString().split(",");
                for (int i = 0; i < array.length; i++) {
                    list.add(array[i]);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("В указанной директории нет файла");
        }
        return list;
    }
}

