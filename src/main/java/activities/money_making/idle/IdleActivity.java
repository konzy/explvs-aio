package activities.money_making.idle;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.Bank;
import activities.banking.Banking;
import activities.banking.DepositAllBanking;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.ui.Tab;
import util.Sleep;

import java.util.*;


public class IdleActivity extends Activity {

    DepositAllBanking depositAllBanking = new DepositAllBanking();

    boolean pickupItems;
    List<String> itemWhitelist;
    List<String> itemBlacklist;
    int minItemValue;
    long nextRequiredMovement;
    Set<Loot> lootHashSet;
    boolean firstMove = true;

    public IdleActivity() {
        super(ActivityType.IDLING);
        itemWhitelist = new ArrayList<>();
        itemBlacklist = new ArrayList<>();
        minItemValue = 0;
        pickupItems = false;
        weMoved();
    }

    public IdleActivity(List<String> itemWhitelist, List<String> itemBlacklist, int minItemValue, boolean pickupItems, HashSet<Loot> lootHashSet) {
        super(ActivityType.IDLING);
        this.itemWhitelist = itemWhitelist;
        this.itemBlacklist = itemBlacklist;
        this.minItemValue = minItemValue;
        this.pickupItems = pickupItems;
        this.lootHashSet = lootHashSet;
        weMoved();
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (firstMove) {
            setStatus("First Move, moving to ge");
            firstMove = false;
            getWalking().webWalk(Bank.GRAND_EXCHANGE.location.getArea());
            weMoved();
        } else if (getInventory().isFull()) {
            setStatus("Inventory full, depositing");
            execute(depositAllBanking);
            weMoved();
        } else if (System.currentTimeMillis() > nextRequiredMovement) {
            int decision = random(0, 100);
            if (decision > 101) {
//                setStatus("Running to a random bank");
//                List<Bank> bankList = Arrays.asList(Bank.FREE_BANK_AREAS);
//                Collections.shuffle(bankList);
//                Area bankToRunTo = bankList.get(0).location.getArea();
//                getWalking().webWalk(bankToRunTo);
//                execute(depositAllBanking);
            } else if (decision > 99) {
                if (Bank.GRAND_EXCHANGE.location.getArea().contains(myPlayer())) {
                    setStatus("Running to Grand Exchange");
                    getWalking().webWalk(Bank.GRAND_EXCHANGE.location.getArea());
                } else {
                    setStatus("Skipping moving");
                    return;
                }
            } else if (decision > 50) {
                setStatus("Move mouse on/off Screen");
                if (getMouse().isOnScreen()) {
                    getMouse().moveOutsideScreen();
                } else {
                    getMouse().move(random(0, 400), random(0, 400));
                }
            } else if (decision > 30) {
                setStatus("Moving Mouse");
                getMouse().move(random(0, 400), random(0, 400));
//            } else if (decision > 20) {
//                setStatus("Move camera");
//                getCamera().moveYaw(random(0, 360));
            } else if (decision >= 0) {
                if (getTabs().getOpen() != Tab.INVENTORY) getTabs().open(Tab.INVENTORY);
                else getTabs().open(Tab.SKILLS);
            }
            // maybe world hop
            weMoved();
        } else if (pickupItems) {
            setStatus("Inventory full, depositing");
            GroundItem itemToPickup = getGroundItems().closest(item -> !itemBlacklist.contains(item.getName()) &&
                    (itemWhitelist.contains(item.getName()) || getGrandExchange().getOverallPrice(item.getId()) > minItemValue));
            if (itemToPickup != null) {
                if (getBank().isOpen()) getBank().close();
                if (!itemToPickup.isVisible()) getWalking().webWalk(itemToPickup.getPosition());
                itemToPickup.interact("Take");
                sleep(random(500, 1000));
                Sleep.sleepUntil(() -> !myPlayer().isMoving(), 15000, 500);
                Sleep.sleepUntil(() -> !itemToPickup.exists(), 15000, 500);
                weMoved();
            }
        }
    }

    private void weMoved() {
        nextRequiredMovement = System.currentTimeMillis() + (random(2 * 60 * 1000, 4 * 60 * 1000));
    }

    @Override
    public Activity copy() {
        return new IdleActivity();
    }
}
