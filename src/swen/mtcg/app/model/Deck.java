package swen.mtcg.app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class Deck {

    private final int deckSize = 4;
    private ArrayList<Card> deckCards = new ArrayList<>();
    private ArrayList<Card> remainingStackCards;

    public Deck(){
    }

    public Deck(ArrayList<Card> deckCards) {
        this.deckCards = deckCards;
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

    public Card getRandomDeckCard(){
        Collections.shuffle(deckCards);
        return deckCards.get(0);
    }

    public void removeCard(Card card){
        deckCards.remove(card);
    }

}
