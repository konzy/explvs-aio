package activities.skills.mining;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.DepositAllBanking;
import activities.banking.MyToolUpgradeBanking;
import org.osbot.rs07.api.filter.AreaFilter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.event.WebWalkEvent;
import util.ResourceMode;
import util.Sleep;
import util.executable.Executable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;

public class MiningActivity extends Activity {

    final ResourceMode resourceMode;
    private final Mine mine;
    private final Rock rock;
    private final List<String> cutGems = Arrays.asList("Opal", "Jade", "Red topaz", "Sapphire", "Emerald", "Ruby", "Diamond", "Dragonstone", "Onyx");
    private final List<String> uncutGems = Arrays.asList("Uncut opal", "Uncut jade", "Uncut red topaz", "Uncut sapphire", "Uncut emerald", "Uncut ruby", "Uncut diamond", "Uncut dragonstone", "Uncut onyx");
    private final MyToolUpgradeBanking pickaxeBanking = new MyToolUpgradeBanking(new PickaxeUpgrade(this));
    DepositAllBanking depositAllBanking = new DepositAllBanking();
    Executable miningNode = new MiningNode();
    private final boolean upgradePickaxe;

    public MiningActivity(final Mine mine, final Rock rock, final ResourceMode resourceMode, final boolean upgradePickaxe) {
        super(ActivityType.MINING);
        this.mine = mine;
        this.rock = rock;
        this.resourceMode = resourceMode;
        this.upgradePickaxe = upgradePickaxe;
    }

    @Override
    public void onStart() throws InterruptedException {
        super.onStart();
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (!pickaxeBanking.haveRequirements()) {
            logger.debug("We don't have a pickaxe, getting one.");
            execute(pickaxeBanking);
        } else if(pickaxeBanking.haveRequirements() &&
                pickaxeBanking.bestToolOnPerson().canEquip(getSkills()) &&
                getInventory().contains(pickaxeBanking.bestToolOnPerson().getName())) {
            getInventory().equip(pickaxeBanking.bestToolOnPerson().getName());
        } else if (nonOreItems() > 14 && resourceMode == ResourceMode.DROP) {
            logger.debug("We have a pretty full inventory, even though we're dropping, lets bank");
            execute(pickaxeBanking);
        } else if (getInventory().isFull() && resourceMode == ResourceMode.BANK) { // regular banking
            logger.debug("We have a full inventory, banking");
            execute(pickaxeBanking);
        } else if (pickaxeBanking.bestToolOnPerson().canEquip(getSkills()) && !getEquipment().isWearingItem(EquipmentSlot.WEAPON, pickaxeBanking.bestToolOnPerson().getName())) {
            setStatus("Equipping new tool");
            getInventory().equip(pickaxeBanking.bestToolOnPerson().getName());
            sleep(random(500, 1000));
            execute(pickaxeBanking);
        } else if (getInventory().isFull() && resourceMode == ResourceMode.DROP) {
            setStatus("Dropping ores");
            dropAll(Rock.allOreNamesArray());
        } else {
            mine();
        }
    }

    private int nonOreItems() {

        long totalInventory = Arrays.stream(getInventory().getItems())
                .filter(Objects::nonNull)
                .count();
//        logger.debug("totalInventory=" + totalInventory);

        long totalLogs = Arrays.stream(getInventory().getItems())
                .filter(Objects::nonNull)
                .filter(item -> Rock.allOreNames().contains(item.getName()))
                .count();
        logger.debug("totalOres=" + totalLogs);

        return (int)(totalLogs - totalInventory);
    }

    protected void mine() throws InterruptedException {
        execute(miningNode);
    }

    @Override
    public MiningActivity copy() {
        return new MiningActivity(mine, rock, resourceMode, upgradePickaxe);
    }

    private class MiningNode extends Executable {

        private Area rockArea;
        private Entity currentRock;

        private Area getAreaWithRock(Rock rock) {
            return Arrays.stream(mine.rockAreas).filter(rockArea -> rockArea.rock == rock)
                    .map(rockArea -> rockArea.area).findAny().get();
        }

        @Override
        public void run() throws InterruptedException {
            if (rockArea == null) {
                rockArea = getAreaWithRock(rock);
            } else if (!rockArea.contains(myPosition())) {
                setStatus("Walking to Mining Area");
                WebWalkEvent webWalkEvent = new WebWalkEvent(rockArea);
                webWalkEvent.setMoveCameraDuringWalking(false);
                getWalking().webWalk(rockArea);
            } else {
                mineRock();
            }
        }

        private void mineRock() {
            currentRock = getNextRock();
            setStatus("Mining Rock");
            if (currentRock != null && currentRock.interact("Mine")) {
                Sleep.sleepUntil(() -> myPlayer().isAnimating(), random(4000, 6000));
                if (myPlayer().isAnimating()) {
                    Sleep.sleepUntil(() -> !currentRock.exists(), 60000);
                }
            }
        }

        private RS2Object getNextRock() {
            return rock.getClosestWithOre(
                    getBot().getMethods(),
                    new AreaFilter<>(rockArea),
                    obj -> obj != currentRock
            );
        }

        @Override
        public String toString() {
            return "Mining";
        }
    }
}
