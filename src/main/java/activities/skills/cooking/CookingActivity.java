package activities.skills.cooking;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.utility.Condition;
import util.Sleep;
import util.executable.Executable;
import util.item_requirement.ItemReq;

public class CookingActivity extends Activity {

    private final CookingItem cookingItem;
    private final CookingLocation cookingLocation;
    private final Executable bankNode;
    private Condition condition = new Condition() {
        @Override
        public boolean evaluate() {
            return getInteractionHelper().canInteract(getCookingObject());
        }
    };

    public CookingActivity(final CookingItem cookingItem, final CookingLocation cookingLocation) {
        super(ActivityType.COOKING);
        this.cookingItem = cookingItem;
        this.cookingLocation = cookingLocation;
        bankNode = new ItemReqBanking(this, cookingItem.itemReqs);
    }

    @Override
    public void runActivity() throws InterruptedException {
        logger.debug("Starting Cooking Activity");
        if (ItemReq.hasItemRequirements(cookingItem.itemReqs, getInventory())) {
            if (runTo(cookingLocation.location.getArea(), condition)) {
                cook();
            } else {
                isComplete = true;
            }
        } else {
            execute(bankNode);
        }
    }

    private void cook() throws InterruptedException {
        if (getMakeAllInterface().isOpen()) {
            getMakeAllInterface().makeAll(cookingItem.name);
//            getMakeAllInterface().makeAll(1);
            Sleep.sleepUntil(() -> getDialogues().isPendingContinuation() || !ItemReq.hasItemRequirements(cookingItem.itemReqs, getInventory()), 90000);
        } else if (!cookingItem.toString().equals(getInventory().getSelectedItemName())) {
            getInventory().use(cookingItem.toString());
        } else if (getCookingObject().interact("Use")) {
            Sleep.sleepUntil(() -> getMakeAllInterface().isOpen(), 2000);
        }
    }

    private RS2Object getCookingObject() {
        return getObjects().closestThatContains("Range", "Cooking range", "Stove", "Fire");
    }

    @Override
    public CookingActivity copy() {
        return new CookingActivity(cookingItem, cookingLocation);
    }
}
