package api;

import utility.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String apiKey;
    private final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
    private final HttpClient client = HttpClient.newHttpClient();
    private final URI url;

    public KVTaskClient(URI url) {
        this.url = url;
        apiKey = registerApiKey();
    }

    public void put(String key, String json) {
        HttpRequest httpRequest = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(url + "/save/" + key + "?API_KEY=" + apiKey))
                .header("Accept", "application/json")
                .build();
        try {
            client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Что-то пошло не так");
        }
    }

    public String load(String key) {
        HttpRequest httpRequest = requestBuilder.GET().uri(URI.create(url + "/load/" + key + "?API_KEY=" + apiKey))
                .header("Accept", "application/json")
                .build();
        String dataResponse = null;
        HttpResponse<String> httpResponse;
        try {
            httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                dataResponse = httpResponse.body();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Данных на сервере нет.");
        }
        return dataResponse;
    }

    public String registerApiKey() {
        HttpRequest httpRequest = requestBuilder.GET().uri(URI.create(url + "register"))
                .build();
        String dataResponse = null;

        try {
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                dataResponse = httpResponse.body();
            }
        } catch (IOException | InterruptedException e) {
            e.getMessage();
        }
        return dataResponse;
    }
}