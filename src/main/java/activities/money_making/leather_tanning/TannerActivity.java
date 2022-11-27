package activities.money_making.leather_tanning;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import activities.skills.crafting.CraftingItem;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.utility.Condition;
import util.Location;
import util.Sleep;
import util.executable.Executable;
import util.item_requirement.ItemReq;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TannerActivity extends Activity {

    Area tannerArea = new Area(3270, 3189, 3277, 3194);
    String tannerName = "Ellis";
    private final Executable bankNode;
    Leather leather;
    private Condition condition = new Condition() {
        @Override
        public boolean evaluate() {
            return tannerArea.contains(myPlayer());
        }
    };
    private int rootId = 324;

    public TannerActivity() {
        super(ActivityType.MONEY_MAKING);
        bankNode = new ItemReqBanking(this, leather.itemReqs);
    }

    public TannerActivity(final Leather leather) {
        super(ActivityType.MONEY_MAKING);
        this.leather = leather;
        bankNode = new ItemReqBanking(this, leather.itemReqs);
    }

    private NPC getTanner() {
        List<NPC> tanners = getNpcs().getAll().stream().filter(npc -> Objects.equals(npc.getName(), tannerName)).collect(Collectors.toList());
        if(!tanners.isEmpty()) {
            return tanners.get(0);
        }
        return null;
    }

    @Override
    public void runActivity() throws InterruptedException {
        logger.debug("Starting Tanning Activity");
        if (ItemReq.hasItemRequirements(leather.itemReqs, getInventory())) {
            if (runTo(tannerArea, condition)) {
                tan();
            } else {
                isComplete = true;
            }
        } else {
            execute(bankNode);
        }
    }

    private void tan() {
        NPC tanner = getTanner();
        RS2Widget tannerInterface = getWidgets().get(rootId, leather.childId);
        if (tannerInterface != null && tannerInterface.isVisible()) {
            tannerInterface.interact("Tan <col=ff7000>All");
        } else if (tanner != null) {
            tanner.interact("Trade");
            Sleep.sleepUntil(() -> getWidgets().get(rootId, leather.childId) != null, 15_000);
        }
    }

    @Override
    public Activity copy() {
        return null;
    }
}
