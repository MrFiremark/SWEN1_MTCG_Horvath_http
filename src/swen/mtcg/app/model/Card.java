package swen.mtcg.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public abstract class Card {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("Cardtype")
    private String cardType;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Element")
    private Elements element;
    @JsonProperty("Rarity")
    private Rarities rarity;
    @JsonProperty("Damage")
    private double damage;

    public Card(String id, String cardType, String name, Elements element, Rarities rarity, double damage) {
        this.id = id;
        this.cardType = cardType;
        this.name = name;
        this.element = element;
        this.rarity = rarity;
        this.damage = damage;
    }

    public Card(String name, Elements element, Rarities rarity, int damage) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.element = element;
        this.rarity = rarity;
        this.damage = damage;
    }

    public String getId() {
        return id;
    }

    public String getCardType() {
        return cardType;
    }

    public String getName() {
        return name;
    }

    public Elements getElement() {
        return element;
    }

    public Rarities getRarity() {
        return rarity;
    }

    public double getDamage() {
        return damage;
    }

    @Override
    public String toString() {
        return "[" +
                "name='" + name + '\'' +
                ", element=" + element +
                ", rarity=" + rarity +
                ", damage=" + damage +
                ']';
    }
}
