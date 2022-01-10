package swen.mtcg.app.model;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Battle {

    private Boolean status = false;
    private User contestant1;
    private User contestant2;
    private Deck deckContestant1;
    private Deck deckContestant2;
    private int turnCounter = 0;
    private int maxTurns = 100;
    private List<String> log = new ArrayList<>();

    public Battle(User contestant1) {
        status = true;
        this.contestant1 = contestant1;
        this.log.add("Battle aborted...");
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public User getContestant1() {
        return contestant1;
    }

    public User getContestant2() {
        return contestant2;
    }

    public void setContestant2(User contestant2) {
        this.contestant2 = contestant2;
    }

    public List<String> getLog() {
        return log;
    }

    public void fight(){

        log.clear();

        deckContestant1 = new Deck(contestant1.getDeck().getDeckCards());
        deckContestant2 = new Deck(contestant2.getDeck().getDeckCards());

        while ((deckContestant1.getDeckLength() > 0 && deckContestant2.getDeckLength() > 0) && turnCounter <= maxTurns){
            log.add(cardBattle(deckContestant1.getRandomDeckCard(), deckContestant2.getRandomDeckCard()));
            turnCounter++;
        }

        if (turnCounter >= 100){
            log.add("DRAW!");
        }
        else if (deckContestant1.getDeckLength() == 0){
            log.add(contestant1.getUsername() + " wins!");
        }else{
            log.add(contestant2.getUsername() + " wins!");
        }

        for (String ausgabe: log) {
            System.out.println(ausgabe);
        }

        status = false;
    }

    public String cardBattle(Card card1, Card card2){

        StringBuilder turn = new StringBuilder();

        turn.append("Turn " + turnCounter + ":\n");
        turn.append(contestant1.getUsername());
        turn.append(": ");
        turn.append(card1.getName());
        turn.append(" (");
        turn.append(card1.getDamage());
        turn.append(" ) vs ");
        turn.append(contestant2.getUsername());
        turn.append(": ");
        turn.append(card2.getName());
        turn.append(" (");
        turn.append(card2.getDamage());
        turn.append(" ) => ");

        int specialty = specialty(card1, card2);
        if(specialty == 0) {

            if (card1 instanceof Monster && card2 instanceof Monster) {

                if (card1.getDamage() == card2.getDamage()) {

                    turn.append(" Draw (no action)! \n");

                } else if (card1.getDamage() > card2.getDamage()) {

                    turn.append(((Monster) card1).getMonsterType());
                    turn.append(" defeats ");
                    turn.append(((Monster) card2).getMonsterType());
                    turn.append("\n");

                    deckContestant1.addCard(card2);
                    deckContestant2.removeCard(card2);

                } else {

                    turn.append(((Monster) card2).getMonsterType());
                    turn.append(" defeats ");
                    turn.append(((Monster) card1).getMonsterType());
                    turn.append("\n");

                    deckContestant1.removeCard(card1);
                    deckContestant2.addCard(card1);

                }
            } else if (card1 instanceof Spell || card2 instanceof Spell) {

                turn.append(card1.getDamage());
                turn.append(" VS ");
                turn.append(card2.getDamage());
                turn.append(" -> ");

                double card1Multiplier = card1.getDamage() * effectiveness(card1.getElement(), card2.getElement());
                double card2Multiplier = card2.getDamage() * effectiveness(card2.getElement(), card1.getElement());

                turn.append(card1Multiplier);
                turn.append(" VS ");
                turn.append(card2Multiplier);
                turn.append("\n");

                if (card1Multiplier == card2Multiplier) {

                    turn.append(" Draw (no action) ");
                    turn.append("\n");

                } else if (card1Multiplier > card2Multiplier) {

                    turn.append(card1.getName());
                    turn.append(" wins\n");

                    deckContestant1.addCard(card2);
                    deckContestant2.removeCard(card2);

                } else {
                    turn.append(card2.getName());
                    turn.append(" wins\n");

                    deckContestant1.removeCard(card1);
                    deckContestant2.addCard(card1);
                }

            }
        }else{

            if (specialty == 1) {

                turn.append("Goblin is too afreid of the Dragon!");
                deckContestant1.removeCard(card1);
                deckContestant2.addCard(card1);
            }
            if (specialty == 2) {

                turn.append("Goblin is too afreid of the Dragon!");
                deckContestant1.addCard(card2);
                deckContestant2.removeCard(card2);
            }
            if (specialty == 3) {

                turn.append("The Wizard controls the Ork with his mindmagic");
                deckContestant1.addCard(card2);
                deckContestant2.removeCard(card2);
            }
            if (specialty == 4) {

                turn.append("The Wizard controls the Ork with his mindmagic");
                deckContestant1.removeCard(card1);
                deckContestant2.addCard(card1);
            }
            if (specialty == 5) {

                turn.append("The armor of Knights is so heavy that WaterSpells make them drown them instantly!");
                deckContestant1.removeCard(card1);
                deckContestant2.addCard(card1);
            }
            if (specialty == 6) {

                turn.append("The armor of Knights is so heavy that WaterSpells make them drown them instantly!");
                deckContestant1.addCard(card2);
                deckContestant2.removeCard(card2);
            }
            if (specialty == 7) {

                turn.append("The Kraken is immune to Spells!");
                deckContestant1.addCard(card2);
                deckContestant2.removeCard(card2);
            }
            if (specialty == 8) {

                turn.append("The Kraken is immune to Spells!");
                deckContestant1.removeCard(card1);
                deckContestant2.addCard(card1);
            }
            if (specialty == 9) {

                turn.append("The Elves studied the Dragons attack and evade them!");
                deckContestant1.addCard(card2);
                deckContestant2.removeCard(card2);
            }
            if (specialty == 10) {

                turn.append("The Elves studied the Dragons attack and evade them!");
                deckContestant1.removeCard(card1);
                deckContestant2.addCard(card1);
            }

        }

        return turn.toString();
    }

    public double effectiveness(Elements element1, Elements element2){

        double modifier = 1;

        if (element1.equals(element2)){
            return modifier;
        }
        if (element1.equals(Elements.Fire) && element2.equals(Elements.Water)){
            return modifier/2;
        }
        if (element1.equals(Elements.Water) && element2.equals(Elements.Fire)){
            return modifier*2;
        }
        if (element1.equals(Elements.Neutral) && element2.equals(Elements.Fire)){
            return modifier/2;
        }
        if (element1.equals(Elements.Fire) && element2.equals(Elements.Neutral)){
            return modifier*2;
        }
        if (element1.equals(Elements.Water) && element2.equals(Elements.Neutral)){
            return modifier/2;
        }
        if (element1.equals(Elements.Neutral) && element2.equals(Elements.Water)){
            return modifier*2;
        }

        return modifier;
    }

    public int specialty (Card card1, Card card2){

        if(card1 instanceof Monster && card2 instanceof Monster){
            if ((((Monster) card1).getMonsterType().equals("Goblin") && ((Monster) card2).getMonsterType().equals("Dragon"))){
                return 1;
            }
        }
        if(card1 instanceof Monster && card2 instanceof Monster){
            if(((Monster) card1).getMonsterType().equals("Dragon") && ((Monster) card2).getMonsterType().equals("Goblin")){
                return 2;
            }
        }
        if(card1 instanceof Monster && card2 instanceof Monster){
            if(((Monster) card1).getMonsterType().equals("Wizard") && ((Monster) card2).getMonsterType().equals("Ork")){
                return 3;
            }
        }
        if(card1 instanceof Monster && card2 instanceof Monster){
            if(((Monster) card1).getMonsterType().equals("Ork") && ((Monster) card2).getMonsterType().equals("Wizard")){
                return 4;
            }
        }
        if(card1 instanceof Monster && card2 instanceof Spell) {
            if (((Monster) card1).getMonsterType().equals("Knight") && (card2.getCardType().equals("Spell") && card2.getElement().equals(Elements.Water))) {
                return 5;
            }
        }
        if(card1 instanceof Spell && card2 instanceof Monster) {
            if ((card1.getCardType().equals("Spell") && card2.getElement().equals(Elements.Water)) && ((Monster) card2).getMonsterType().equals("Knight")) {
                return 6;
            }
        }
        if(card1 instanceof Monster && card2 instanceof Spell) {
            if (((Monster) card1).getMonsterType().equals("Kraken") && card2.getCardType().equals("Spell")) {
                return 7;
            }
        }
        if(card1 instanceof Spell && card2 instanceof Monster) {
            if (card1.getCardType().equals("Spell") && ((Monster) card2).getMonsterType().equals("Kraken")) {
                return 8;
            }
        }
        if(card1 instanceof Monster && card2 instanceof Monster) {
            if (((Monster) card1).getMonsterType().equals("Elf") && ((Monster) card2).getMonsterType().equals("Dragon")) {
                return 9;
            }
        }
        if(card1 instanceof Monster && card2 instanceof Monster) {
            if (((Monster) card1).getMonsterType().equals("Dragon") && ((Monster) card2).getMonsterType().equals("Elf")) {
                return 10;
            }
        }

        return 0;
    }

    public void eloCalc(User won, User lost){}
}
