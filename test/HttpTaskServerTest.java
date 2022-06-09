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

    HttpTaskServerTest() throws IOException {

    }

    @BeforeEach
    void start() throws IOException {
        server = new HttpTaskServer();
        tm = Managers.getDefault();
    }

    @AfterEach
    void stop() throws IOException {
        server.stop();
    }

    @Test
    void addTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(TasksType.TASK)))
                .uri(url).build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void addSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(TasksType.SUBTASK)))
                .uri(url).build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void addEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toJson(TasksType.EPIC)))
                .uri(url).build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }



    String toJson(TasksType type) {
        if (type == TasksType.TASK) {
            Task task = new Task("a", "b", Statuses.NEW, LocalDateTime.now(), 0);
            return gson.toJson(task);
        } else if (type == TasksType.EPIC) {
            Epic epic = new Epic("a", "b");
            return gson.toJson(epic);
        } else {
            Epic epic = new Epic("a", "b");
            tm.addEpic(epic);
                Subtask subtask = new Subtask("a", "b", Statuses.NEW, LocalDateTime.now(), 0
                );
            System.out.println(subtask);
            return gson.toJson(subtask);
        }
    }

    @Test
    void getTasks() {
        init();
        URI url = URI.create("http://localhost:8080/tasks/task/");

    }

    void init() {
        tm.addTask(new Task("a", "b", Statuses.NEW, LocalDateTime.of(2022, 1, 1, 0, 0), 0));
        Epic epic = new Epic("a", "b");
        tm.addEpic(epic);
        System.out.println(epic);
        tm.addSubtask(new Subtask("a", "b", Statuses.NEW,
                LocalDateTime.of(2022, 1, 1, 0, 0), 0), );

    }
}