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

public class DeckController extends Controller{

    private final UserService userService;
    private final UserRepository userRepository;

    public DeckController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public Response handleRequest(Request request) {

        String check = request.getAuthentication().substring(6).replace("-mtcgToken", "");

        if (request.getMethod().equals("PUT")) {
            if(userService.checkUserLoggedIn(check)) {
                return createDeck(check, request.getContent());
            }
        }
        if (request.getMethod().equals("GET")) {
            if(userService.checkUserLoggedIn(check)) {
                return showDeck(check);
            }
        }

        return response(
                HttpStatus.NOT_FOUND,
                ContentType.HTML,
                "{ \"error\": \"Not Found\"}"
        );
    }

    public Response createDeck(String username, String json){
        ObjectMapper objectMapper = new ObjectMapper();
        String contain = "";
        User user = userService.getUser(username);

        try {
            JsonNode jsonNode = objectMapper.readTree(json);

            if (jsonNode.size() == 4 && user.getDeck().getDeckLength() == 0) {
                for (int i = 0; i < jsonNode.size(); i++) {
                    //Check to see if the User has the Card in his Stack
                    if (user.checkStack(jsonNode.get(i).textValue())) {
                        if (jsonNode.size() == 4 && user.getDeck().getDeckLength() == 0) {
                            //Write the new Deck into the DB
                            userRepository.createDeck(user, jsonNode.get(i).textValue());
                        }else if (jsonNode.size() == 4 && user.getDeck().getDeckLength() == 4){
                            userRepository.updateDeck(user, jsonNode.get(i).textValue());
                        }
                    }
                }
            }
            //set the Deck for the temporary User by selecting the Deck from DB
            user.setDeck(userRepository.getDeck(user.getUserid()));
            //Update User Class in UserService with the temporary User from this method
            userService.updateUser(user);
            contain = objectMapper.writeValueAsString(user.getDeck());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(contain);
    }

    public Response showDeck(String username){
        return null;
    }
}
