package swen.mtcg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import swen.mtcg.app.service.UserService;
import swen.mtcg.repository.ScoreboardRepository;
import swen.mtcg.server.http.ContentType;
import swen.mtcg.server.http.HttpStatus;
import swen.mtcg.server.util.Request;
import swen.mtcg.server.util.Response;

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
                return getScoreboard();
            }
        }
        return response(
                HttpStatus.NOT_FOUND,
                ContentType.HTML,
                "{ \"error\": \"Not Found\"}"
        );
    }

    public Response getScoreboard(){

        ObjectMapper objectMapper = new ObjectMapper();
        String contain = "";

        try {
            objectMapper.writeValueAsString(scoreboardRepository.getScoreboard().toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(contain);
    }
}
