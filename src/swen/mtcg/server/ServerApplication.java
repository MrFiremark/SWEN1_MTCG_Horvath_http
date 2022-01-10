package swen.mtcg.server;

import swen.mtcg.server.util.Request;
import swen.mtcg.server.util.Response;

public interface ServerApplication {

    Response handleRequest(Request request);
}
