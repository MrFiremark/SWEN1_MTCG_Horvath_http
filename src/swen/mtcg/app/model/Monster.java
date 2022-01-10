package swen.mtcg.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Monster extends Card {

    @JsonProperty("Monstertype")
    private String monsterType;

    public Monster(String id, String name, String cardType, Elements element, Rarities rarity, double damage, String monsterType) {
        super(id, cardType, name, element, rarity, damage);
        this.monsterType = monsterType;
    }

    public String getMonsterType() {
        return monsterType;
    }

    @Override
    public String toString() {
        return "MONSTER" + super.toString().substring(0,super.toString().length()-1) +
                ", type='" + monsterType + '\'' +
                ']';
    }
}
