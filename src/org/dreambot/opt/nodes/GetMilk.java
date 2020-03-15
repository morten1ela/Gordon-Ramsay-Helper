package org.dreambot.opt.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.opt.CooksAssitant;
import org.dreambot.opt.Node;

public class GetMilk extends Node {
    public GetMilk(CooksAssitant main) {
        super(main);
    }

    private static final Area COW_AREA = new Area(3253, 3270, 3255, 3275);
    private static final Area BASEMENT = new Area(3208, 9625, 3219, 9615);
    private static final Area COOK_ROOM = new Area(3205, 3215, 3212, 3212);

    @Override
    public boolean validate() {
        return !c.getInventory().contains("Bucket of milk");
    }

    @Override
    public int execute() {
        GameObject COW = c.getGameObjects().closest("Dairy cow");
        GameObject LADDER = c.getGameObjects().closest("Ladder");

        if(BASEMENT.contains(c.getLocalPlayer())){
            if(LADDER.interact("Climb-up")){
                sleepUntil(() -> COOK_ROOM.contains(c.getLocalPlayer()), 10000);
            }
        }

        if(c.getInventory().contains("Bucket") && !COW_AREA.contains(c.getLocalPlayer()) && !BASEMENT.contains(c.getLocalPlayer())){
            if(c.getWalking().walk(COW_AREA.getRandomTile())){
                sleep(Calculations.random(1700, 3450));
            }
        }

        if(COW_AREA.contains(c.getLocalPlayer()) && c.getInventory().contains("Bucket")){
            if(COW.interact("Milk")){
                sleepUntil(() -> c.getInventory().contains("Bucket of milk"), 8000);
            }
        }



        return 250;
    }
}
