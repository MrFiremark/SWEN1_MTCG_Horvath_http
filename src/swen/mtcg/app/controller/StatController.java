package swen.mtcg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import swen.mtcg.app.model.User;
import swen.mtcg.app.service.UserService;
import swen.mtcg.server.http.ContentType;
import swen.mtcg.server.http.HttpStatus;
import swen.mtcg.server.util.Request;
import swen.mtcg.server.util.Response;

public class StatController extends Controller{

    private final UserService userService;;

    public StatController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod().equals("GET")) {
            String check = request.getAuthentication().substring(6).replace("-mtcgToken", "");
            if(userService.checkUserLoggedIn(check)) {
                return showStats(check);
            }
        }

        return response(
                HttpStatus.NOT_FOUND,
                ContentType.HTML,
                "{ \"error\": \"Not Found\"}"
        );
    }

    public Response showStats(String username){

        ObjectMapper objectMapper = new ObjectMapper();
        User user = userService.getUser(username);
        String contain = "";
        try {
            contain = objectMapper.writeValueAsString(user.getElo());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(contain);
    }
}
