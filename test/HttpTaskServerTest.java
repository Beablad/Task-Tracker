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

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    TaskManager tm;
    HttpTaskServer server;
    Gson gson = Managers.getDefaultGson();
    HttpClient client = HttpClient.newHttpClient();
    Task task = new Task("a", "b", Statuses.NEW, LocalDateTime.of(2022, 1, 1, 0, 0), 0);
    Epic epic = new Epic("a", "b");
    Subtask subtask = new Subtask("a", "b", Statuses.NEW,
            LocalDateTime.of(2022, 1, 1, 0, 0), 0, epic.getTaskId());

    @BeforeEach
    void start() throws IOException {
        server = new HttpTaskServer();
        tm = Managers.getDefault();
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    void addTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(TasksType.TASK)))
                .uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void addSubtask() throws IOException, InterruptedException {
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest requestPostEpic = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(TasksType.EPIC)))
                .uri(urlEpic).build();
        client.send(requestPostEpic, HttpResponse.BodyHandlers.ofString());
        URI urlSubtask = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest requestPostSubtask = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(TasksType.SUBTASK)))
                .uri(urlSubtask).build();
        HttpResponse<String> responseSubtask = client.send(requestPostSubtask, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseSubtask.statusCode());
    }


    @Test
    void addEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(TasksType.EPIC)))
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
        HttpResponse<String>  response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void clearTaskList () throws IOException, InterruptedException {
        init();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void removeTask() throws IOException, InterruptedException{
        init();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void removeEpic() throws IOException, InterruptedException{
        init();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void removeSubtask() throws IOException, InterruptedException{
        init();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }


    String toJson(TasksType type) {
        if (type == TasksType.TASK) {
            return gson.toJson(task);
        } else if (type == TasksType.EPIC) {
            return gson.toJson(epic);
        } else {
            return gson.toJson(subtask);
        }
    }


    void init() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(TasksType.TASK)))
                .uri(url).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        URI urlEpic = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest requestPostEpic = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(TasksType.EPIC)))
                .uri(urlEpic).build();
        client.send(requestPostEpic, HttpResponse.BodyHandlers.ofString());
        URI urlSubtask = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest requestPostSubtask = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(TasksType.SUBTASK)))
                .uri(urlSubtask).build();
        client.send(requestPostSubtask, HttpResponse.BodyHandlers.ofString());

    }
}