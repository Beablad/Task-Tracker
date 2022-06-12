import api.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new KVServer().start();

        String a = "a,a";
        System.out.println(a.split(",")[0] + a.split(",")[1]);
    }
}
