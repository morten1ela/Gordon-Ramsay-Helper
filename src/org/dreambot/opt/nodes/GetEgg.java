package org.dreambot.opt.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.opt.CooksAssitant;
import org.dreambot.opt.Node;

public class GetEgg extends Node {
    public GetEgg(CooksAssitant main) {
        super(main);
    }

    private static final Area EGG_AREA = new Area(3235, 3295, 3226, 3300);

    @Override
    public boolean validate() {
        return !c.getInventory().contains("Egg");
    }

    @Override
    public int execute() {
        GroundItem EGG = c.getGroundItems().closest("Egg");

        if(!EGG_AREA.contains(c.getLocalPlayer())){
            c.getWalking().walk(EGG_AREA.getRandomTile());
            sleep(Calculations.random(1350, 2250));
        } else if(EGG_AREA.contains(c.getLocalPlayer()) && !c.getInventory().contains("Egg")){
            if(EGG != null && EGG.interact("Take")){
                sleepUntil(() -> c.getInventory().contains("Egg"), 6000);
            }
        }

        return 250;
    }
}
