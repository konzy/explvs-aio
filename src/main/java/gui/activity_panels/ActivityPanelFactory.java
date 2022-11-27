package gui.activity_panels;

import activities.activity.ActivityType;

public class ActivityPanelFactory {
    public static ActivityPanel createActivityPanel(final ActivityType activityType) {
        switch (activityType) {
            case WOODCUTTING:
                return new WCActivityPanel();
            case AGILITY:
                return new AgilityActivityPanel();
            case FLETCHING:
                return new FletchActivityPanel();
            case FISHING:
                return new FishingActivityPanel();
            case SMITHING:
                return new SmithActivityPanel();
            case MINING:
                return new MiningActivityPanel();
            case FIREMAKING:
                return new FMActivityPanel();
            case MAGIC:
                return new MagicActivityPanel();
            case RUNECRAFTING:
                return new RCActivityPanel();
            case THIEVING:
                return new ThievingActivityPanel();
            case HERBLORE:
                return new HerbloreActivityPanel();
            case MONEY_MAKING:
                return new MoneyMakingActivityPanel();
            case TANNING:
                return new TanningActivityPanel();
            case IDLING:
                return new IdleActivityPanel();
            case SHOPPING:
                return new ShoppingActivityPanel();
            case CRAFTING:
                return new CraftingActivityPanel();
            case RANGED:
                return new RangedActivityPanel();
            case COOKING:
                return new CookingActivityPanel();
            case FAVOUR:
                return new FavourActivityPanel();
        }
        return null;
    }
}
