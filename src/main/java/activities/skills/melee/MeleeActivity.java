//package activities.skills.melee;
//
//import activities.activity.Activity;
//import activities.activity.ActivityType;
//import util.Location;
//import util.ResourceMode;
//
//import java.util.List;
//
//public class MeleeActivity extends Activity {
//
//    final List<String> monsterNames;
//    final List<Location> monsterAreas;
//    final List<String> itemWhiteList;
//    final int minTradeableValue;
//    final boolean pickupUntradeables;
//    final boolean buryBones;
//
//    public MeleeActivity(final List<String> monsterNames, final List<Location> monsterAreas,
//                         final List<String> itemWhiteList, final int minTradeableValue,
//                         final boolean pickupUntradeables, final boolean buryBones) {
//        super(ActivityType.COMBAT);
//        this.monsterNames = monsterNames;
//        this.monsterAreas = monsterAreas;
//        this.itemWhiteList = itemWhiteList;
//        this.minTradeableValue = minTradeableValue;
//        this.pickupUntradeables = pickupUntradeables;
//        this.buryBones = buryBones;
//    }
//
//    @Override
//    public void runActivity() throws InterruptedException {
//        if (!axeBanking.haveRequirements()) {
//            logger.debug("We don't have an axe, getting one.");
//            execute(axeBanking);
//        } else if (nonWoodItems() > 14) {
//            logger.debug("We have a pretty full inventory, banking");
//            execute(axeBanking);
//        } else if (getInventory().isFull() && resourceMode == ResourceMode.BANK) { // regular banking
//            logger.debug("We have a full inventory, banking");
//            execute(axeBanking);
//        } else if(axeBanking.haveRequirements() &&
//                axeBanking.bestToolOnPerson().canEquip(getSkills()) &&
//                getInventory().contains(axeBanking.bestToolOnPerson().getName())) {
//            getInventory().equip(axeBanking.bestToolOnPerson().getName());
//        } else if (getInventory().isFull() && resourceMode == ResourceMode.DROP) {
//            dropAllExcept(axeBanking.bestToolOnPerson().getName());
//        } else if (!treeLocation.getArea().contains(myPosition())) {
//            getWalking().webWalk(treeLocation.getArea());
//        } else if ((birdsNest = getGroundItems().closest(BIRDS_NEST)) != null && resourceMode == ResourceMode.BANK ) {
//            birdsNest.interact("Take");
//        } else if (!myPlayer().isAnimating() || (targetTree != null && !targetTree.exists())) {
//            chopTree();
//        } else if (nextTree == null || nextTree == targetTree){
//            nextTree = getNextTree();
//            if (nextTree != null && random.nextBoolean()) {
//                nextTree.hover();
//            }
//        }
//    }
//
//    @Override
//    public Activity copy() {
//        return new MeleeActivity(monsterNames, monsterAreas, itemWhiteList, minTradeableValue, pickupUntradeables, buryBones);
//    }
//}
