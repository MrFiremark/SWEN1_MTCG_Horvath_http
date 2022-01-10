package swen.mtcg.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Spell extends Card {



    public Spell(String id,  String cardType, String name, Elements element, Rarities rarity, double damage) {
        super(id, cardType, name, element, rarity, damage);
    }

    @Override
    public String toString() {
        return "SPELL" + super.toString();
    }
}
