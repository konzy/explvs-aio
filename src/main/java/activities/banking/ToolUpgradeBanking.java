//package activities.banking;
//
//import org.osbot.rs07.api.Bank;
//import org.osbot.rs07.api.model.Item;
//import org.osbot.rs07.api.ui.EquipmentSlot;
//import util.Sleep;
//import util.Tool;
//import util.executable.ExecutionFailedException;
//
//import java.util.Comparator;
//import java.util.EnumSet;
//import java.util.Optional;
//import java.util.stream.Stream;
//
//public class ToolUpgradeBanking<T extends Enum<T> & Tool> extends Banking {
//
//    private final Class<T> toolClass;
//    private final EnumSet<T> toolsInBank;
//    private boolean checkedBank = false;
//
//    public ToolUpgradeBanking(final Class<T> toolClass) {
//        this.toolClass = toolClass;
//        toolsInBank = EnumSet.noneOf(toolClass);
//    }
//
////    public T getCurrentTool() {
////        return bestToolOnPerson();
//////        if (bestToolOnPerson() == bestPossibleTool()) {
//////            return bestToolOnPerson();
//////        } else if (!toolsInBank.isEmpty()) {
//////
//////        }
////
////    }
//    private T bestUsableToolInBank() {
//        return toolsInBank.stream()
//                .filter(tool -> tool.canUse(getSkills()))
//                .max(Comparator.comparingInt(tool -> tool.getLevelRequired()))
//                .orElse(null);
//    }
//
//    public T bestToolOnPerson() {
//        if (getInventory() == null) return null;
//        if (getEquipment() == null) return null;
//        Item[] inventory = getInventory().getItems();
//        Item[] equipment = getEquipment().getItems();
//        return Stream.of(toolClass.getEnumConstants())
//                .filter(tool -> (inventory.length > 0 && getInventory().contains(tool.name())) ||
//                        (equipment.length > 0 && getEquipment().contains(tool.name())))
//                .max(Comparator.comparingInt(tool -> tool.getLevelRequired()))
//                .orElse(null);
//    }
//
//    private T bestPossibleTool() {
//        return Stream.of(toolClass.getEnumConstants())
//                .filter(tool -> !tool.isMembersOnly() || getWorlds().isMembersWorld())
//                .filter(tool -> tool.canUse(getSkills()))
//                .filter(tool -> getInventory().contains(tool.getName()) ||
//                        getEquipment().isWearingItem(EquipmentSlot.WEAPON, tool.getName()))
//                .max(Comparator.comparingInt(tool -> tool.getLevelRequired()))
//                .orElse(null);
//    }
//
//    public boolean haveRequirements() {
//        return bestToolOnPerson() != null;
//    }
//
//    public boolean haveCheckedBank() {
//        return checkedBank;
//    }
//
//    public boolean haveBestToolOnPerson() {
//        return bestToolOnPerson() != null
//                && bestUsableToolInBank() != null
//                && bestToolOnPerson() == bestUsableToolInBank();
//    }
//
//    @Override
//    public void onStart() throws InterruptedException {
//        super.onStart();
//    }
//
//    @Override
//    protected void bank(final BankType currentBankType) {
//        // Depositing all except the best tool we have
////        if (!getInventory().isEmptyExcept(bestToolOnPerson().getName())) {
////            getBank().depositAllExcept(bestToolOnPerson().getName());
////        }
//        if (bestToolOnPerson()!getInventory().onlyContains(bestToolOnPerson().getName())) {
//            getBank().depositAllExcept(bestToolOnPerson().getName());
//            int initialItems = getInventory().getItems().length;
//            Sleep.sleepUntil(() -> getInventory().getItems().length != initialItems, 15000, 350);
//        }
//        if (!checkedBank) { // checking bank and getting all the tools of this type in the bank
//            for (T tool : toolClass.getEnumConstants()) {
//                if (!getWorlds().isMembersWorld() && tool.isMembersOnly()) {
//                    log(String.format("Skipping tool '%s' as not in members world", tool.getName()));
//                    continue;
//                }
//                if (getBank().contains(tool.getName())) {
//                    log(String.format("Found tool '%s' in the bank", tool.getName()));
//                    toolsInBank.add(tool);
//                }
//            }
//            checkedBank = true;
//        }
//        // if we have the best tool, or we don't have any tool, return
//        if (haveBestToolOnPerson() || (bestToolOnPerson() == null && bestUsableToolInBank() == null)) {
//            return;
//        }
//
//        // if we don't have a tool, or there is a better tool in the bank
//        if (bestToolOnPerson() == null || bestUsableToolInBank() != null && bestUsableToolInBank() != bestToolOnPerson()) {
//            if (getBank().getWithdrawMode() != Bank.BankMode.WITHDRAW_ITEM) {
//                getBank().enableMode(Bank.BankMode.WITHDRAW_ITEM);
//            }
//            T bestTool = bestUsableToolInBank();
//            if (bestTool != null) {
//                getBank().withdraw(bestTool.getName(), 1);
//                int initialItems = getInventory().getItems().length;
//                Sleep.sleepUntil(() -> getInventory().getItems().length != initialItems, 15000, 350);
//                setFinished();
//            }
//        } else {
//            setFinished();
//        }
//    }
//
//}
