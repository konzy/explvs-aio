package activities.skills.woodcutting;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.MyToolUpgradeBanking;
import org.osbot.rs07.api.filter.AreaFilter;
import org.osbot.rs07.api.filter.NameFilter;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.Skill;
import util.Location;
import util.ResourceMode;
import util.Sleep;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

public class WoodcuttingActivity extends Activity {

    private static final String BIRDS_NEST = "Bird's nest";
    private final Tree tree;
    private final Location treeLocation;
    private final ResourceMode resourceMode;
    private final Random random = new Random();
    private final MyToolUpgradeBanking axeBanking = new MyToolUpgradeBanking(new AxeUpgrade(this));
    private Entity targetTree;
    private Entity nextTree;

    public WoodcuttingActivity(final Tree tree, final Location treeLocation, final ResourceMode resourceMode) {
        super(ActivityType.WOODCUTTING);
        this.tree = tree;
        this.treeLocation = treeLocation;
        this.resourceMode = resourceMode;
    }

    @Override
    public void onStart() throws InterruptedException {
        super.onStart();
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (getSkills().getStatic(Skill.WOODCUTTING) < tree.reqLevel) {
            logger.info("not lvl req");
            isComplete = true;
            return;
        }
        GroundItem birdsNest;// = getGroundItems().closest(BIRDS_NEST);
        if (!axeBanking.haveRequirements()) {
            logger.debug("We don't have an axe, getting one.");
            execute(axeBanking);
        } else if (nonWoodItems() > 14) {
            logger.debug("We have a pretty full inventory, banking");
            execute(axeBanking);
        } else if (getInventory().isFull() && resourceMode == ResourceMode.BANK) { // regular banking
            logger.debug("We have a full inventory, banking");
            execute(axeBanking);
        } else if(axeBanking.haveRequirements() &&
                axeBanking.bestToolOnPerson().canEquip(getSkills()) &&
                getInventory().contains(axeBanking.bestToolOnPerson().getName())) {
            getInventory().equip(axeBanking.bestToolOnPerson().getName());
            sleep(random(500, 1000));
            if(!getInventory().isEmpty())
                execute(axeBanking);
        } else if (getInventory().isFull() && resourceMode == ResourceMode.DROP) {
            dropAllExcept(axeBanking.bestToolOnPerson().getName());
        } else if (!treeLocation.getArea().contains(myPosition())) {
            getWalking().webWalk(treeLocation.getArea().getRandomPosition());
        } else if ((birdsNest = getGroundItems().closest(BIRDS_NEST)) != null && resourceMode == ResourceMode.BANK ) {
            birdsNest.interact("Take");
        } else if (!myPlayer().isAnimating() || (targetTree != null && !targetTree.exists())) {
            chopTree();
        } else if (nextTree == null || nextTree == targetTree){
            nextTree = getNextTree();
            if (nextTree != null && random.nextBoolean()) {
                nextTree.hover();
            }
        }
    }

    private int nonWoodItems() {

        long totalInventory = Arrays.stream(getInventory().getItems())
                .filter(Objects::nonNull)
                .count();
        logger.debug("totalInventory=" + totalInventory);

        long totalLogs = Arrays.stream(getInventory().getItems())
                .filter(Objects::nonNull)
                .filter(item -> Tree.allLogNames().contains(item.getName()))
                .count();
        logger.debug("totalLogs=" + totalLogs);

        return (int)(totalLogs - totalInventory);
    }

    private void chopTree() {
        targetTree = getObjects().closest(
                new AreaFilter<>(treeLocation.getArea()),
                new NameFilter<>(tree.toString())
        );
        if (targetTree != null && targetTree.interact("Chop down")) {
            Sleep.sleepUntil(() -> !targetTree.exists(), 5000);
        }
    }

    private Entity getNextTree() {
        if (targetTree != null){
            return getObjects().closest(
                    new AreaFilter<>(treeLocation.getArea()),
                    new NameFilter<>(tree.toString()),
                    obj -> obj != targetTree
            );
        } else {
            return null;
        }
    }

    @Override
    public void onPaint(Graphics2D graphics) {
        Color prevColor = graphics.getColor();

        if (targetTree != null) {
            graphics.setColor(Color.GREEN);
            getGraphics().drawEntity(targetTree, graphics);
        }

        if (nextTree != null) {
            graphics.setColor(Color.ORANGE);
            getGraphics().drawEntity(nextTree, graphics);
        }

        graphics.setColor(prevColor);
    }

    @Override
    public WoodcuttingActivity copy() {
        return new WoodcuttingActivity(tree, treeLocation, resourceMode);
    }
}
