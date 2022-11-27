package activities.banking;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.event.WebWalkEvent;
import util.Sleep;
import util.executable.BlockingExecutable;

import java.util.stream.Stream;

public abstract class Banking extends BlockingExecutable {

    private static final Area[] ALL_BANK_AND_DEPOSIT_BOX_AREAS = Stream.concat(Stream.of(Bank.AREAS), Stream.of(DepositBox.AREAS)).toArray(Area[]::new);
    private static Position[] ALL_BANK_AND_DEPOSIT_BOX_POSITIONS = Stream.concat(Stream.of(Bank.POSITIONS), Stream.of(DepositBox.POSITIONS)).toArray(Position[]::new);


    private final boolean useDepositBoxes;

    protected enum BankType {
        BANK,
        DEPOSIT_BOX
    }

    private BankType currentBankType;

    public Banking() {
        this.useDepositBoxes = false;
    }

    public Banking(final boolean useDepositBoxes) {
        this.useDepositBoxes = useDepositBoxes;
    }

    @Override
    public void blockingRun() throws InterruptedException {
        if (!playerInBank()) {
            getWidgets().closeOpenInterface();
            if (!walkToBank()) {
                logger.debug("We failed to webwalk, great...");
            }
        } else if (getInventory().contains("Coin pouch")) {
            getInventory().getItem("Coin pouch").interact();
        } else if (!isBankOpen()) {
            openBank();
        } else {
            if (firstTimeBanking()) {
                sleep(random(500, 1000));
                firstTimeBankingWidget().interact();
                sleep(random(500, 1000));
            }
            if (getBank() != null && getBank().isOpen()) {
                currentBankType = BankType.BANK;
            } else {
                currentBankType = BankType.DEPOSIT_BOX;
            }

            bank(currentBankType);
        }
    }

    private RS2Widget firstTimeBankingWidget() {
        return getWidgets().get(664, 29);
    }

    private boolean firstTimeBanking() {
        return firstTimeBankingWidget() != null;
    }

    public boolean playerInBank() {
        if (myPlayer() != null & myPlayer().getPosition() != null) {
            Entity closestBank;
            if (currentBankType == BankType.BANK)
                closestBank = getBank().closest();
            else closestBank = getObjects().closest("Bank deposit box");
            return Stream.of(Bank.AREAS).anyMatch(area -> area.contains(myPosition())) ||
                    (closestBank != null && closestBank.isVisible()) ;
        }
        return false;
    }

    private boolean walkToBank() throws InterruptedException {
        WebWalkEvent webEvent;
        if (useDepositBoxes) {
            int shortestDistance = Integer.MAX_VALUE;
            Position closestPosition = myPosition();
            for (Position position : ALL_BANK_AND_DEPOSIT_BOX_POSITIONS) {
                shortestDistance = Math.min(myPosition().distance(position), shortestDistance);
                closestPosition = position;
            }
            if (shortestDistance < 20) {
                ALL_BANK_AND_DEPOSIT_BOX_POSITIONS = Stream.concat(Stream.of(Bank.POSITIONS), Stream.of(DepositBox.POSITIONS)).toArray(Position[]::new);
                return getWalking().walk(closestPosition);
            }
            return getWalking().webWalk(ALL_BANK_AND_DEPOSIT_BOX_AREAS);
//            webEvent = new WebWalkEvent(ALL_BANK_AND_DEPOSIT_BOX_AREAS);
        } else {
            return getWalking().webWalk(Bank.POSITIONS);
//            webEvent = new WebWalkEvent(Bank.POSITIONS);
        }
//        webEvent.setEnergyThreshold(random(15, 35));
//        webEvent.setHighBreakPriority(true);
//        if(random(1,3) == 1) webEvent.useSimplePath();
//
//        webEvent.execute();
//        if(webEvent.getCompletion() < 100) {
//            Entity closestBank = getBank().closest();
//            closestBank = getBank().closest();
//            if (closestBank.getPosition() != null) {
//                logger.debug("Walking the last bit to the bank.");
//                getWalking().webWalk(closestBank.getPosition());
//            }
//        }

//        return true;
    }

    boolean isBankOpen() {
        if (useDepositBoxes && getBank() != null && getDepositBox() != null) {
            return getBank().isOpen() || getDepositBox().isOpen();
        }

        if (getBank() != null) {
            return getBank().isOpen();
        }

        if (useDepositBoxes && getDepositBox() != null) {
            return getDepositBox().isOpen();
        }

        return false;
    }

    private void openBank() throws InterruptedException {
        if (getBank() != null && getBank().open()) {
            Sleep.sleepUntil(() -> getBank().isOpen(), 15000);
            return;
        }

        if (useDepositBoxes && getDepositBox() != null) {
            if (getDepositBox().open()) {
                Sleep.sleepUntil(() -> getDepositBox().isOpen(), 15000);
                return;
            }
        }

        throw new IllegalStateException("Cannot open bank, no bank or deposit box found.");
    }

    /**
     * Execute banking operation
     */
    protected abstract void bank(final BankType currentBankType) throws InterruptedException;

    @Override
    public void onEnd() {
        closeBank();
    }

    private void closeBank() {
        if (currentBankType == BankType.DEPOSIT_BOX) {
            if (getDepositBox().isOpen() && getDepositBox().close()) {
                Sleep.sleepUntil(() -> !getDepositBox().isOpen(), 2500);
            }
            return;
        }

        if (getBank().isOpen() && getBank().close()) {
            Sleep.sleepUntil(() -> !getBank().isOpen(), 2500);
        }
    }

    @Override
    public String toString() {
        return "ItemReqBanking";
    }
}

