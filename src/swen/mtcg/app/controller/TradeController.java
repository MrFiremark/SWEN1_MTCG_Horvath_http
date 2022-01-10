package swen.mtcg.app.controller;

import swen.mtcg.app.service.UserService;
import swen.mtcg.repository.TradeRepository;
import swen.mtcg.repository.UserRepository;
import swen.mtcg.server.http.ContentType;
import swen.mtcg.server.http.HttpStatus;
import swen.mtcg.server.util.Request;
import swen.mtcg.server.util.Response;

public class TradeController extends Controller{

    private final UserService userService;
    private final TradeRepository tradeRepository;

    public TradeController(UserService userService, TradeRepository tradeRepository) {
        this.userService = userService;
        this.tradeRepository = tradeRepository;
    }

    public Response handleRequest(Request request) {

        String check = request.getAuthentication().substring(6).replace("-mtcgToken", "");
        String tradeid = request.getRoute().substring(9);

        if (userService.checkUserLoggedIn(check)) {

            if (request.getMethod().equals("POST")) {
                return createTrade(request.getContent());
            }

            if (request.getMethod().equals("GET")) {
                return getTrades();
            }
            if (request.getMethod().equals("DELETE")) {
                return deleteTrade(tradeid);
            }
        }

        return response(
                HttpStatus.NOT_FOUND,
                ContentType.HTML,
                "{ \"error\": \"Not Found\"}"
        );
    }

    public Response getTrades(){

        return new Response();
    }

    public Response createTrade(String json){
        return new Response();
    }

    public Response deleteTrade(String tradeid){
        return new Response();
    }

}
