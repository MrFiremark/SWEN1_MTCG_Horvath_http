package swen.mtcg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import swen.mtcg.app.model.Battle;
import swen.mtcg.app.model.User;
import swen.mtcg.app.service.BattleService;
import swen.mtcg.app.service.UserService;
import swen.mtcg.repository.UserRepository;
import swen.mtcg.server.http.ContentType;
import swen.mtcg.server.http.HttpStatus;
import swen.mtcg.server.util.Request;
import swen.mtcg.server.util.Response;

import java.util.ArrayList;
import java.util.List;

public class BattleController extends Controller{

    private final UserService userService;
    private final BattleService battleService;

    public BattleController(UserService userService, BattleService battleService) {
        this.userService = userService;
        this.battleService = battleService;
    }

    public Response handleRequest(Request request) {

        String check = request.getAuthentication().substring(6).replace("-mtcgToken", "");

        if (request.getMethod().equals("POST")) {
            if(userService.checkUserLoggedIn(check)){
                return startBattle(check);
            }

        }

        return response(
                HttpStatus.NOT_FOUND,
                ContentType.HTML,
                "{ \"error\": \"Not Found\"}"
        );
    }

    public Response startBattle(String username){

        ObjectMapper objectMapper = new ObjectMapper();
        User user = userService.getUser(username);
        String contain = "";


        List<String> log = new ArrayList<>(battleService.lookForActiveBattle(user).getLog());

        try {
            contain = objectMapper.writeValueAsString(log);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(contain);
    }

}
