package swen.mtcg.app.model;

public class Pack {

    private int cost = 5;
    private Card[] packCards = new Card[5];

    public Pack() {}

    public int getCost() {
        return cost;
    }

    public Card[] getPackCards() {
        return packCards;
    }

    public Card getPackCard(int index) {
        return packCards[index];
    }

    public void setPackCard(Card card, int index) {
        this.packCards[index] = card;
    }

    public void setPackCards(Card[] packCards) {
        this.packCards = packCards;
    }
}
