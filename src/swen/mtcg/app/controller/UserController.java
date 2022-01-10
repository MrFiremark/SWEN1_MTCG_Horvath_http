package swen.mtcg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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

        String check = request.getAuthentication().substring(6).replace("-mtcgToken", "");
        String userpath = request.getRoute().substring(7);

        if (request.getMethod().equals("POST")) {
            return addUser(request.getContent());
        }
        if (check.equals(userpath) && userService.checkUserLoggedIn(check)) {
            if (request.getMethod().equals("GET")) {
                return getUserData(check);
            }
            if (request.getMethod().equals("PUT")) {
                return editUserData(request.getContent(), check);
            }
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
            if (!userRepository.register(user.getUsername(), user.getPassword())){
                return new Response(HttpStatus.BAD_REQUEST, "User already exists");
            }

            System.out.println("New User Registererd");
            contain = objectMapper.writeValueAsString(user);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(contain);
    }

    public Response getUserData(String username){

        ObjectMapper objectMapper = new ObjectMapper();
        User user = userService.getUser(username);
        user = userRepository.readProfile(user);
        String contain = "";

        try {
            contain = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(contain);
    }

    public  Response editUserData(String json, String username){

        ObjectMapper objectMapper = new ObjectMapper();
        User user = userService.getUser(username);
        String contain = "";

        try {
            JsonNode jsonNode = objectMapper.readTree(json);

            user.setName(jsonNode.get("Name").textValue());
            user.setBio(jsonNode.get("Bio").textValue());
            user.setImage(jsonNode.get("Image").textValue());

            user = userRepository.editProfile(user);

            contain = objectMapper.writeValueAsString(user);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(contain);
    }
}