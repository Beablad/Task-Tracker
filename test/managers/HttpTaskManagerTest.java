package managers;

import api.KVServer;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Statuses;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

class HttpTaskManagerTest {

    Gson gson = Managers.getDefaultGson();
    HttpTaskManager htm = new HttpTaskManager();
    Task task = new Task("a", "b", Statuses.NEW, LocalDateTime.now(), 10);
    HttpClient client = HttpClient.newHttpClient();

    void a() throws IOException, InterruptedException {
        new KVServer().start();
        String tasks = gson.toJson(List.of(task));
        HttpRequest requestTask =  HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8078/save/tasks?API_TOKEN=DEBUG")).
                POST(HttpRequest.BodyPublishers.ofString(tasks))
                .build();
        HttpResponse responseTask = client.send(requestTask, HttpResponse.BodyHandlers.ofString());
        System.out.println(responseTask.body());
    }

    @Test
    void recovery() throws IOException, InterruptedException {
        a();
        htm.recoveryData();
        System.out.println(htm.getTasks());
    }
}