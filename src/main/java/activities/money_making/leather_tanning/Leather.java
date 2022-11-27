package activities.money_making.leather_tanning;


import org.osbot.rs07.api.map.Area;
import util.Location;
import util.item_requirement.ItemReq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Leather {

    SOFT_LEATHER("Soft Leather", "Leather", 148, new ItemReq("Cowhide", 1), new ItemReq("Coins", 1).setStackable()),
    HARD_LEATHER("Hard Leather", "Hard leather", 149, new ItemReq("Cowhide", 1), new ItemReq("Coins", 3).setStackable()),
    SNAKESKIN("Snakeskin", "Snakeskin", 150, new ItemReq("Snake hide", 1), new ItemReq("Coins", 20).setStackable()),
    SNAKESKIN_SWAMP("Snakeskin (Swamp)", "Snakeskin",151, new ItemReq("Snake hide (Swamp)", 1), new ItemReq("Coins", 15).setStackable()),
    GREEN_DRAGONHIDE("Green D'Hide", "Green dragon leather", 152, new ItemReq("Green dragonhide", 1), new ItemReq("Coins", 20).setStackable()),
    BLUE_DRAGONHIDE("Blue D'Hide", "Blue dragon leather",153, new ItemReq("Blue dragonhide", 1), new ItemReq("Coins", 20).setStackable()),
    RED_DRAGONHIDE("Red D'Hide", "Red dragon leather", 154, new ItemReq("Red dragonhide", 1), new ItemReq("Coins", 20).setStackable()),
    BLACK_DRAGONHIDE("Black D'Hide", "Black dragon leather",155, new ItemReq("Black dragonhide", 1), new ItemReq("Coins", 20).setStackable());

    public String name;
    public String leatherName;
    public int childId;
    public ItemReq[] itemReqs;

    Leather(final String name, final String leatherName, final int childId, final ItemReq... itemReqs) {
        this.name = name;
        this.leatherName = leatherName;
        this.childId = childId;
        this.itemReqs = itemReqs;
    }

    @Override
    public String toString() {
        return name;
    }
}

