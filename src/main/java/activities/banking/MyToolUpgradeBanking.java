package activities.banking;

import org.osbot.rs07.api.Bank;
import util.MyTool;
import util.MyToolUpgrade;
import util.Sleep;

import java.util.*;

public class MyToolUpgradeBanking extends Banking {

    private final MyToolUpgrade toolUpgrade;
    private final List<MyTool> toolsInBank = new ArrayList<>();
    private boolean checkedBank = false;

    public MyToolUpgradeBanking(final MyToolUpgrade toolUpgrade) {
        this.toolUpgrade = toolUpgrade;
    }

    private MyTool bestUsableToolInBank() {
        return toolsInBank.stream()
                .filter(myTool -> myTool.canUse(getSkills()))
                .max(Comparator.comparingInt(MyTool::getLevelRequired))
                .orElse(null);
    }

    public MyTool bestToolOnPerson() {
        return toolUpgrade.bestToolOnPlayer();
    }

    private MyTool bestPossibleTool() {
        return toolUpgrade.bestMyToolToEquip();
    }

    public boolean haveRequirements() {
        return bestToolOnPerson() != null;
    }

    public boolean haveCheckedBank() {
        return checkedBank;
    }

    public boolean haveBestToolOnPerson() {
        return bestToolOnPerson() != null
                && bestUsableToolInBank() != null
                && bestToolOnPerson() == bestUsableToolInBank();
    }

    @Override
    public void onStart() throws InterruptedException {
        super.onStart();
    }

    @Override
    protected void bank(final BankType currentBankType) throws InterruptedException {
        if (bestToolOnPerson() == null && !getInventory().isEmpty()) {
            logger.debug("Depositing All");
            getBank().depositAll();
        } else if (bestToolOnPerson() != null && !getInventory().onlyContains(bestToolOnPerson().getName())) {
            logger.debug("Depositing All except tool");
            getBank().depositAllExcept(bestToolOnPerson().getName());
        }
        if (!checkedBank) { // checking bank and getting all the tools of this type in the bank
            for (MyTool tool : toolUpgrade.getAllMyTools()) {
                if (!getWorlds().isMembersWorld() && tool.isMembersOnly()) {
                    logger.debug(String.format("Skipping tool '%s' as not in members world", tool.getName()));
                    continue;
                }
                if (getBank().contains(tool.getName())) {
                    logger.debug(String.format("Found tool '%s' in the bank", tool.getName()));
                    toolsInBank.add(tool);
                }
            }
            checkedBank = true;
        }
        // if we don't have any tool, in bank or on person return
        if (bestToolOnPerson() == null && bestUsableToolInBank() == null) {
            return;
        }

        // if we don't have a tool, or there is a better tool in the bank withdraw it
        if (bestToolOnPerson() == null || bestUsableToolInBank() != null
                && bestUsableToolInBank().getLevelRequired() > bestToolOnPerson().getLevelRequired()) {
            if (getBank().getWithdrawMode() != Bank.BankMode.WITHDRAW_ITEM) {
                getBank().enableMode(Bank.BankMode.WITHDRAW_ITEM);
            }
            MyTool bestTool = bestUsableToolInBank();
            if (bestTool != null) {
                getBank().withdraw(bestTool.getName(), 1);
            }
        }
        if (bestToolOnPerson() != null &&
                getInventory().contains(bestToolOnPerson().getName()) &&
                getInventory().equip(bestToolOnPerson().getName())) {
            logger.debug("Equipped " + bestToolOnPerson().getName());
            sleep(random(500, 1000));
            if (!getInventory().isEmpty()) {
                logger.debug("Depositing All after we equipped tool");
                getBank().depositAll();
            }
        }
        setFinished();

    }

}
