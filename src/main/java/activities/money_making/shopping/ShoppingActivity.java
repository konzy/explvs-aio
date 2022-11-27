package activities.money_making.shopping;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import org.osbot.rs07.api.Chatbox;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.World;
import util.Sleep;
import util.item_requirement.ItemReq;

import java.util.*;
import java.util.stream.Collectors;

public class ShoppingActivity extends Activity {

    private Shop shop;
    private HashMap<String, Integer> itemsToBuyQuantToLeave;
    ItemReq coinReq = new ItemReq("Coins",1).setStackable();
    private ItemReqBanking itemReqBanking = new ItemReqBanking(this, coinReq);
    private Queue<Integer> blackListedWorlds = new PriorityQueue<>();
    private boolean doneWithThisShop = false;

    public ShoppingActivity() {
        super(ActivityType.SHOPPING);
    }

    public ShoppingActivity(HashMap<String, Integer> itemsToBuy, Shop shop) {
        super(ActivityType.SHOPPING);
        this.itemsToBuyQuantToLeave = itemsToBuy;
        this.shop = shop;
    }

    @Override
    public void runActivity() throws InterruptedException {
        while(blackListedWorlds.size() > 10) blackListedWorlds.poll();

        RS2Widget shopWidget = getWidgets().get(300, 16);

        String keeper = shop.shopKeepers.get(0);

        NPC closestKeeper = getNpcs().closest(keeper);
        logger.debug("looping");
        if (getInventory().isFull()) {
            logger.debug("Inventory full");
            execute(itemReqBanking);
        } else if (!coinReq.hasRequirement(getInventory())) {
            logger.debug("getting money");
            if(!getBank().isOpen()) {
                getBank().open();
                Sleep.sleepUntil(getBank()::isOpen, 5000, random(300, 500));
            }
            getBank().depositAll();
            Sleep.sleepUntil(getInventory()::isEmpty, 5000, random(300, 500));
            execute(itemReqBanking);
        } else if (!shop.location.getArea().contains(myPlayer()) && myPlayer().getPosition().distance(closestKeeper) > 20) {
            logger.debug("getting closer to the shop: web walk");
            getWalking().webWalk(shop.location.getArea());
        } else if (!shop.location.getArea().contains(myPlayer()) && myPlayer().getPosition().distance(closestKeeper) > 4)  {
            logger.debug("getting closer to the shop: straight walk");
            getWalking().walk(shop.location.getArea().getRandomPosition());
        } else if (shopWidget == null || !shopWidget.isVisible()) {
            logger.debug("trying to trade with shop keeper");
            Sleep.sleepUntil(() -> !myPlayer().isMoving(), 5000);
            sleep(random(0, 100));
            getNpcs().getAll().stream()
                    .filter(npc -> shop.shopKeepers.contains(npc.getName()))
                    .findFirst()
                    .ifPresent(npc -> npc.interact("Trade"));
            Sleep.sleepUntil(() -> getWidgets().get(300, 16) != null, 3000);
            sleep(random(500, 1000));
        } else if (shopWidget.isVisible()){
            logger.debug("Shop window open");
            List<RS2Widget> widgets = Arrays.stream(shopWidget.getChildWidgets())
                    .filter(wid -> {
                        if(wid == null || !wid.getSpellName().contains(">") || !wid.getSpellName().contains("<") || wid.getItemAmount() == 0) return false;
                        return itemsToBuyQuantToLeave.containsKey(cleanSpellName(wid)) && wid.getItemAmount() > itemsToBuyQuantToLeave.get(cleanSpellName(wid));
                    }).collect(Collectors.toList());
            if (!widgets.isEmpty()) {
                for (RS2Widget wid : widgets) {
                    int maxTries = 10;
                    int tries = 0;
                    String cleanName = cleanSpellName(wid);
                    double pct = (double)wid.getItemAmount() / (double)itemsToBuyQuantToLeave.get(cleanName);
                    while (wid.getItemAmount() > itemsToBuyQuantToLeave.get(cleanName) && tries < maxTries && !getInventory().isFull() && pct > 0.05) {
                        long initialItemCount = Arrays.stream(getInventory().getItems()).filter(Objects::isNull).count();
                        tries++;
                        int amtToBuy = wid.getItemAmount() - itemsToBuyQuantToLeave.get(cleanName);
                        if (itemsToBuyQuantToLeave.get(cleanName) == 0) amtToBuy = random(10, 200);
//                        if (getInventory().getItem("Coins").getAmount() < 1000) {
//                            isComplete = true;
//                            return;
//                        }
                        if (amtToBuy >= 50) {
                            wid.interact("Buy 50");
                        } else if(amtToBuy >= 10) {
                            wid.interact("Buy 10");
                        } else if(amtToBuy >= 5) {
                            wid.interact("Buy 5");
                        } else if(amtToBuy >= 1) {
                            wid.interact("Buy 1");
                        }
                        List<String> chat = getChatbox().getMessages(Chatbox.MessageType.GAME);
                        int len = chat.size();
                        String lastMessage = chat.get(len - 1);
                        if (lastMessage.contains("You don't have enough coins.")) {
                            isComplete = true;
                            return;
                        }
                        Sleep.sleepUntil(() -> initialItemCount != Arrays.stream(getInventory().getItems()).filter(Objects::isNull).count(), 2500);
                        sleep(random(0, 150));
                    }
                }
            } else {
                logger.debug("jumping worlds");
                blackListedWorlds.add(getWorlds().getCurrentWorld());

                getWidgets().closeOpenInterface();
                getWorlds().isMembersWorld();

                List<World> worlds = getWorlds().getAvailableWorlds(true).stream()
                        .filter(w -> !w.isFull() &&
                                !w.isPvpWorld() &&
                                !w.isHighRisk() &&
                                !blackListedWorlds.contains(w.getId()) &&
                                getWorlds().isWorldAllowedForHop(w) &&
                                (getWorlds().isMembersWorld() == w.isMembers())
                        ).collect(Collectors.toList());
                Collections.shuffle(worlds);
                getWorlds().hop(worlds.get(0).getId());
                doneWithThisShop = false;
            }
        }
    }

    private String cleanSpellName(RS2Widget wid) {
        return wid.getSpellName().split(">")[1].split("<")[0].trim();
    }

    @Override
    public Activity copy() {
        return null;
    }
}
