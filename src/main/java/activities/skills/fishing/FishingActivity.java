package activities.skills.fishing;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.DepositAllBanking;
import activities.banking.ItemReqBanking;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import util.ResourceMode;
import util.Sleep;
import util.executable.Executable;
import util.item_requirement.ItemReq;

import java.util.*;

import static activities.skills.fishing.Fish.COOKED_FISH_FILTER;

public class FishingActivity extends Activity {

    private final Fish fish;
    private final FishingLocation location;
    private final ResourceMode resourceMode;
    private final ItemReq[] itemReqs;

    private NPC currentFishingSpot;
    private final Executable itemReqBankNode;
    private final Executable depositAllBankNode;

    public FishingActivity(final Fish fish, final FishingLocation location, final ResourceMode resourceMode) {
        super(ActivityType.FISHING);
        this.fish = fish;
        this.location = location;
        this.resourceMode = resourceMode;

        List<ItemReq> itemReqs = new ArrayList<>();
        Collections.addAll(itemReqs, fish.fishingMethod.itemReqs);

        if (location == FishingLocation.MUSA_POINT) {
            itemReqs.add(new ItemReq("Coins", 60, 5000).setStackable());
        }

        this.itemReqs = itemReqs.toArray(new ItemReq[0]);
        itemReqBankNode = new ItemReqBanking(this, this.itemReqs);
        depositAllBankNode = new DepositAllBanking(this.itemReqs);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (!ItemReq.hasItemRequirements(itemReqs, getInventory())
                || ItemReq.hasNonItemRequirement(itemReqs, getInventory(), Fish.RAW_FISH_FILTER)) {
            execute(itemReqBankNode);
        } else if (getInventory().isFull()) {
            logger.debug("Inventory is Full");
            if (resourceMode == ResourceMode.BANK) {
                logger.debug("Depositing at bank");
                execute(depositAllBankNode);
            } else if (resourceMode == ResourceMode.DROP) {
                RS2Object fire = getCookingObject();
                if(fire != null && myPlayer().getPosition().distance(fire) < 15) {
                    cook(fire);
                } else getInventory().dropAll(Fish.RAW_FISH_FILTER);
            }
        } else if (!location.location.getArea().contains(myPosition())) {
            getWalking().webWalk(location.location.getArea());
        } else if (!myPlayer().isInteracting(currentFishingSpot) || getDialogues().isPendingContinuation()) {
            fish();
        } else {
            int rand = random(0, 100);
            if (rand < 50) {
                sleep(random(5000, 15000));
            } else if (rand < 85) {
                sleep(random(25000, 50000));
            } else {
                sleep(random(50000, 150000));
            }
        }
    }

    private void cook(RS2Object fire) throws InterruptedException {
        logger.info("Cooking");
        Item rawItem = getInventory().getItem(Fish.RAW_FISH_FILTER);
        for (int i = 0; i < 30 && rawItem != null; i++) {
            rawItem = getInventory().getItem(Fish.RAW_FISH_FILTER);
            if (rawItem != null) {
                String rawItemName = rawItem.getName();
                if (getMakeAllInterface().isOpen()) {
                    getMakeAllInterface().makeAll(rawItemName);
                    Sleep.sleepUntil(() -> getDialogues().isPendingContinuation()  || !getInventory().contains(rawItemName), 90000);
                    getInventory().dropAll(COOKED_FISH_FILTER);
                } else if (!rawItemName.equals(getInventory().getSelectedItemName())) {
                    getInventory().use(rawItemName);
                    sleep(random(250, 500));
                } else if (fire.interact("Use")) {
                    Sleep.sleepUntil(() -> getMakeAllInterface().isOpen(), 7000);
                    sleep(random(1000, 2000));
                }
            }
        }
    }

    private RS2Object getCookingObject() {
        return getObjects().closestThatContains("Range", "Cooking range", "Stove", "Fire");
    }

    private void cookAll(RS2Object fire) throws InterruptedException {
        logger.info("cookAll");
        for (int i = 0; i < 5; i++) {
            Item rawItem = getInventory().getItem(Fish.RAW_FISH_FILTER);
            if (rawItem != null) {
                if (!fire.isVisible()) getWalking().walk(fire.getPosition());
                rawItem.interact("Use");
                Sleep.sleepUntil(() -> getInventory().isItemSelected(), 5000);
                sleep(random(500, 1000));
                fire.interact("Use");
                Sleep.sleepUntil(() -> getMakeAllInterface().isOpen(), 2500);

                getMakeAllInterface().makeAll(rawItem.getName());
                Sleep.sleepUntil(() -> myPlayer().isAnimating() || getDialogues().isPendingContinuation(), 60_000);
                sleep(random(500, 1000));
            }
        }
        getInventory().dropAll(COOKED_FISH_FILTER);

    }

    private void fish() {
        currentFishingSpot = getFishingSpot();
        if (currentFishingSpot != null && !myPlayer().isMoving()) {
            if (!currentFishingSpot.isVisible()) {
                getWalking().walk(currentFishingSpot);
            }
            if (!myPlayer().isMoving() && currentFishingSpot.interact(fish.fishingMethod.action)) {
                Sleep.sleepUntil(() -> myPlayer().isInteracting(currentFishingSpot) || !currentFishingSpot.exists(), random(5000, 10000));
            }
        }
    }

    private NPC getFishingSpot() {
        return getNpcs().closest(npc ->
                npc.getName().equals(fish.fishingMethod.spotName) &&
                        npc.hasAction(fish.fishingMethod.action) &&
                        map.canReach(npc) &&
                        location.location.getArea().contains(npc.getPosition())
        );
    }

    @Override
    public FishingActivity copy() {
        return new FishingActivity(fish, location, resourceMode);
    }
}
