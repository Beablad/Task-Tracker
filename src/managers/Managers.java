package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utility.LocalDateTimeAdapter;

import java.net.URI;
import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefault() {
        return new HttpTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager(){
        return new InMemoryHistoryManager();
    }

    public static Gson  getDefaultGson() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
    }
}

