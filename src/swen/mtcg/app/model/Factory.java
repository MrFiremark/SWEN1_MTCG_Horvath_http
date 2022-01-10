package swen.mtcg.app.model;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Factory {

    private final Pack finishedPack = new Pack();
    private final Random r = new Random();
    private Collection collection = new Collection();

    public Pack generatePack (Collection collection) {
        this.collection = collection;
        for (int i = 0; i <= 4; i++) {
            int chance = r.nextInt(100) + 1;
            if (chance <= 3) {
                rarityList(Rarities.Legendary, i);
            } else if (chance <= 13) {
                rarityList(Rarities.Epic, i);
            } else if (chance <= 65) {
                rarityList(Rarities.Rare, i);
            } else {
                rarityList(Rarities.Common, i);
            }
        }
        return finishedPack;
    }

    public void rarityList(Enum<Rarities> raritiesEnum, int index){
        List<Card> rarityList = collection.getAllCards()
                .stream()
                .filter(c -> c.getRarity() == raritiesEnum)
                .collect(Collectors.toList());
        finishedPack.setPackCard(rarityList.get(r.nextInt(rarityList.size())), index);
        System.out.println("You got the Card: " + finishedPack.getPackCard(index).toString());
    }
}
