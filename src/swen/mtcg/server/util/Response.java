package swen.mtcg.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import swen.mtcg.server.http.ContentType;
import swen.mtcg.server.http.HttpStatus;

import java.lang.reflect.Type;

public class Response {

    private int status;
    private String message;
    private String contentType;
    private String content;

    public Response() {
        setStatus(HttpStatus.OK);
        contentType = "text/html";
        content = "<!DOCTYPE html><html><body><h1>OK</h1></body></html>";
    }

    public Response(String json){
        setStatus(HttpStatus.OK);
        contentType = "application/json";
        content = json;

    }

    public Response(HttpStatus httpStatus, String content){
        setStatus(httpStatus);
        contentType = "text/html";
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status.code;
        this.message = status.message;
    }

    public String getMessage() {
        return message;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType.type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "HTTP/1.1 " + status + " " + message + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + content.length() + "\r\n" +
                "\r\n" +
                content;
    }
}
