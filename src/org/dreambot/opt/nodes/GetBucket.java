package org.dreambot.opt.nodes;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.opt.CooksAssitant;
import org.dreambot.opt.Node;

public class GetBucket extends Node {
    public GetBucket(CooksAssitant main) {
        super(main);
    }

    private static final Area BASEMENT = new Area(3208, 9625, 3219, 9615);
    private static final Area COOK_ROOM = new Area(3205, 3217, 3211, 3212);

    @Override
    public boolean validate() {
        return !c.getInventory().contains("Bucket") && !c.getInventory().contains("Bucket of milk");
    }

    @Override
    public int execute() {
        GameObject TRAPDOOR = c.getGameObjects().closest("Trapdoor");
        GroundItem BUCKET = c.getGroundItems().closest("Bucket");

        if(!COOK_ROOM.contains(c.getLocalPlayer()) && !c.getInventory().contains("Bucket")){
            if(c.getWalking().walk(COOK_ROOM.getRandomTile())){
                sleep(1570, 3200);
            }
        } else if(COOK_ROOM.contains(c.getLocalPlayer()) && !c.getInventory().contains("Bucket")){
            if(TRAPDOOR.interact("Climb-down")){
                sleepUntil(() -> BASEMENT.contains(c.getLocalPlayer()), 7000);
            }
        }

        if(BUCKET != null && !c.getInventory().contains("Bucket")){
            if(BUCKET.interact("Take")){
                sleepUntil(() -> c.getInventory().contains("Bucket"), 10000);
            }
        }

        return 250;
    }
}
