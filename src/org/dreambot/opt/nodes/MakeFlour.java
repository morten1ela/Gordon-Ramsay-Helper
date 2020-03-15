package org.dreambot.opt.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.opt.CooksAssitant;
import org.dreambot.opt.Node;

public class MakeFlour extends Node {
    public MakeFlour(CooksAssitant main) {
        super(main);
    }

    private static final Area UPPER = new Area(3167, 3305, 3165, 3308, 2);
    private static final Area BIN = new Area(3165, 3305, 3168, 3308);
    private static final Area GRAIN_AREA = new Area(3162, 3295, 3157, 3298);
    private static final Area MILL_DOOR = new Area(3164, 3303, 3169, 3300);
    private boolean put = false;
    private boolean operated = false;
    private boolean got_grain = false;
    private boolean door_open = false;

    @Override
    public boolean validate() {
        return !c.getInventory().contains("Pot of flour");
    }

    @Override
    public int execute() {
        if(!c.getInventory().contains("Grain")
                && !c.getInventory().contains("Pot of flour")
                && !got_grain){
            getGrain();
        }

        if(!door_open && got_grain){
            checkMillDoor();
        }

        if(!put && door_open) {
            fillHopper();
        }

        if(!operated && put){
            operateHopper();
        }

        if(put && operated){
            if(BIN.contains(c.getLocalPlayer())){
                getItemFromObject(BIN, "Flour bin", "Empty");
            } else if(!BIN.contains(c.getLocalPlayer())){
                if(c.getWalking().walk(BIN.getRandomTile())){
                    sleep(Calculations.random(1500, 3210));
                }
            }
        }

        return 250;
    }


    private void getGrain(){
        GameObject WHEAT = c.getGameObjects().closest("Wheat");

        if(!GRAIN_AREA.contains(c.getLocalPlayer())){
            if(c.getWalking().walk(GRAIN_AREA.getRandomTile())){
                sleep(Calculations.random(1484, 3750));
            }
        }

        if(GRAIN_AREA.contains(c.getLocalPlayer()) && !c.getInventory().contains("Grain")){
            if(WHEAT.interact("Pick")){
                sleepUntil(() -> c.getInventory().contains("Grain"), 8000);
                if(c.getInventory().contains("Grain")){
                    got_grain = true;
                }
            }
        }
    }

    private void fillHopper(){
        GameObject HOPPER = c.getGameObjects().closest("Hopper");
        if(!UPPER.contains(c.getLocalPlayer())){
            if(c.getWalking().walk(UPPER.getRandomTile())){
                sleep(1450, 2900);
            }
        } else if(HOPPER != null){
            if(HOPPER.interact("Fill")){
                sleepUntil(() -> !c.getInventory().contains("Grain"), 10000);
                sleep(1000);
                put = true;
            }
        } else {
            log("Could not find game object 'Hopper'");
        }

    }

    private void operateHopper(){
        GameObject CONTROLS = c.getGameObjects().closest("Hopper Controls");
        if(!UPPER.contains(c.getLocalPlayer())){
            if(c.getWalking().walk(UPPER.getRandomTile())){
                sleep(1450, 2900);
            }
        } else if(CONTROLS != null){
            if(CONTROLS.interact("Operate")){
                sleepUntil(() -> c.getLocalPlayer().isAnimating(), 10000);
                sleep(1000);
                operated = true;
            }
        } else {
            log("Could not find game object 'Controls'");
        }
    }

    private void getItemFromObject(Area place, String objectName, String action){
        GameObject OBJECT = c.getGameObjects().closest(objectName);
        if(!place.contains(c.getLocalPlayer())){
            if(c.getWalking().walk(place.getRandomTile())){
                sleep(Calculations.random(1484, 3464));
            }
        } else if(objectName != null){
            if(OBJECT.interact(action)){
                sleepUntil(() -> c.getLocalPlayer().isAnimating(), 7000);
            }
        }
    }

    private void checkMillDoor(){
        GameObject LARGE_DOOR = c.getGameObjects().closest("Large door");
        if(MILL_DOOR.contains(c.getLocalPlayer())) {
            if (LARGE_DOOR != null && LARGE_DOOR.interact("Open")) {
                sleepUntil(() -> LARGE_DOOR.hasAction("Close"), 7000);
                if(LARGE_DOOR.hasAction("Close")){
                    door_open = true;
                }
            } else if(LARGE_DOOR.hasAction("Close")){
                door_open = true;
            }
        } else {
            if(c.getWalking().walk(MILL_DOOR.getRandomTile())){
                sleep(Calculations.random(1750, 2510));
            }
        }
    }
}
