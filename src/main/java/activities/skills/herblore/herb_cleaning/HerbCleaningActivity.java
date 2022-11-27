package activities.skills.herblore.herb_cleaning;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import util.Sleep;
import util.executable.Executable;
import util.item_requirement.ItemReq;

public class HerbCleaningActivity extends Activity {

    private final Herb herb;
    private final ItemReq herbReq;
    private final Executable bankNode;

    public HerbCleaningActivity(final Herb herb) {
        super(ActivityType.HERBLORE);
        this.herb = herb;
        this.herbReq = new ItemReq(herb.grimyName, 1);
        bankNode = new ItemReqBanking(this, herbReq);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (getInventory().contains(herb.grimyName)) {
            cleanHerbs();
        } else {
            execute(bankNode);
        }
    }

    private void cleanHerbs() throws InterruptedException {
        int choice = random(1, 10);
//        if (choice < 3) {
//            while(getInventory().contains(herb.grimyName)) {
//                getInventory().interact("Clean", herb.grimyName);
//                sleep(random(100, 200));
//            }
//        } else {
            if (getInventory().interact("Clean", herb.grimyName)) {
                Sleep.sleepUntil(() -> !getInventory().contains(herb.grimyName) ||
                        getDialogues().isPendingContinuation(), 40_000);
                sleep(random(1_000, 10_000));
            }
//        }
    }

    @Override
    public HerbCleaningActivity copy() {
        return new HerbCleaningActivity(herb);
    }
}
