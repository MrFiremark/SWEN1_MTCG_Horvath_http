package swen.mtcg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import swen.mtcg.app.model.*;
import swen.mtcg.app.service.UserService;
import swen.mtcg.repository.PackageRepository;
import swen.mtcg.server.http.ContentType;
import swen.mtcg.server.http.HttpStatus;
import swen.mtcg.server.util.Request;
import swen.mtcg.server.util.Response;


public class PackageController extends Controller{

    private final UserService userService;
    private final PackageRepository packageRepository;

    public PackageController(UserService userService, PackageRepository packageRepository) {
        this.userService = userService;
        this.packageRepository = packageRepository;
    }

    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod().equals("POST")) {
            String check = request.getAuthentication().substring(6).replace("-mtcgToken", "");
            if(request.getAuthentication().equals("Basic admin-mtcgToken") && userService.checkUserLoggedIn(check)) {
                return createPackage(request.getContent());
            }
        }

        return response(
                HttpStatus.NOT_FOUND,
                ContentType.HTML,
                "{ \"error\": \"Not Found\"}"
        );
    }

    public Response createPackage(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Pack pack = new Pack();
        String contain = "";
        try {
            JsonNode jsonNode = objectMapper.readTree(json);

            for (int i = 0; i < jsonNode.size(); i++){
                if(jsonNode.get(i).has("Monstertype")){
                    //String element = jsonNode.get(i).get("Element").textValue();
                    pack.setPackCard(
                            new Monster(
                                    jsonNode.get(i).get("Id").textValue(),
                                    jsonNode.get(i).get("Cardtype").textValue(),
                                    jsonNode.get(i).get("Name").textValue(),
                                    Elements.valueOf(jsonNode.get(i).get("Element").textValue()),
                                    Rarities.valueOf(jsonNode.get(i).get("Rarity").textValue()),
                                    jsonNode.get(i).get("Damage").asDouble(),
                                    jsonNode.get(i).get("Monstertype").textValue()
                            ), i
                    );
                }else {
                    pack.setPackCard(
                            new Spell(
                                    jsonNode.get(i).get("Id").textValue(),
                                    jsonNode.get(i).get("Cardtype").textValue(),
                                    jsonNode.get(i).get("Name").textValue(),
                                    Elements.valueOf(jsonNode.get(i).get("Element").textValue()),
                                    Rarities.valueOf(jsonNode.get(i).get("Rarity").textValue()),
                                    jsonNode.get(i).get("Damage").asDouble()
                            ), i
                    );
                }
            }
            packageRepository.createPackage(pack.getPackCards());
            contain = objectMapper.writeValueAsString(pack);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(contain);
    }
}
