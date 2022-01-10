package swen.mtcg;

import swen.mtcg.app.MtcgAPI;
import swen.mtcg.server.util.RestServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        RestServer server = new RestServer(new MtcgAPI());
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
