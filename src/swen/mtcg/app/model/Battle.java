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

    public void waitingRoom(){

        long startmillis = System.currentTimeMillis();
        int seconds = 0;
        while(contestant2.getUsername().length() == 0 && seconds <= 180){

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            seconds = (int)((System.currentTimeMillis() - startmillis) /1000);

        }
        if (contestant2.getUsername().length() != 0){
            fight();
        }
    }

    public void fight(){

        log.clear();

        deckContestant1 = contestant1.getDeck();
        deckContestant2 = contestant2.getDeck();

        while ((deckContestant1.getDeckLength() > 1 && deckContestant2.getDeckLength() > 1) || maxTurns == turnCounter){
            log.add(cardBattle(deckContestant1.getRandomDeckCard(), deckContestant2.getRandomDeckCard()));

            turnCounter++;
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

        if(specialty(card1, card2) == 0) {

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

            switch (specialty(card1, card2)){
                case 1:

                    turn.append("Goblin is too afreid of the Dragon!");
                    deckContestant1.removeCard(card1);
                    deckContestant2.addCard(card1);

                case 2:

                    turn.append("Goblin is too afreid of the Dragon!");
                    deckContestant1.addCard(card2);
                    deckContestant2.removeCard(card2);

                case 3:

                    turn.append("The Wizard controls the Ork with his mindmagic");
                    deckContestant1.addCard(card2);
                    deckContestant2.removeCard(card2);

                case 4:

                    turn.append("The Wizard controls the Ork with his mindmagic");
                    deckContestant1.removeCard(card1);
                    deckContestant2.addCard(card1);

                case 5:

                    turn.append("The armor of Knights is so heavy that WaterSpells make them drown them instantly!");
                    deckContestant1.removeCard(card1);
                    deckContestant2.addCard(card1);

                case 6:

                    turn.append("The armor of Knights is so heavy that WaterSpells make them drown them instantly!");
                    deckContestant1.addCard(card2);
                    deckContestant2.removeCard(card2);

                case 7:

                    turn.append("The Kraken is immune to Spells!");
                    deckContestant1.addCard(card2);
                    deckContestant2.removeCard(card2);

                case 8:

                    turn.append("The Kraken is immune to Spells!");
                    deckContestant1.removeCard(card1);
                    deckContestant2.addCard(card1);

                case 9:

                    turn.append("The Elves studied the Dragons attack and evade them!");
                    deckContestant1.addCard(card2);
                    deckContestant2.removeCard(card2);

                case 10:

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

        if(((Monster) card1).getMonsterType().equals("Goblin") && ((Monster) card2).getMonsterType().equals("Dragon")){
            return 1;
        }
        if(((Monster) card1).getMonsterType().equals("Dragon") && ((Monster) card2).getMonsterType().equals("Goblin")){
            return 2;
        }
        if(((Monster) card1).getMonsterType().equals("Wizard") && ((Monster) card2).getMonsterType().equals("Ork")){
            return 3;
        }
        if(((Monster) card1).getMonsterType().equals("Ork") && ((Monster) card2).getMonsterType().equals("Wizard")){
            return 4;
        }
        if(((Monster) card1).getMonsterType().equals("Knight") && (card2.getCardType().equals("Spell") && card2.getElement().equals(Elements.Water))){
            return 5;
        }
        if((card1.getCardType().equals("Spell") && card2.getElement().equals(Elements.Water)) && ((Monster) card2).getMonsterType().equals("Knight")){
            return 6;
        }
        if(((Monster) card1).getMonsterType().equals("Kraken") && card2.getCardType().equals("Spell")){
            return 7;
        }
        if(card1.getCardType().equals("Spell") && ((Monster) card2).getMonsterType().equals("Kraken")){
            return 8;
        }
        if(((Monster) card1).getMonsterType().equals("Elf") && ((Monster) card2).getMonsterType().equals("Dragon")){
            return 9;
        }
        if(((Monster) card1).getMonsterType().equals("Dragon") && ((Monster) card2).getMonsterType().equals("Elf")){
            return 10;
        }

        return 0;
    }

    public void eloCalc(User won, User lost){}
}
