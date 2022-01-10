package swen.mtcg.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class Deck {

    private final int deckSize = 4;
    private ArrayList<Card> deckCards = new ArrayList<>();

    public Deck(){
    }

    public Deck(ArrayList<Card> deckCards) {
        this.deckCards = new ArrayList<>();
        this.deckCards.addAll(deckCards);
    }

    public ArrayList<Card> getDeckCards() {
        return deckCards;
    }

    public void setDeckCards(ArrayList<Card> deckCards) {
        this.deckCards = deckCards;
    }

    public int getDeckLength(){
        return deckCards.size();
    }

    public void addCard(Card card){
            deckCards.add(card);
    }

    @JsonIgnore
    public Card getRandomDeckCard(){
        Collections.shuffle(deckCards);
        return deckCards.get(0);
    }

    @JsonIgnore
    public void removeCard(Card card){
        deckCards.remove(card);
    }

}
