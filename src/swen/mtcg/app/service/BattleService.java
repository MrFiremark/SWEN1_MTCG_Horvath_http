package swen.mtcg.app.service;

import swen.mtcg.app.model.Battle;
import swen.mtcg.app.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BattleService {

    private List<Battle> activeBattle = new ArrayList<>();

    public void startBattle(Battle battle){
        activeBattle.add(battle);
    }

    public void stopBattle(Battle battle){
        activeBattle.remove(battle);
    }

    public Battle lookForActiveBattle(User user){

        for (Battle battle: activeBattle) {
            if(battle.getStatus()){
                battle.setContestant2(user);
                while (battle.getStatus()){
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                stopBattle(battle);
                return battle;
            }
            else {
                Battle newbattle = new Battle(user);
                startBattle(newbattle);
                waitingRoom(newbattle);
                while (newbattle.getStatus()){
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                stopBattle(newbattle);
                return battle;
            }
        }

        return null;
    }

    public void waitingRoom(Battle battle){

        long startmillis = System.currentTimeMillis();
        int seconds = 0;
        while(battle.getContestant2().getUsername().length() == 0 && seconds <= 180){

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            seconds = (int)((System.currentTimeMillis() - startmillis) /1000);

        }
        if (battle.getContestant2().getUsername().length() != 0){
            battle.fight();
        }
        battle.setStatus(false);
    }

    public Battle getBattle(User user){

        for (Battle battle: activeBattle) {
            if(battle.getContestant1().equals(user) || battle.getContestant2().equals(user)){
                return battle;
            }
        }

        return null;
    }
}
