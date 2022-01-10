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

public class UserController extends Controller{

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public Response handleRequest(Request request) {

        if (request.getMethod().equals("POST")) {
            return addUser(request.getContent());
        }

        return response(
                HttpStatus.NOT_FOUND,
                ContentType.HTML,
                "{ \"error\": \"Not Found\"}"
        );
    }

    public Response addUser(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = null;
        String contain = "";
        try {
            user = objectMapper.readValue(json, User.class);
            userRepository.register(user.getUsername(), user.getPassword());
            System.out.println("New User Registererd");
            contain = objectMapper.writeValueAsString(user);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(contain);
    }
}