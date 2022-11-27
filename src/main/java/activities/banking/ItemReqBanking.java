package activities.banking;

import activities.activity.Activity;
import org.osbot.rs07.api.Bank.BankMode;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Item;
import util.Sleep;
import util.executable.BlockingExecutable;
import util.executable.ExecutionFailedException;
import util.item_requirement.ItemReq;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemReqBanking extends Banking {

    private final ItemReq[] itemReqs;
    private final Map<ItemReq, Integer> reqTargetAmountMap = new HashMap<>();
    private final Filter<Item> itemReqFilter;
    private final Activity activity;

    public ItemReqBanking(Activity activity, final ItemReq... itemReqs) {
        this.itemReqs = itemReqs;
        itemReqFilter = item -> Stream.of(itemReqs).anyMatch(req -> req.isRequirementItem(item));
        this.activity = activity;
        loadItemReqTargetAmounts();
    }

    public ItemReqBanking(Activity activity, boolean useDepositBoxes, final ItemReq... itemReqs) {
        super(useDepositBoxes);
        this.itemReqs = itemReqs;
        itemReqFilter = item -> Stream.of(itemReqs).anyMatch(req -> req.isRequirementItem(item));
        this.activity = activity;
        loadItemReqTargetAmounts();
    }

    private void loadItemReqTargetAmounts() {
        int slotsRemaining = 28;

        for (ItemReq itemReq : itemReqs) {
            if (itemReq.isStackable()) {
                reqTargetAmountMap.put(itemReq, itemReq.getMaxQuantity());

                // If the item is equippable it won't take any slots in the inventory
                if (!itemReq.isEquipable()) {
                    slotsRemaining--;
                }
            }
        }

        while (slotsRemaining > 0) {
            boolean noChange = true;

            for (final ItemReq itemReq : itemReqs) {
                if (!itemReq.isStackable()) {
                    if (itemReq.getMinQuantity() > slotsRemaining) {
                        break;
                    }
                    Integer existingAmount = reqTargetAmountMap.get(itemReq);

                    if (existingAmount == null) {
                        reqTargetAmountMap.put(itemReq, itemReq.getMinQuantity());

                        // If the item is equipable it won't take any slots in the inventory
                        if (!itemReq.isEquipable()) {
                            slotsRemaining -= itemReq.getMinQuantity();
                        }
                        noChange = false;
                    } else if (itemReq.getMaxQuantity() == ItemReq.QUANTITY_ALL || existingAmount < itemReq.getMaxQuantity()) {
                        reqTargetAmountMap.put(itemReq, existingAmount + itemReq.getMinQuantity());

                        // If the item is equipable it won't take any slots in the inventory
                        if (!itemReq.isEquipable()) {
                            slotsRemaining -= itemReq.getMinQuantity();
                        }
                        noChange = false;
                    }
                }
            }

            if (noChange) {
                break;
            }
        }
    }

    @Override
    protected void bank(final BankType currentBankType) throws InterruptedException {
        reqTargetAmountMap.forEach((itemRequirement, integer) -> {
            if (itemRequirement != null && integer != null) {
                log(itemRequirement.getName() + ": " + integer);
            }
        });

        if (getInventory().contains(item -> !itemReqFilter.match(item))) {
            log("Depositing");
            depositNonItemReqs();
        } else if (!ItemReq.hasItemRequirements(itemReqs, getInventory(), getEquipment()) && !activity.isComplete) {
            log("Withdrawing item requirements");
            getItemRequirements(itemReqs);
        } else {
            setFinished();
        }
    }

    private void depositNonItemReqs() throws InterruptedException {
        getExecutableUtil().execute(new BlockingExecutable() {
            @Override
            protected void blockingRun() throws InterruptedException {
                if (!getBank().isOpen()) {
                    getBank().open();
                } else if (getInventory().contains(item -> !itemReqFilter.match(item))) {
                    getBank().depositAll(item -> !itemReqFilter.match(item));
                } else {
                    setFinished();
                }
            }
        });
    }

    private void getItemRequirements(final ItemReq... itemReqs) throws InterruptedException {
        final List<ItemReq> itemReqList = Arrays.asList(itemReqs);

        final List<ItemReq> equipableItemReqs = itemReqList.stream().filter(ItemReq::isEquipable).collect(Collectors.toList());
        final Filter<Item> equipableItemReqFilter = item -> equipableItemReqs.stream().anyMatch(req -> req.isRequirementItem(item));
        final List<ItemReq> nonEquipableItemReqsList = itemReqList.stream().filter(req -> !req.isEquipable()).collect(Collectors.toList());

        // First we deposit any items that are not a requirement
        execute(new BlockingExecutable() {
            @Override
            protected void blockingRun() throws InterruptedException {
                if (!getInventory().contains(item -> !itemReqFilter.match(item))) {
                    setFinished();
                    return;
                }

                if (!getBank().isOpen()) {
                    getBank().open();
                } else {
                    getBank().depositAllExcept(itemReqFilter);
                }
            }
        });

        // Then we deposit any excess item reqs we have
        execute(new BlockingExecutable() {
            final Queue<ItemReq> nonEquipableItemReqs = new LinkedList<>(nonEquipableItemReqsList);

            @Override
            protected void blockingRun() throws InterruptedException {
                if (nonEquipableItemReqs.isEmpty()) {
                    setFinished();
                    return;
                }

                if (!getBank().isOpen()) {
                    getBank().open();
                } else {
                    ItemReq itemReq = nonEquipableItemReqs.peek();

                    int targetAmount = reqTargetAmountMap.get(itemReq);
                    long amountOnPlayer = itemReq.getAmount(getInventory(), getEquipment());

                    if (amountOnPlayer > targetAmount && targetAmount != ItemReq.QUANTITY_ALL) {
                        depositExcess(itemReq);
                    } else {
                        nonEquipableItemReqs.poll();
                    }
                }
            }
        });

        if (!equipableItemReqs.isEmpty()) {
            // Now we want to withdraw any equipable item reqs
            execute(new BlockingExecutable() {
                Queue<ItemReq> equipableItemReqQueue = new LinkedList<>(equipableItemReqs);

                @Override
                protected void blockingRun() throws InterruptedException {
                    if (equipableItemReqQueue.isEmpty()) {
                        setFinished();
                        return;
                    }

                    if (!getBank().isOpen()) {
                        getBank().open();
                    } else if (getInventory().contains(item -> !equipableItemReqFilter.match(item))) {
                        getBank().depositAllExcept(equipableItemReqFilter);
                    } else if (getEquipment().contains(item -> !equipableItemReqFilter.match(item))) {
                        getBank().depositWornItems();
                    } else {
                        ItemReq itemReq = equipableItemReqQueue.peek();
                        if (!itemReq.hasRequirement(getInventory(), getEquipment())) {
                            withdrawItemReq(itemReq);
                        } else {
                            equipableItemReqQueue.poll();
                        }
                    }
                }
            });

            // Now we want to equip the item reqs
            execute(new BlockingExecutable() {
                Queue<ItemReq> equipableItemReqQueue = new LinkedList<>(equipableItemReqs);

                @Override
                protected void blockingRun() throws InterruptedException {
                    if (equipableItemReqQueue.isEmpty()) {
                        setFinished();
                    } else if (getBank().isOpen()) {
                        getBank().close();
                    } else {
                        ItemReq itemReq = equipableItemReqQueue.peek();
                        if (getInventory().contains(itemReq.getName())) {
                            getInventory().getItem(itemReq.getName()).interact();
                        } else {
                            equipableItemReqQueue.poll();
                        }
                    }
                }
            });
        }

        // Finally we want to withdraw any remaining item reqs
        execute(new BlockingExecutable() {
            final Queue<ItemReq> nonEquippableItemReqs = new LinkedList<>(nonEquipableItemReqsList);

            @Override
            protected void blockingRun() throws InterruptedException {
                if (nonEquippableItemReqs.isEmpty()) {
                    log("Finished");
                    setFinished();
                    return;
                }

                if (!getBank().isOpen()) {
                    getBank().open();
                    return;
                }

                ItemReq itemReq = nonEquippableItemReqs.peek();

                int targetAmount = reqTargetAmountMap.get(itemReq);
                long amountOnPlayer = itemReq.getAmount(getInventory(), getEquipment());

//                logger.debug("amountInBank=" + itemMissing(nonEquippableItemReqs));
                if (//amountOnPlayer == 0 &&
                        amountOnPlayer < targetAmount || (itemReq.isStackable() && amountOnPlayer < itemReq.getMinQuantity())  ) {
                    withdrawItemReq(itemReq);
                } else {
                    nonEquippableItemReqs.poll();
                }
            }
        });
    }

    private boolean itemInBank(Queue<ItemReq> itemReqs) {
        for (String reqName : itemReqs.stream().map(ItemReq::getName).collect(Collectors.toList())) {
            if ((getBank().getItem(reqName) == null && getInventory().getItem(reqName) == null) || getBank().getItem(reqName).getAmount() == 0) {
                activity.isComplete = true;
                return false;
            }
        }
        return true;
    }

    private boolean depositExcess(final ItemReq itemReq) {
        int amountOnPlayer = (int) getAmountOnPlayer(itemReq);

        int excessAmount = amountOnPlayer - reqTargetAmountMap.get(itemReq);
        if (getBank().deposit(itemReq.getName(), excessAmount)) {
            Sleep.sleepUntil(() -> getAmountOnPlayer(itemReq) < amountOnPlayer, 1200, 600);
            return true;
        }

        return false;
    }

    private void withdrawItemReq(final ItemReq itemReq) throws InterruptedException {
        log("Withdrawing item requirement: " + itemReq);
        if (itemReq.isNoted() && getBank().getWithdrawMode() != BankMode.WITHDRAW_NOTE) {
            getBank().enableMode(BankMode.WITHDRAW_NOTE);
            sleep(random(1000, 2000));
        }
        if (!itemReq.isNoted() && getBank().getWithdrawMode() != BankMode.WITHDRAW_ITEM) {
            getBank().enableMode(BankMode.WITHDRAW_ITEM);
            sleep(random(1000, 2000));
        }
        int amountOnPlayer = (int) getAmountOnPlayer(itemReq);
        logger.debug("amountOnPlayer=" + amountOnPlayer);
        int targetAmount = reqTargetAmountMap.get(itemReq);
        logger.debug("targetAmount=" + targetAmount);

        if (amountOnPlayer < targetAmount) {
            int requiredAmountForMinQuantity = Math.max(0, itemReq.getMinQuantity() - amountOnPlayer);
            int bankAmount = (int) itemReq.getAmount(getBank());
            logger.debug("bankAmount=" + bankAmount);

            if (bankAmount < requiredAmountForMinQuantity) {
                throw new ExecutionFailedException("Not enough " + itemReq.getName() + " in bank. Required: " + requiredAmountForMinQuantity + ", Found: " + bankAmount);
            }
        }
        if (targetAmount == getInventory().getEmptySlots()) {
            getBank().withdrawAll(itemReq.getName());
        } else {
            int requiredTargetAmount = reqTargetAmountMap.get(itemReq) - amountOnPlayer;
            if (getBank().withdraw(itemReq.getName(), requiredTargetAmount)) {
                Sleep.sleepUntil(() -> getAmountOnPlayer(itemReq) > amountOnPlayer, 1200, 600);
            }
        }

    }

    private long getAmountOnPlayer(final ItemReq itemReq) {
        return itemReq.getAmount(getInventory(), getEquipment());
    }
}
