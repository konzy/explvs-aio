package activities.grand_exchange;

import activities.banking.ItemReqBanking;
import activities.grand_exchange.event.GrandExchangeBuyEvent;
import util.Sleep;
import util.executable.ExecutionFailedException;
import util.item_requirement.ItemReq;

public class GEBuyActivity extends GEActivity {

    private final GEItem geItem;
    private final ItemReq coinReq;

    private ItemReqBanking itemReqBanking;

    public GEBuyActivity(final GEItem geItem) {
        this.geItem = geItem;

        int coinsRequired = geItem.getPrice() * geItem.getQuantity();

        coinReq = new ItemReq(
                "Coins",
                coinsRequired
        ).setStackable();

        itemReqBanking = new ItemReqBanking(this, coinReq);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (box != null) {
            return;
        }

        if (!GRAND_EXCHANGE.contains(myPosition())) {
            getWalking().webWalk(GRAND_EXCHANGE);
        } else if (!coinReq.hasRequirement(getInventory())) {
            if(!getBank().isOpen()) {
                getBank().open();
                Sleep.sleepUntil(getBank()::isOpen, 5000, random(300, 500));
            }
            if(!getInventory().isEmpty()) getBank().depositAll();
            Sleep.sleepUntil(getInventory()::isEmpty, 5000, random(300, 500));
            execute(itemReqBanking);
        } else {
            GrandExchangeBuyEvent buyEvent = new GrandExchangeBuyEvent(
                    geItem.getName(),
                    geItem.getPrice(),
                    geItem.getQuantity()
            );
            execute(buyEvent);
            if (buyEvent.hasFailed()) {
                throw new ExecutionFailedException("Failed to buy items");
            } else {
                box = buyEvent.getBoxUsed();
            }
        }
    }

    @Override
    public String toString() {
        return "Buying";
    }

    @Override
    public GEBuyActivity copy() {
        return new GEBuyActivity(geItem);
    }
}
