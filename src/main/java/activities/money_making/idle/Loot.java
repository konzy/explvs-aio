package activities.money_making.idle;

import org.osbot.rs07.api.map.Area;

public enum Loot {

    GRAND_EXCHANGE("Grand Exchange", new Area(3141, 3468, 3185, 3513)),
    BARBARIAN_FISHING("Barbarian Fishing", new Area(
            new int[][]{
                    { 3101, 3421 },
                    { 3105, 3421 },
                    { 3111, 3434 },
                    { 3108, 3438 },
                    { 3101, 3438 }
            }
    )),
    WILDERNESS_GREEN_DRAGONS("Wilderness Green Dragons", new Area(3324, 3674, 3365, 3725)),
    COWS_EAST("Cows", new Area(
            new int[][]{
                    { 3265, 3255 },
                    { 3253, 3255 },
                    { 3253, 3274 },
                    { 3243, 3287 },
                    { 3243, 3298 },
                    { 3266, 3298 }
            }
    ));

    String name;
    Area area;

    Loot(final String name, final Area area) {
        this.name = name;
        this.area = area;
    }

    @Override
    public String toString() {
        return name;
    }
}
