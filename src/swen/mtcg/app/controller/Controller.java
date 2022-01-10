package swen.mtcg.app.controller;

import swen.mtcg.server.http.ContentType;
import swen.mtcg.server.http.HttpStatus;
import swen.mtcg.server.util.Request;
import swen.mtcg.server.util.Response;

public abstract class Controller {

    public abstract Response handleRequest(Request request);

    protected Response response(HttpStatus status, ContentType contentType, String content) {
        Response response = new Response();
        response.setStatus(status);
        response.setContentType(contentType);
        response.setContent(content);
        return response;
    }
}
