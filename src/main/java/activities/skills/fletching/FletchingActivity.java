package activities.skills.fletching;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import util.Sleep;
import util.executable.Executable;
import util.item_requirement.ItemReq;


public class FletchingActivity extends Activity {

    private final FletchItem fletchItem;
    private final Executable bankNode;

    public FletchingActivity(final FletchItem fletchItem) {
        super(ActivityType.FLETCHING);
        this.fletchItem = fletchItem;
        bankNode = new ItemReqBanking(this, fletchItem.itemReqs);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (canMake()) {
            makeAllItem(fletchItem.itemReqs[1].toString(), fletchItem.itemReqs[0].toString());
        } else {
            execute(bankNode);
        }
    }

    private boolean canMake() {
        return ItemReq.hasItemRequirements(fletchItem.itemReqs, getInventory());
    }

    private void makeAllItem(String useItem, String interactItem) throws InterruptedException {
        if (getMakeAllInterface().isOpen()) {
            sleep(random(250, 500));
            getMakeAllInterface().makeAll(fletchItem.widgetText);
            Sleep.sleepUntil(() -> !canMake() || getDialogues().isPendingContinuation(), 120_000);
        } else if (!useItem.equals(getInventory().getSelectedItemName())) {
            getInventory().use(useItem);
            sleep(random(250, 500));
        } else if (getInventory().getItem(interactItem).interact()) {
            if (fletchItem.type != FletchItemType.DART) {
                Sleep.sleepUntil(() -> getMakeAllInterface().isOpen(), 3000);
                sleep(random(250, 500));
            }
        }
    }

    @Override
    public FletchingActivity copy() {
        return new FletchingActivity(fletchItem);
    }
}
