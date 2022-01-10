package swen.mtcg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import swen.mtcg.app.model.User;
import swen.mtcg.app.service.UserService;
import swen.mtcg.repository.UserRepository;
import swen.mtcg.server.http.ContentType;
import swen.mtcg.server.http.HttpStatus;
import swen.mtcg.server.util.Request;
import swen.mtcg.server.util.Response;

public class SessionController extends Controller{

    private ObjectMapper objectMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    public SessionController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod().equals("POST")) {
                return loginUser(request.getContent());
        }

        return response(
                HttpStatus.NOT_FOUND,
                ContentType.HTML,
                "{ \"error\": \"Not Found\"}"
        );
    }

    public Response loginUser(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = null;
        String contain = "";
        try {
            user = objectMapper.readValue(json, User.class);
            user = userRepository.login(user.getUsername(), user.getPassword());
            contain = objectMapper.writeValueAsString(user);
            userService.addUser(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(contain);
    }
}
