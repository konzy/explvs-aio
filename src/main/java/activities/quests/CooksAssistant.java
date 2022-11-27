package activities.quests;

import activities.activity.Activity;
import activities.banking.DepositAllBanking;
import org.osbot.rs07.api.Quests;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Tab;
import util.Sleep;
import util.executable.ExecutionFailedException;

import java.util.stream.Stream;

public class CooksAssistant extends QuestActivity {

    private static final Area COOK_ROOM = new Area(3205, 3215, 3212, 3212);
    private static final Area BASEMENT = new Area(3214, 9625, 3216, 9623);
    private static final Area COW = new Area(3253, 3270, 3255, 3275);
    private static final Area CHICKEN = new Area(3235, 3295, 3226, 3300);
    private static final Area WHEAT = new Area(
            new int[][]{
                    { 3162, 3290 },
                    { 3157, 3295 },
                    { 3164, 3295 },
                    { 3164, 3292 }
            }
    );
    private static final Area BIN = new Area(
            new int[][]{
                    { 3166, 3303 },
                    { 3163, 3306 },
                    { 3163, 3308 },
                    { 3166, 3311 },
                    { 3168, 3311 },
                    { 3171, 3308 },
                    { 3171, 3306 },
                    { 3168, 3303 }
            }
    ).setPlane(0);
    private static final Area UPPER = new Area(
            new int[][]{
                    { 3166, 3303 },
                    { 3163, 3306 },
                    { 3163, 3308 },
                    { 3166, 3311 },
                    { 3168, 3311 },
                    { 3171, 3308 },
                    { 3171, 3306 },
                    { 3168, 3303 }
            }
    ).setPlane(2);
    private boolean haveAllItems = false;

    private static final int INVENTORY_SLOTS_REQUIRED = 7;

    private static final String[] ITEMS_NEEDED = {
            "Pot of flour",
            "Bucket of milk",
            "Egg"
    };
    private final DepositAllBanking depositAllBanking = new DepositAllBanking(ITEMS_NEEDED);
    private final DialogueCompleter cookDialogueCompleter = new DialogueCompleter(
            "Cook",
            COOK_ROOM,
            "What's wrong?",
            "I'm always happy to help a cook in distress.",
            "Actually, I know where to find this stuff."
    );
    private boolean operated = false;
    private boolean put = false;

    public CooksAssistant() {
        super(Quest.COOKS_ASSISTANT);
    }

    @Override
    public void onEnd() {
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (!getInventory().contains(ITEMS_NEEDED) && getInventory().getEmptySlotCount() < INVENTORY_SLOTS_REQUIRED) {
            execute(depositAllBanking);
        } else if (getTabs().getOpen() != Tab.INVENTORY) {
            getTabs().open(Tab.INVENTORY);
        } else {
            switch (getProgress()) {
                case 0:
                    execute(cookDialogueCompleter);
                    break;
                case 1:
                    if (hasRequiredItems() && !getQuests().isComplete(Quests.Quest.COOKS_ASSISTANT)) {
                        execute(cookDialogueCompleter);
                        sleep(random(1000, 2000));
                    } else {
                        getItemsNeeded();
                    }
                    break;
                case 2:
                    getWidgets().closeOpenInterface(); // quest complete
                    log("Quest is complete");
                    isComplete = true;
                    break;
                default:
                    throw new ExecutionFailedException("Unknown progress config value: " + getProgress());
            }
        }
    }

    private boolean hasRequiredItems() {
        if (!haveAllItems) {
            haveAllItems = Stream.of(ITEMS_NEEDED).allMatch(item -> getInventory().contains(item));
        }
        return haveAllItems;
    }

    private void getItemsNeeded() throws InterruptedException {
        if (!getInventory().contains("Pot", "Pot of flour")) {
            getGroundItem(COOK_ROOM, "Pot");
        } else if (!getInventory().contains("Bucket", "Bucket of milk")) {
            getGroundItem(BASEMENT, "Bucket");
        } else if (getInventory().contains("Bucket") && !getInventory().contains("Bucket of milk")) {
            getItemFromObject(COW, "Bucket of milk", "Dairy COW", "Milk");
        } else if (!getInventory().contains("Egg")) {
            getGroundItem(CHICKEN, "Egg");
        } else if (!getInventory().contains("Pot of flour")) {

            // Get grain
            if (!put && !getInventory().contains("Grain")) {
                setStatus("Getting Grain");
                getItemFromObject(WHEAT, "Grain", "Wheat", "Pick");
            }

            // Put grain
            if (!put && !operated && getInventory().contains("Grain")) {
                setStatus("Filling Hopper");
                fillHopper();
            }

            // Operate machine
            if (!operated && put) {
                setStatus("Operating Hopper");
                operateHopper();
            }

            // Get flour
            if (operated && put) {
                setStatus("Getting Flour");
                getItemFromObject(BIN.setPlane(0), "Pot of flour", "Flour BIN", "Empty");
            }
        }
    }

    private void fillHopper() {
        if (!UPPER.contains(myPosition())) {
            getWalking().webWalk(UPPER);
        } else if (!"Grain".equals(getInventory().getSelectedItemName())) {
            getInventory().use("Grain");
        } else {
            RS2Object hopper = getObjects().closest("Hopper");

            if (hopper == null) {
                throw new ExecutionFailedException("Could not find object 'Hopper'");
            }

            if (hopper.interact("Use")) {
                Sleep.sleepUntil(() -> myPlayer().isAnimating(), 5000);
                Sleep.sleepUntil(() -> !myPlayer().isAnimating(), 3000);
                put = true;
            }
        }
    }

    private void operateHopper() {
        if (!UPPER.contains(myPosition())) {
            getWalking().webWalk(UPPER);
        } else {
            RS2Object controls = getObjects().closest("Hopper controls");

            if (controls == null) {
                throw new ExecutionFailedException("Could not find object 'Hopper controls'");
            }

            if (controls.interact("Operate")) {
                logger.debug("Operated Hopper, waiting until we're animating");
                Sleep.sleepUntil(() -> myPlayer().isAnimating(), 5000);
                logger.debug("Operated Hopper, we're animating, waiting until we aren't");
                Sleep.sleepUntil(() -> !myPlayer().isAnimating(), 3000);
                logger.debug("Operated Hopper, Done");
                operated = true;
                put = true;
            }
        }
    }

    private void getItemFromObject(Area place, String itemName, String objectName, String interaction) throws InterruptedException {
        logger.debug("placeContainsMyPlayer=" + place.contains(myPlayer()));
        if (place.contains(myPlayer())) {
            RS2Object object = getObjects().closest(objectName);
            if (object != null && object.interact(interaction)) {
                Sleep.sleepUntil(() -> getInventory().contains(itemName) && !myPlayer().isAnimating(), 15000);
            }
        } else {
            logger.debug("trying to walk to place");
            boolean didWalk = getWalking().webWalk(place);
            logger.debug("didWalk=" + didWalk);

        }
    }

    private void getGroundItem(Area place, String itemName) {
        if (place.contains(myPosition())) {
            getGroundItems().take(itemName);
        } else {
            getWalking().webWalk(place);
        }
    }

    @Override
    public Activity copy() {
        return new CooksAssistant();
    }
}