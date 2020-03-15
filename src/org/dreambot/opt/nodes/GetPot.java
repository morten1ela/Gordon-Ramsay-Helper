package org.dreambot.opt.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.opt.CooksAssitant;
import org.dreambot.opt.Node;

public class GetPot extends Node {
    public GetPot(CooksAssitant main) {
        super(main);
    }

    private static final Area COOK_ROOM = new Area(3205, 3215, 3212, 3212);

    @Override
    public boolean validate() {
        return !c.getInventory().contains("Pot") && !c.getInventory().contains("Pot of flour");
    }

    @Override
    public int execute() {
        GroundItem POT = c.getGroundItems().closest("Pot");

        if(!c.getTabs().isOpen(Tab.INVENTORY)){
            if(c.getTabs().open(Tab.INVENTORY)){
                sleepUntil(() -> c.getTabs().isOpen(Tab.INVENTORY), 7000);
            }
        }

        if(!COOK_ROOM.contains(c.getLocalPlayer())){
            if(c.getWalking().walk(COOK_ROOM.getRandomTile())){
                sleep(Calculations.random(1200, 4250));
            }
        }

        if(POT != null && COOK_ROOM.contains(c.getLocalPlayer())){
            if(POT.interact("Take")){
                sleepUntil(() -> c.getInventory().contains("Pot"), 8000);
            }
        }

        return 250;
    }
}
