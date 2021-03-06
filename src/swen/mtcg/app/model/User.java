package swen.mtcg.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class User {

    @JsonIgnore
    private String userid;
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Password")
    private String password;
    //@JsonIgnore
    @JsonProperty("Coins")
    private int coins;
    @JsonProperty("ELO")
    private int elo;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Bio")
    private String bio;
    @JsonProperty("Image")
    private String image;
    @JsonProperty("Stack")
    private  ArrayList<Card> stack;
    @JsonIgnore
    private Deck deck;

    public User(){}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, int coins, int elo, ArrayList<Card> stack, Deck deck) {
        this.username = username;
        this.coins = coins;
        this.elo = elo;
        this.stack = stack;
        this.deck = deck;
    }

    public User(String id, String username, int coins, int elo) {
        this.userid = id;
        this.username = username;
        this.coins = coins;
        this.elo = elo;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getElo() {
        return elo;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStack(ArrayList<Card> stack) {
        this.stack = stack;
    }

    public void updateStack(Card[] newCards){
        Collections.addAll(stack, newCards);
    }

    public ArrayList<Card> getStack() {
        return stack;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Deck getDeck() {
        return deck;
    }

    public Boolean checkStack(String cardId){
        for (Card c: stack) {
            if (c.getId().equals(cardId)) {
                return true;
            }
        }
        return false;
    }
}