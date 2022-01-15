package swen.mtcg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import swen.mtcg.app.service.UserService;
import swen.mtcg.repository.ScoreboardRepository;
import swen.mtcg.server.http.ContentType;
import swen.mtcg.server.http.HttpStatus;
import swen.mtcg.server.util.Request;
import swen.mtcg.server.util.Response;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardController extends Controller{

    private final UserService userService;
    private final ScoreboardRepository scoreboardRepository;

    public ScoreboardController(UserService userService,  ScoreboardRepository scoreboardRepository) {
        this.userService = userService;
        this.scoreboardRepository = scoreboardRepository;
    }

    public Response handleRequest(Request request) {

        String check = request.getAuthentication().substring(6).replace("-mtcgToken", "");

        if (userService.checkUserLoggedIn(check)) {
            if (request.getMethod().equals("GET")) {
                return getScoreboard(check);
            }
        }
        return response(
                HttpStatus.NOT_FOUND,
                ContentType.HTML,
                "{ \"error\": \"Not Found\"}"
        );
    }

    public Response getScoreboard(String username){

        ObjectMapper objectMapper = new ObjectMapper();
        String contain = "";

        List<String> scoreboard = new ArrayList<>(scoreboardRepository.getScoreboard(username));

        try {
            contain = objectMapper.writeValueAsString(scoreboard);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(contain);
    }
}
