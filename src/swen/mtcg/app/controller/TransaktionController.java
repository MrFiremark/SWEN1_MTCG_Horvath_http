package swen.mtcg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import swen.mtcg.app.model.Card;
import swen.mtcg.app.model.Pack;
import swen.mtcg.app.model.User;
import swen.mtcg.app.service.UserService;
import swen.mtcg.repository.PackageRepository;
import swen.mtcg.repository.UserRepository;
import swen.mtcg.server.http.ContentType;
import swen.mtcg.server.http.HttpStatus;
import swen.mtcg.server.util.Request;
import swen.mtcg.server.util.Response;

import java.util.ArrayList;
import java.util.Collections;

public class TransaktionController extends Controller{

    private final UserService userService;
    private final PackageRepository packageRepository;
    private final UserRepository userRepository;

    public TransaktionController(UserService userService, PackageRepository packageRepository, UserRepository userRepository) {
        this.userService = userService;
        this.packageRepository = packageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod().equals("POST")) {

            String check = request.getAuthentication().substring(6).replace("-mtcgToken", "");
            if(userService.checkUserLoggedIn(check)) {
                return acquirePackage(check);
            }
        }

        return response(
                HttpStatus.NOT_FOUND,
                ContentType.HTML,
                "{ \"error\": \"Not Found\"}"
        );
    }

    public Response acquirePackage(String user){

        ObjectMapper objectMapper = new ObjectMapper();
        User buyer = userService.getUser(user);
        ArrayList<Card> stack = new ArrayList<>();
        Pack pack = new Pack();
        Card[] cards = new Card[5];

        //Only continue if the User has more Coins than the Pack costs
        if (buyer.getCoins() >= pack.getCost()) {
            //get Pack Class from DB and delete Pack from DB
            pack.setPackCards(packageRepository.buyPackage(cards));
            //deduct Coins from User
            buyer.setCoins(buyer.getCoins() - pack.getCost());
            //Copy Pack into ArrayList to update Stack from User Class and DB with it
            Collections.addAll(stack, pack.getPackCards());
            buyer.setStack(stack);
            userService.updateUser(buyer);
            userRepository.updateUserStack(buyer);
            //Update UserData with new Coins for persitency
            userRepository.updateUserData(buyer);
        }

        String contain = "";
        try {
            contain = objectMapper.writeValueAsString(buyer);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Response(contain);
    }

}
