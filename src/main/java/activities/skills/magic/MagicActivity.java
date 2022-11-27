package activities.skills.magic;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import activities.skills.cooking.CookingItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.MagicSpell;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.input.mouse.MouseDestination;
import org.osbot.rs07.utility.Condition;
import util.Sleep;
import util.executable.Executable;
import util.item_requirement.ItemReq;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MagicActivity extends Activity {
    private final Executable bankNode;
    private final Spell spell;
    private final String targetName;
    private final Staff staff;
    private final ItemReq[] itemReqs;

    private Rectangle spellBoundingBox = null;
    private Rectangle targetBoundingBox = null;

    public MagicActivity(final Spell spell, final Staff staff, final String targetName) {
        super(ActivityType.MAGIC);
        this.spell = spell;
        this.targetName = targetName;
        this.staff = staff;
        this.itemReqs = makeItemRequirements();
        bankNode = new ItemReqBanking(this, itemReqs);
//        for (ItemReq item:
//             itemReqs) {
//            logger.info(item.getName());
//        }
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (!ItemReq.hasItemRequirements(itemReqs, getInventory())) {
            logger.info("getting requirements");
            execute(bankNode);
        } else if(staff != null &&
                getInventory().getItem(staff.name()) != null &&
                !getEquipment().isWieldingWeapon(staff.name())) {
            getInventory().getItem(staff.name()).interact("Wield");
            Sleep.sleepUntil(() -> getEquipment().isWieldingWeapon(staff.name()), 3_000, random(500, 1000));
        }
        else {
            castSpell();
        }
    }

    private void castSpell() {
        switch (spell.getSpellTarget()) {
            case ITEM:
                switch (spell) {
                    case HIGH_LEVEL_ALCHEMY:
                    case LOW_LEVEL_ALCHEMY:
                        alchemy();
                        break;
                    default:
                        break;
                }
                break;
            case NPC:
            case OBJECT:
                break;
        }
    }

    private void alchemy() {
        if (!getInventory().getItemInSlot(16).getName().equals(targetName)) {
            moveItemToSlot(16);
        } else if (!Tab.MAGIC.isOpen(getBot())) {
            getTabs().open(Tab.MAGIC);
            Sleep.sleepUntil(() -> Tab.MAGIC.isOpen(getBot()), 5000);
            if (spellBoundingBox == null) {
                spellBoundingBox = getTabs().magic.getSpellWidget(spell.getSpell()).getBounds();
            }
        } else if (spellBoundingBox != null && targetBoundingBox != null){
            Rectangle2D box = spellBoundingBox.createIntersection(targetBoundingBox);
            if (!box.contains(getMouse().getPosition()) && random(0, 100) > 2) {
                int x = random((int)box.getMinX(), (int)box.getMaxX());
                int y = random((int)box.getMinY(), (int)box.getMaxY());
                getMouse().move(x, y);
            } else if(!getMagic().isSpellSelected()) {
                getMouse().click(false);
                Sleep.sleepUntil(() -> getTabs().getOpen() == Tab.INVENTORY, 5000, random(500, 1000));
            } else {
                getMouse().click(false);
                Sleep.sleepUntil(() -> getTabs().getOpen() == Tab.MAGIC, 5000, random(500, 1000));
            }
        }
    }

    private void moveItemToSlot(int destinationSlotNumber) {
        Item targetItem = getInventory().getItem(targetName);
        targetItem.hover();
        Condition condition = new Condition() {
            @Override
            public boolean evaluate() {
                return getInventory().getSlotBoundingBox(destinationSlotNumber).contains(getMouse().getPosition());
            }
        };

        MouseDestination destination = new MouseDestination(getBot()) {
            @Override
            public Rectangle getBoundingBox() {
                int slotNumber = getInventory().getSlot(targetName);
                return getInventory().getSlotBoundingBox(slotNumber);
            }

            @Override
            public boolean evaluate() {
                return true;
            }

            @Override
            public Area getArea() {
                return new Area();
            }

            @Override
            public boolean isVisible() {
                return true;
            }
        };

        getMouse().continualClick(destination, condition);
        getInventory().hover(destinationSlotNumber);
        targetBoundingBox = getInventory().getSlotBoundingBox(destinationSlotNumber);
    }

    private ItemReq[] makeItemRequirements() {
        List<ItemReq> newReqs = Arrays.stream(spell.getItemReqs())
                .filter(itemReq -> Arrays.stream(staff.runes)
                        .noneMatch(rune -> rune.name().equals(itemReq.getName())))
                .collect(Collectors.toList());
        logger.info("test");

////        logger.info("itemReqs.length=" + spell.itemReqs.length);
//
//        ItemReq[] tempItemReqs = spell.getItemReqs();
////        logger.info("itemReqs.length=" + tempItemReqs.length);
//
//        List<ItemReq> newReqs = Arrays.asList(tempItemReqs);
//        for (Rune rune : staff.getRunes()) {
//            if (Arrays.stream(spell.getItemReqs()).anyMatch(itemReq -> itemReq.getName().equals(rune.name())))
//
//            newReqs = Arrays.stream(spell.getItemReqs()).filter(itemReq -> !itemReq.getName().equals(rune.name())).collect(Collectors.toList());
//        }

//        List<ItemReq> newReqs = Arrays.stream(tempItemReqs)
//                .filter(itemReq -> Arrays.stream(staff.getRunes()).noneMatch(rune -> {
////                    logger.info("rune name=" + itemReq.getName());
////                    logger.info("staff rune name=" + rune.name());
////                    logger.info("logic=" + !rune.name().equals(itemReq.getName()));
//
//                    return rune.name().equals(itemReq.getName());
//                }))
//                .collect(Collectors.toList());
//        logger.info("rune name=" + staff.name());
//        logger.info("staff name=" + staff.name());
        newReqs.add(new ItemReq(staff.name).setEquippable());
        if (spell.getSpellTarget().equals(SpellTarget.ITEM)) newReqs.add(new ItemReq(targetName, 1).setStackable().setNoted());

        return newReqs.toArray(new ItemReq[0]);
    }


    @Override
    public Activity copy() {
        return new MagicActivity(spell, staff, targetName);
    }
}
