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

    public static void main(String[] args) throws IOException {
        new HttpTaskServer();
    }

    public class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            //taskManager.addTask(new Task("a", "b", Statuses.NEW,
            //        LocalDateTime.of(2022,1,22,9,0), 10));
            taskManager.addEpic(new Epic(null, null));
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
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.contains("tasks/task")) {
                            if (taskManager.getTasks().size() == 0) {
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
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.contains("tasks/subtask")) {
                            if (taskManager.getSubtasks().size() == 0) {
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
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.contains("tasks/epic")) {
                            if (taskManager.getEpics().size() == 0) {
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
                                exchange.sendResponseHeaders(400, 0);
                            }
                            if (!taskManager.getTasks().contains(task)) {
                                try {
                                    taskManager.addTask(task);
                                    exchange.sendResponseHeaders(201, 0);
                                } catch (IntersectionException e){
                                    e.getMessage();
                                    exchange.sendResponseHeaders(400, 0);
                                }
                            } else {
                                taskManager.updateTask(task);
                                exchange.sendResponseHeaders(200, 0);
                            }
                        } else if (path.contains("tasks/epic")) {
                            Epic epic = gson.fromJson(line, Epic.class);
                            if (!taskManager.getEpics().contains(epic)) {
                                taskManager.addEpic(epic);
                                exchange.sendResponseHeaders(201, 0);
                            } else {
                                taskManager.updateEpic(epic);
                                exchange.sendResponseHeaders(200, 0);
                            }
                        } else if (path.contains("tasks/subtask")) {
                            Subtask subtask = gson.fromJson(line, Subtask.class);
                            if (!taskManager.getSubtasks().contains(subtask) &&
                                    taskManager.getEpics().contains(taskManager.getEpicById(subtask.getIdEpic()))) {
                                try {
                                    taskManager.addSubtask(subtask);
                                    exchange.sendResponseHeaders(201, 0);
                                } catch (IntersectionException e){
                                    e.getMessage();
                                    exchange.sendResponseHeaders(400, 0);
                                }
                            } else if (taskManager.getSubtasks().contains(subtask)) {
                                taskManager.updateSubtask(subtask);
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                exchange.sendResponseHeaders(400, 0);
                            }
                        } else {
                            exchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    case "DELETE":
                        if (path.contains("tasks/task") && query.contains("id=")) {
                            int id = Integer.parseInt(query.split("=")[1]);
                            if (taskManager.getTasks().contains(taskManager.getTaskById(id))) {
                                taskManager.removeTaskById(id);
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.contains("tasks/epic") && query.contains("id=")) {
                            int id = Integer.parseInt(query.split("=")[1]);
                            if (taskManager.getEpics().contains(taskManager.getEpicById(id))) {
                                taskManager.removeEpicById(id);
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.contains("tasks/subtask") && query.contains("id=")) {
                            int id = Integer.parseInt(query.split("=")[1]);
                            if (taskManager.getSubtasks().contains(taskManager.getSubtaskById(id))) {
                                taskManager.removeSubtaskById(id);
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                exchange.sendResponseHeaders(404, 0);
                            }
                        } else if (path.contains("tasks/task")) {
                            taskManager.clearTaskList();
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.contains("tasks/subtask")) {
                            taskManager.clearSubtaskList();
                            exchange.sendResponseHeaders(200, 0);
                        } else if (path.contains("tasks/epic")) {
                            taskManager.clearEpicList();
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            exchange.sendResponseHeaders(400, 0);
                        }
                }
            } catch (IOException e){
                System.out.println("Произошла ошибка");
            } finally {
                exchange.close();
            }
        }
    }

    public void stop() {
        server.stop(0);
    }
}
