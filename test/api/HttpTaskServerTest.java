package api;

import api.HttpTaskServer;
import com.google.gson.Gson;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    HttpTaskServer server;
    KVServer kvServer;
    Gson gson = Managers.getDefaultGson();
    HttpClient client = HttpClient.newHttpClient();
    Task task;
    Epic epic;
    Subtask subtask;


    HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    void start() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        server = new HttpTaskServer();
    }

    @AfterEach
    void stop() {
        kvServer.stop();
        server.stop();
    }

    @Test
    void addTask() throws IOException, InterruptedException {
        task = new Task("a", "b", Statuses.NEW, LocalDateTime.of(2022, 1, 1, 0, 0), 0);
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(task)))
                .uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void addSubtask() throws IOException, InterruptedException {
        epic = new Epic("a", "b");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest requestPostEpic = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(epic)))
                .uri(urlEpic).build();
        client.send(requestPostEpic, HttpResponse.BodyHandlers.ofString());
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/tasks/epic?id=1")).build();
        epic = gson.fromJson(client.send(request, HttpResponse.BodyHandlers.ofString()).body(), Epic.class);
        subtask = new Subtask("a", "b", Statuses.NEW,
                LocalDateTime.of(2022, 1, 1, 0, 0), 0, epic);
        URI urlSubtask = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest requestPostSubtask = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(subtask)))
                .uri(urlSubtask).build();
        HttpResponse<String> responseSubtask = client.send(requestPostSubtask, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseSubtask.statusCode());
    }


    @Test
    void addEpic() throws IOException, InterruptedException {
        epic = new Epic("a", "b");
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(epic)))
                .uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        init();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        init();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getSubtasks() throws IOException, InterruptedException {
        init();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response404 = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response404.statusCode());
        init();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getSubtaskById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response404 = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response404.statusCode());
        init();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void clearTaskList() throws IOException, InterruptedException {
        init();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void removeTask() throws IOException, InterruptedException {
        init();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void removeEpic() throws IOException, InterruptedException {
        init();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void removeSubtask() throws IOException, InterruptedException {
        init();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getAllSubtasksOfEpic() throws IOException, InterruptedException {
        init();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic?id=2");
        HttpRequest requestEpic = HttpRequest.newBuilder().GET().uri(urlEpic).build();
        epic = gson.fromJson(getTask(TasksType.EPIC), Epic.class);
        assertEquals(1, epic.getListOfSubtask().size());
    }

    @Test
    void getPrioritizedTask() throws IOException, InterruptedException {
        init();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> list = gson.fromJson(response.body().toString(), List.class);
        assertEquals(2, list.size());
    }

    String toJson(Task task) {
        if (task instanceof Epic) {
            System.out.println(gson.toJson(task));
            return gson.toJson(task);
        } else if (task instanceof Subtask) {
            System.out.println(gson.toJson(task));
            return gson.toJson(task);
        } else {
            System.out.println(gson.toJson(task));
            return gson.toJson(task);
        }
    }


    void init() throws IOException, InterruptedException {
        task = new Task("a", "b", Statuses.NEW, LocalDateTime.of(2022, 1, 1, 0, 0), 0);
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(task)))
                .uri(url).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        epic = new Epic("a", "b");
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest requestPostEpic = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(epic)))
                .uri(urlEpic).build();
        client.send(requestPostEpic, HttpResponse.BodyHandlers.ofString());
        HttpRequest request1 = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/tasks/epic?id=2")).build();
        String s = client.send(request1, HttpResponse.BodyHandlers.ofString()).body();
        epic = gson.fromJson(s, Epic.class);
        subtask = new Subtask("a", "b", Statuses.NEW,
                null, 0, epic);
        URI urlSubtask = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest requestPostSubtask = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(subtask)))
                .uri(urlSubtask).build();
        client.send(requestPostSubtask, HttpResponse.BodyHandlers.ofString());
    }

    String getTask(TasksType type) throws IOException, InterruptedException {
        if (type.equals(TasksType.EPIC)) {
            URI urlEpic = URI.create("http://localhost:8080/tasks/epic?id=2");
            HttpRequest requestEpic = HttpRequest.newBuilder().GET().uri(urlEpic).build();
            String s = client.send(requestEpic, HttpResponse.BodyHandlers.ofString()).body();
            return s;
        } else {
            URI urlEpic = URI.create("http://localhost:8080/tasks/subtask?id=3");
            HttpRequest requestSub = HttpRequest.newBuilder().GET().uri(urlEpic).build();
            String s = client.send(requestSub, HttpResponse.BodyHandlers.ofString()).body();
            return s;
        }
    }
}