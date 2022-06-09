package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utility.LocalDateTimeAdapter;

import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager(){
        return new InMemoryHistoryManager();
    }

    public static Gson  getDefaultGson() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
    }
}

