package org.dreambot.opt;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.opt.nodes.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

import static org.dreambot.api.methods.quest.book.FreeQuest.COOKS_ASSISTANT;

@ScriptManifest(category = Category.QUEST, name = "CooksAssistant 2.0", author = "morten", version = 2.0, description ="The cake is a lie" )
public class CooksAssitant extends AbstractScript {
    private Node[] nodes;
    private static final Area COOK_ROOM = new Area(3205, 3217, 3211, 3212);
    private boolean got_all_items = false;

    private Image mainPaint = getImage("https://i.imgur.com/0XEPzrC.png");
    private Timer timeRan;

    @Override
    public void onStart(){
        nodes = new Node[]{
                new StartQuest(this),
                new GetPot(this),
                new GetBucket(this),
                new GetMilk(this),
                new GetEgg(this),
                new MakeFlour(this),
        };
        timeRan = new Timer();

    }

    public int onLoop(){
        if(getQuests().isFinished(COOKS_ASSISTANT)){
            onExit();
        }

        if((!getInventory().contains("Pot of flour")
                || !getInventory().contains("Egg")
                || !getInventory().contains("Bucket of milk"))
                && !got_all_items) {
            for (Node node : nodes) {
                if (node.validate()) {
                    return node.execute();
                }
            }
        }

        if(getInventory().contains("Pot of flour")
                && getInventory().contains("Egg")
                && getInventory().contains("Bucket of milk")
                && !getQuests().isFinished(COOKS_ASSISTANT))
        {
            got_all_items = true;
        }

        if(got_all_items){
            NPC COOK = getNpcs().closest("Cook");
            if(!getTabs().isOpen(Tab.QUEST)){
                if(getTabs().open(Tab.QUEST)){
                    sleepUntil(() -> getTabs().isOpen(Tab.QUEST), 7000);
                }
            }

            if(!COOK_ROOM.contains(getLocalPlayer())){
                if(getWalking().walk(COOK_ROOM.getCenter())){
                    sleep(Calculations.random(1484, 3481));
                }
            } else if(COOK != null && !getDialogues().inDialogue() && COOK.interact("Talk-to")){
                sleepUntil(() -> getDialogues().inDialogue(), 7000);
            } else if(getDialogues().inDialogue()){
                if(getDialogues().canContinue()){
                    getDialogues().spaceToContinue();
                }
            }
        }
        return 250;
    }

    public void onExit(){
        stop();
    }

    /**
     *
     */
    public void onPaint(Graphics2D g){
        Font font = new Font("Arial", Font.BOLD, 14);
        g.setFont(font);
        g.setColor(Color.BLACK);

        g.drawImage(mainPaint, 7, 345, null);

        g.drawString("Time Ran: " + timeRan.formatTime(), 60, 472);
    }

    private Image getImage(String url){
        try {
            return ImageIO.read(new URL(url));
        }catch (IOException e){
            return null;
        }
    }
}
