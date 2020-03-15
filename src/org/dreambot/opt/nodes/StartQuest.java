package org.dreambot.opt.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.opt.CooksAssitant;
import org.dreambot.opt.Node;

import static org.dreambot.api.methods.quest.book.FreeQuest.COOKS_ASSISTANT;

public class StartQuest extends Node {
    public StartQuest(CooksAssitant main) {
        super(main);
    }

    private static final Area COOK_ROOM = new Area(3205, 3217, 3211, 3212);

    @Override
    public boolean validate() {
        return !c.getQuests().isStarted(COOKS_ASSISTANT);
    }

    @Override
    public int execute() {
        NPC COOK = c.getNpcs().closest("Cook");
        log("Start Quest Node");

        if(!c.getTabs().isOpen(Tab.QUEST)){
            if(c.getTabs().open(Tab.QUEST)){
                sleepUntil(() -> c.getTabs().isOpen(Tab.QUEST), 7000);
            }
        }

        if(COOK_ROOM.contains(c.getLocalPlayer())){
            completeDialogue("Cook", dialogueOptions);
        } else if(!COOK_ROOM.contains(c.getLocalPlayer())){
            if(c.getWalking().walk(COOK_ROOM.getRandomTile())){
                sleep(Calculations.random(1590, 3460));
            }
        }

        return 250;
    }


    public final String[] dialogueOptions = {
            "What's wrong?",
            "I'm always happy to help a cook in distress.",
            "Actually, I know where to find this stuff.",
    };

    public void completeDialogue(final String npcName, final String... options){
        if(!c.getDialogues().inDialogue() && npcName != null){
            talkTo(npcName);
        } else if (c.getDialogues().canContinue()){
            c.getDialogues().continueDialogue();
        } else if (options.length > 0 && c.getDialogues().getOptions() != null){
            for(int i = 0; i <= options.length; i++) {
                c.getDialogues().chooseOption(options[i]);
            }
        }
    }

    private void talkTo(String npcName){
        NPC npc = c.getNpcs().closest(npcName);
        if(npc != null){
            npc.interact("Talk-to");
            sleepUntil(() -> c.getDialogues().inDialogue(), 5000);
        }
    }

}
