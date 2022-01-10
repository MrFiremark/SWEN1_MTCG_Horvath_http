package swen.mtcg.app;

import swen.mtcg.app.controller.*;
import swen.mtcg.app.service.BattleService;
import swen.mtcg.app.service.UserService;
import swen.mtcg.repository.PackageRepository;
import swen.mtcg.repository.UserRepository;
import swen.mtcg.server.ServerApplication;
import swen.mtcg.server.http.ContentType;
import swen.mtcg.server.http.HttpStatus;
import swen.mtcg.server.util.Request;
import swen.mtcg.server.util.Response;

public class MtcgAPI implements ServerApplication {

    private final UserController userController;
    private final UserService userService = new UserService();
    private final SessionController sessionController;
    private final PackageController packageController;
    private final TransaktionController transaktionController;
    private final CardController cardController;
    private final DeckController deckController;
    private final BattleController battleController;
    private final BattleService battleService = new BattleService();

    public MtcgAPI() {
        userController = new UserController(
                userService,
                new UserRepository()
        );
        sessionController = new SessionController(
                userService,
                new UserRepository()
        );
        packageController = new PackageController(
                userService,
                new PackageRepository()
        );
        transaktionController = new TransaktionController(
                userService,
                new PackageRepository(),
                new UserRepository()
        );
        cardController = new CardController(
                userService
        );
        deckController = new DeckController(
                userService,
                new UserRepository()
        );
        battleController = new BattleController(
                userService,
                battleService
        );
    }

    @Override
    public Response handleRequest(Request request) {

        if (request.getRoute().contains("/users")) {
            return userController.handleRequest(request);
        }
        if (request.getRoute().contains("/sessions")){
            return sessionController.handleRequest(request);
        }
        if (request.getRoute().contains("/transactions/packages")){
            return transaktionController.handleRequest(request);
        }
        if (request.getRoute().contains("/packages")){
            return packageController.handleRequest(request);
        }
        if (request.getRoute().contains("/cards")){
            return cardController.handleRequest(request);
        }
        if (request.getRoute().contains("/deck")){
            return deckController.handleRequest(request);
        }
        if (request.getRoute().contains("/stats")){

        }
        if (request.getRoute().contains("/score")){

        }
        if (request.getRoute().contains("/battle")){
            return battleController.handleRequest(request);
        }
        if (request.getRoute().contains("/tradings")){

        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setContentType(ContentType.JSON);
        response.setContent("{ \"error\": \"Not Found\"}");

        return response;
    }
}