package activities.skills.woodcutting;

import org.osbot.rs07.api.map.Area;
import util.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Tree {

    NORMAL("Tree",
            "Logs",
            1,
            new Location("Lumbridge", new Area(3183, 3205, 3199, 3237)),
            new Location("North Seers' Village", new Area(2706, 3499, 2717, 3504)),
            new Location("Draynor Village", new Area(3103, 3226, 3108, 3232)),
            new Location("West Varrock", new Area(3158, 3399, 3171, 3421)),
            new Location("East Varrock", new Area(3271, 3458, 3284, 3437)),
            new Location("North Falador", new Area(2940, 3393, 3029, 3419)),
            new Location("Inside Falador", new Area(2969, 3353, 3010, 3391)),
            new Location("Edgeville", new Area(
                    new int[][]{
                            { 3111, 3490 },
                            { 3122, 3498 },
                            { 3126, 3504 },
                            { 3131, 3514 },
                            { 3097, 3520 },
                            { 3072, 3518 },
                            { 3102, 3480 }
                    }
            ))
    ),
    EVERGREEN("Evergreen",
            "Logs",
            1,
            new Location("Monastery", new Area(3035, 3429, 3067, 3475))

    ),
    ACHEY("Achey tree", "Logs", 1, null),
    OAK("Oak",
            "Oak logs",
            15,
            new Location("Lumbridge", new Area(3202, 3237, 3206, 3247)),
            new Location("West Varrock", new Area(3160, 3410, 3171, 3423)),
            new Location("Seers' Village Bank", new Area(2731, 3490, 2734, 3494)),
            new Location("North Falador", new Area(2940, 3393, 3029, 3419)),
            new Location("Inside Falador", new Area(2969, 3353, 3010, 3391)),
            new Location("South Falador", new Area(2991, 3295, 3024, 3322)),
            new Location("East Varrock", new Area(3283, 3409, 3276, 3430))
    ),
    WILLOW("Willow",
            "Willow logs",
            30,
            new Location("Draynor Village", new Area(3080, 3238, 3091, 3224)),
            new Location("North Seers' Village", new Area(2707, 3506, 2714, 3514))
    ),
    TEAK("Teak", "Teak logs", 35,
            new Location("Castle Wars", new Area(2332, 3046, 2336, 3050)),
            new Location("Isle of Souls", new Area(2184, 2987, 2188, 2993))
    ),
    MAPLE("Maple tree", "Maple logs", 45,
            new Location("South Seers' Village Bank", new Area(2728, 3480, 2731, 3482)),
            new Location("North Seers' Village Bank", new Area(2720, 3498, 2734, 3502))),
    HOLLOW("Hollow tree", "Bark", 45, null),
    MAHOGANY("Magohany", "Mahogany logs", 50, null),
    ARCTIC_PINE("Arctic pine", "Arctic pine logs", 54, null),
    YEW("Yew",
            "Yew logs",
            60,
            new Location("Grand Exchange", new Area(
                    new int[][]{
                            {3201, 3501},
                            {3201, 3507},
                            {3226, 3507},
                            {3226, 3498},
                            {3207, 3498},
                            {3207, 3499},
                            {3206, 3499},
                            {3206, 3500},
                            {3205, 3500},
                            {3205, 3501}
                    }
            )),
            new Location("Falador", new Area(2994, 3309, 2998, 3314)),
            new Location("Port Sarim", new Area(3050, 3268, 3056, 3273)),
            new Location("Varrock South", new Area(3250, 3361, 3254, 3365)),
            new Location("Varrock Church", new Area(3247, 3470, 3251, 3475)),
            new Location("Edgeville", new Area(
                    new int[][]{
                            {3085, 3468},
                            {3085, 3483},
                            {3090, 3483},
                            {3090, 3474},
                            {3092, 3474},
                            {3092, 3468}
                    })),
            new Location("Seers' Village", new Area(
                    new int[][]{
                            {2704, 3457},
                            {2704, 3467},
                            {2708, 3467},
                            {2708, 3461},
                            {2718, 3461},
                            {2718, 3457}
                    }
            )),
            new Location("Catherby", new Area(new int[][]{
                    {2761, 3425},
                    {2761, 3428},
                    {2762, 3428},
                    {2762, 3433},
                    {2759, 3433},
                    {2759, 3435},
                    {2752, 3435},
                    {2752, 3427},
                    {2757, 3427},
                    {2757, 3425}
            }))
    ),
    MAGIC("Magic tree",
            "Magic logs",
            75,
            new Location("Sorcerer's Tower", new Area(2698, 3395, 2707, 3400))
    );

    public String name;
    public String logsName;
    public Location[] locations;
    public int reqLevel;

    Tree(final String name, final String logsName, final int reqLevel, final Location... locations) {
        this.name = name;
        this.logsName = logsName;
        this.locations = locations;
        this.reqLevel = reqLevel;
    }

    public static List<String> allLogNames() {
        return Arrays.stream(values()).map(tree -> tree.logsName).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return name;
    }
}

