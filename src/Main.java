import api.HttpTaskServer;
import api.KVServer;
import managers.HttpTaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();
        new HttpTaskServer();
        final HttpClient client = HttpClient.newHttpClient();

    }
}
