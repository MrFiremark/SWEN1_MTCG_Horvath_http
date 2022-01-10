package swen.mtcg.app.model;

import java.util.ArrayList;
import java.util.UUID;

public class Collection {

    ArrayList<Card> allCards = new ArrayList<>();

    public ArrayList<Card> getAllCards() {

        return allCards;
    }

    public void createCard(String id, String cardType, String name, Elements element, Rarities rarity, int damage, String monsterType){
        if(cardType.equals("Monster")){

            allCards.add(new Monster(id, cardType, name, element, rarity, damage, monsterType));

        }else if(cardType.equals("Spell")){

            allCards.add(new Spell(id, cardType, name, element, rarity, damage));

        }else{

            System.out.println("Wrong cardtype detected");
        }
    }
}
