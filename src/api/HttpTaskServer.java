package api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Statuses;
import tasks.Subtask;
import tasks.Task;
import utility.IntersectionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class HttpTaskServer {
    HttpServer server;
    private final int PORT = 8080;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        gson = Managers.getDefaultGson();
        taskManager = Managers.getDefault();
        server.createContext("/tasks", new TaskHandler());
        server.start();
    }

    public class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
           Task task1 = new Task("a", "b", Statuses.NEW,
                    LocalDateTime.of(2022,1,22,9,0), 10);
            //taskManager.addEpic(new Epic(null, null));
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = "";
            String response = "";
            if (exchange.getRequestURI().getQuery() != null) {
                query = exchange.getRequestURI().getQuery();
            }
            try {
                switch (method) {
                    case "GET":
                        if (query.contains("id=") && path.contains("tasks/subtask/epic")) {
                            int id = Integer.parseInt(query.split("=")[1]);
                            if (taskManager.getAllSubtasksOfEpic(id) == null) {
                                System.out.println("Не найждено задач.");
                                exchange.sendResponseHeaders(404, 0);
                            } else {
                                response = gson.toJson(taskManager.getAllSubtasksOfEpic(id));
                                exchange.sendResponseHeaders(200, 0);
                            }
                        } else if (query.contains("id=") && path.contains("tasks/task")) {
                            int id = Integer.parseInt(query.split("=")[1]);
                            if (taskManager.getTasks().contains(taskManager.getTaskById(id))) {
                                response = gson.toJson(taskManager.getTaskById(id));
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                System.out.println("Не найждено задач.");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.contains("tasks/task")) {
                            if (taskManager.getTasks().size() == 0) {
                                System.out.println("Не найждено задач.");
                                exchange.sendResponseHeaders(404, 0);
                            }
                            response = gson.toJson(taskManager.getTasks());
                            exchange.sendResponseHeaders(200, 0);
                        } else if (query.contains("id=") && path.contains("tasks/subtask")) {
                            int id = Integer.parseInt(query.split("=")[1]);
                            if (taskManager.getSubtasks().contains(taskManager.getSubtaskById(id))) {
                                response = gson.toJson(taskManager.getSubtaskById(id));
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                System.out.println("Не найждено задач.");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.contains("tasks/subtask")) {
                            if (taskManager.getSubtasks().size() == 0) {
                                System.out.println("Не найждено задач.");
                                exchange.sendResponseHeaders(404, 0);
                            } else {
                                response = gson.toJson(taskManager.getSubtasks());
                                exchange.sendResponseHeaders(200, 0);
                            }
                        } else if (query.contains("id=") && path.contains("tasks/epic")) {
                            int id = Integer.parseInt(query.split("=")[1]);
                            if (taskManager.getEpics().contains(taskManager.getEpicById(id))) {
                                response = gson.toJson(taskManager.getEpicById(id));
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                System.out.println("Не найждено задач.");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.contains("tasks/epic")) {
                            if (taskManager.getEpics().size() == 0) {
                                System.out.println("Не найждено задач.");
                                exchange.sendResponseHeaders(404, 0);
                            } else {
                                response = gson.toJson(taskManager.getEpics());
                                exchange.sendResponseHeaders(200, 0);
                            }
                        } else if (path.contains("tasks/history")) {
                            response = gson.toJson(taskManager.getHistory());
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.contains("tasks")) {
                            response = gson.toJson(taskManager.getPrioritizedTask());
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Неверный запрос.");
                            exchange.sendResponseHeaders(400, 0);
                        }
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(response.getBytes(StandardCharsets.UTF_8));
                        }
                        break;
                    case "POST":
                        BufferedReader bf = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
                        String line = bf.readLine();
                        if (line == null) {
                            exchange.sendResponseHeaders(400, 0);
                            break;
                        }
                        if (path.contains("tasks/task")) {
                            Task task = gson.fromJson(line, Task.class);
                            if (task == null) {
                                System.out.println("Не указано тело запроса.");
                                exchange.sendResponseHeaders(400, 0);
                            }
                            if (!taskManager.taskMap().containsKey(task.getTaskId())) {
                                try {
                                    System.out.println("Задача успешно добавлена.");
                                    taskManager.addTask(task);
                                    exchange.sendResponseHeaders(201, 0);
                                } catch (IntersectionException e){
                                    System.out.println("Данное время уже занято другой задачей.");
                                    exchange.sendResponseHeaders(400, 0);
                                }
                            } else {
                                System.out.println("Задача успешно обновлена.");
                                taskManager.updateTask(task);
                                exchange.sendResponseHeaders(200, 0);
                            }
                        } else if (path.contains("tasks/epic")) {
                            Epic epic = gson.fromJson(line, Epic.class);
                            if (!taskManager.epicMap().containsKey(epic.getTaskId())) {
                                taskManager.addEpic(epic);
                                System.out.println("Эпик успешно добавлен.");
                                exchange.sendResponseHeaders(201, 0);
                            } else {
                                taskManager.updateEpic(epic);
                                System.out.println("Эпик успешно обновлен.");
                                exchange.sendResponseHeaders(200, 0);
                            }
                        } else if (path.contains("tasks/subtask")) {
                            Subtask subtask = gson.fromJson(line, Subtask.class);
                            if (!taskManager.subtaskMap().containsKey(subtask.getTaskId()) &&
                                    taskManager.epicMap().containsKey(subtask.getIdEpic())) {
                                try {
                                    taskManager.addSubtask(subtask);
                                    System.out.println("Подзадача успешно добавлена.");
                                    exchange.sendResponseHeaders(201, 0);
                                } catch (IntersectionException e){
                                    System.out.println("Данное время уже занято другой задачей.");
                                    exchange.sendResponseHeaders(400, 0);
                                }
                            } else if (taskManager.subtaskMap().containsKey(subtask.getTaskId())) {
                                taskManager.updateSubtask(subtask);
                                System.out.println("Подзадача успешно обновлена.");
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                System.out.println("Нет эпика для подзадачи.");
                                exchange.sendResponseHeaders(400, 0);
                            }
                        } else {
                            System.out.println("Неверный запрос.");
                            exchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    case "DELETE":
                        if (path.contains("tasks/task") && query.contains("id=")) {
                            int id = Integer.parseInt(query.split("=")[1]);
                            if (taskManager.getTasks().contains(taskManager.getTaskById(id))) {
                                taskManager.removeTaskById(id);
                                System.out.println("Задача удалена.");
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                System.out.println("Задача не найдена.");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.contains("tasks/epic") && query.contains("id=")) {
                            int id = Integer.parseInt(query.split("=")[1]);
                            if (taskManager.getEpics().contains(taskManager.getEpicById(id))) {
                                taskManager.removeEpicById(id);
                                System.out.println("Эпик удален.");
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                System.out.println("Эпик не найден.");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.contains("tasks/subtask") && query.contains("id=")) {
                            int id = Integer.parseInt(query.split("=")[1]);
                            if (taskManager.getSubtasks().contains(taskManager.getSubtaskById(id))) {
                                taskManager.removeSubtaskById(id);
                                System.out.println("Подзадача удалена.");
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                System.out.println("Подзадача не найдена.");
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.contains("tasks/task")) {
                            taskManager.clearTaskList();
                            System.out.println("Список задач отчищен.");
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.contains("tasks/subtask")) {
                            taskManager.clearSubtaskList();
                            System.out.println("Список эпиков отчищен");
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.contains("tasks/epic")) {
                            taskManager.clearEpicList();
                            System.out.println("Список подзадач отчищен");
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Неверный запрос");
                            exchange.sendResponseHeaders(400, 0);
                        }
                    default:
                        System.out.println("Неверный запрос");
                        exchange.sendResponseHeaders(400, 0);
                }
            } catch (IOException e){
                System.out.println("Произошла ошибка.");
            } finally {
                exchange.close();
            }
        }
    }

    public void stop() {
        server.stop(0);
    }
}
