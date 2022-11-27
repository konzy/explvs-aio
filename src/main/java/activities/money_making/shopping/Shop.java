package activities.money_making.shopping;

import org.osbot.rs07.api.map.Area;
import util.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public enum Shop {

    LUMBRIDGE_GENERAL(Arrays.asList("Shop keeper", "Shop assistant"), new Location("Lumbridge General Store", new Area(
            new int[][]{
                    { 3210, 3242 },
                    { 3208, 3244 },
                    { 3208, 3250 },
                    { 3210, 3252 },
                    { 3213, 3252 },
                    { 3215, 3250 },
                    { 3215, 3244 },
                    { 3213, 3242 }
            }
    ))),
    VARROCK_GENERAL( Arrays.asList("Shop keeper", "Shop assistant"), new Location("Varrock General Store", new Area(3214, 3411, 3220, 3419))),
    EDGEVILLE_GENERAL(Arrays.asList("Shop keeper", "Shop assistant"), new Location("Edgeville General Store", new Area(3077, 3507, 3083, 3513))),
    FALADOR_GENERAL(Arrays.asList("Shop keeper", "Shop assistant"), new Location("Falador General Store", new Area(
            new int[][]{
                    { 2956, 3385 },
                    { 2956, 3388 },
                    { 2953, 3388 },
                    { 2953, 3391 },
                    { 2961, 3391 },
                    { 2961, 3385 }
            }
    ))),
    GRAND_TREE_GROCERIES(Collections.singletonList("Hudo"), new Location("Grand Tree Groceries", new Area(2446, 3508, 2451, 3512).setPlane(1))),
    FUNCHS_FINE_GROCERIES(Collections.singletonList("Funch"), new Location("Funch's Fine Groceries (Grand Tree)", new Area(2488, 3485, 2494, 3490).setPlane(1))),
    FRENITAS_COOKERY_SHOP(Collections.singletonList("Frenita"), new Location("Frenita's Cookery Shop (Yanille)", new Area(
            new int[][]{
                    { 2565, 3093 },
                    { 2561, 3097 },
                    { 2561, 3101 },
                    { 2565, 3105 },
                    { 2569, 3105 },
                    { 2573, 3101 },
                    { 2573, 3097 },
                    { 2569, 3093 }
            }
    ))),
    LOGAVA_GRICOLLERS_COOKING_SUPPLIES(Collections.singletonList("Logava"), new Location("Logava Gricoller's Cooking Supplies (Hosidius)", new Area(1764, 3594, 1771, 3604))),
    NEITIZNOT_SUPPLIES(Collections.singletonList("Jofridr Mordstatter"), new Location("Neitiznot supplies (Neitiznot )", new Area(2333, 3804, 2340, 3809))),
    AL_KHARID_CRAFTING(Collections.singletonList("Dommik"), new Location("Dommik's Crafting Store (Al Kharid)", new Area(3318, 3191, 3323, 3197))),
    WARRIORS_GUILD_FOOD(Collections.singletonList("Lidio"), new Location("Warrior Guild Food Shop", new Area(2838, 3548, 2843, 3555))),
    FISHING_GUILD_SOUTH(Collections.singletonList("Gerrant"), new Location("Gerrant's Fishy Business (Port Sarim)", new Area(2603, 3410, 2614, 3417))),
    RANGING_GUILD_ARROWS(Collections.singletonList("Bow and Arrow salesman"), new Location("Dargaud's Bow and Arrows (Ranging Guild)", new Area(
            new int[][]{
                    { 2670, 3431 },
                    { 2669, 3432 },
                    { 2669, 3435 },
                    { 2671, 3437 },
                    { 2673, 3437 },
                    { 2675, 3435 },
                    { 2675, 3433 },
                    { 2673, 3431 }
            }
    ))),
    LOWES_ARCHERY_EMPORIUM(Collections.singletonList("Lowe"), new Location("Lowe's Archery Emporium (Varrock)", new Area(3230, 3420, 3236, 3427))),
    DARYLS_RANGING_SURPLUS(Collections.singletonList("Daryl"), new Location("Daryl's Ranging Surplus (Shayzien)", new Area(1531, 3547, 1568, 3586))),
    HICKTONS_ARCHERY_EMPORIUM(Collections.singletonList("Hickton"), new Location("Hickton's Archery Emporium", new Area(2821, 3441, 2825, 3445))),
    BOBS_BRILLIANT_AXES(Collections.singletonList("Bob"), new Location("Bob's Brilliant Axes", new Area(3228, 3201, 3233, 3205))),
    WARRIOR_GUILD_ARMOURY(Collections.singletonList("Anton"), new Location("Warrior Guild Armoury", new Area(
            new int[][]{
                    { 2861, 3533 },
                    { 2848, 3533 },
                    { 2848, 3538 },
                    { 2853, 3538 },
                    { 2853, 3541 },
                    { 2861, 3541 }
            }
    ).setPlane(1))),
    CANDLE_SHOP(Collections.singletonList("Candle maker"), new Location("Candle Shop (Catherby)", new Area(
            new int[][]{
                    { 2800, 3436 },
                    { 2797, 3439 },
                    { 2798, 3440 },
                    { 2796, 3442 },
                    { 2797, 3443 },
                    { 2799, 3443 },
                    { 2803, 3439 }
            }
    ))),
    WAYNES_CHAINS(Collections.singletonList("Wayne"), new Location("Wayne's Chains! (Falador)", new Area(2969, 3310, 2975, 3314))),
    THESSALINAS_FINE_CLOTHES(Collections.singletonList("Thessalia"), new Location("Thessalia's Fine Clothes (Varrock)", new Area(
            new int[][]{
                    { 3208, 3420 },
                    { 3204, 3420 },
                    { 3201, 3417 },
                    { 3207, 3411 },
                    { 3208, 3411 },
                    { 3209, 3412 },
                    { 3209, 3417 },
                    { 3210, 3418 }
            }
    ))),
    FANCY_CLOTHES_STORE(Collections.singletonList("Fancy dress shop owner"), new Location("Fancy Clothes Store (VarrockP2P)", new Area(
            new int[][]{
                    { 3278, 3395 },
                    { 3278, 3401 },
                    { 3284, 3401 },
                    { 3284, 3394 },
                    { 3280, 3394 },
                    { 3279, 3395 }
            }
    ))),
    FINE_FASHIONS(Collections.singletonList("Rometti"), new Location("Fine Fashions (Grand Tree)", new Area(2480, 3507, 2484, 3514).setPlane(1))),
    YRSAS_ACCOUTREMENTS(Collections.singletonList("Yrsa"), new Location("Yrsa's Accoutrements (Rellekka)", new Area(2622, 3672, 2626, 3676))),
    BARKERS_HABERDASHERY(Collections.singletonList("Barker"), new Location("Barkers' Haberdashery (Canifis)", new Area(3496, 3503, 3504, 3507))),
    LLETYA_SEAMSTRESS(Collections.singletonList("Oronwen"), new Location("Lletya Seamstress (Lletya)", new Area(
            new int[][]{
                    { 2326, 3174 },
                    { 2325, 3175 },
                    { 2325, 3178 },
                    { 2327, 3179 },
                    { 2328, 3178 },
                    { 2329, 3178 },
                    { 2330, 3177 },
                    { 2330, 3175 },
                    { 2329, 3174 }
            }
    ))),
    DODGY_MIKES_SECOND_HAND_CLOTHING(Collections.singletonList("Mike"), new Location("Dodgy Mike's Second Hand Clothing (Mos Le'Harmless)", new Area(
            new int[][]{
                    { 3688, 2974 },
                    { 3688, 2975 },
                    { 3687, 2976 },
                    { 3687, 2979 },
                    { 3688, 2980 },
                    { 3688, 2981 },
                    { 3692, 2981 },
                    { 3693, 2982 },
                    { 3696, 2982 },
                    { 3697, 2981 },
                    { 3697, 2974 },
                    { 3696, 2973 },
                    { 3693, 2973 },
                    { 3692, 2974 }
            }
    ))),
    MISCELLANIAN_CLOTHES_SHOP(Collections.singletonList("Halla"), new Location("Miscellanian Clothes Shop", new Area(2506, 10248, 2511, 10253))),
    AGMUNDI_QUALITY_CLOTHES(Collections.singletonList("Agmundi"), new Location("Agmundi Quality Clothes (Keldagrim)", new Area(2863, 10203, 2870, 10211))),
    VERMUNDIS_CLOTHES_STALL(Collections.singletonList("Vermundi"), new Location("Vermundi's Clothes Stall (Keldagrim)", new Area(2885, 10186, 2895, 10193))),
    VANESSAS_FARMING_SHOP(Collections.singletonList("Vanessa"), new Location("Vanessa's Farming shop (Catherby)", new Area(2818, 3459, 2821, 3464))),
    VANNAHS_FARMING_STALL(Collections.singletonList("Vannah"), new Location("Vannah's Farming Stall (Hosidius)", new Area(1759, 3591, 1766, 3598))),
    HARRYS_FISHING_SHOP(Collections.singletonList("Harry"), new Location("Harry's Fishing Shop (Catherby)", new Area(2830, 3440, 2837, 3446))),
    FOOD_STORE(Collections.singletonList("Wydin"), new Location("Food Store (Port Sarim)", new Area(
            new int[][]{
                    { 3012, 3203 },
                    { 3012, 3211 },
                    { 3014, 3211 },
                    { 3017, 3208 },
                    { 3017, 3203 }
            }
    ))),
    FUR_TRADER_EAST_ARDOUGNE(Collections.singletonList("Fur trader"), new Location("Fur trader (East Ardougne)", new Area(2662, 3293, 2669, 3299))),
    JATIXS_HERBLORE_SHOP(Collections.singletonList("Jatix"), new Location("Jatix's Herblore Shop (Taverly)", new Area(
            new int[][]{
                    { 2897, 3425 },
                    { 2895, 3427 },
                    { 2895, 3430 },
                    { 2897, 3432 },
                    { 2900, 3432 },
                    { 2902, 3430 },
                    { 2902, 3427 },
                    { 2900, 3425 }
            }
    ))),
    KARIM_THE_KEBAB_SELLER(Collections.singletonList("Karim"), new Location("Karim the Kebab Seller (Al Kharid)", new Area(3271, 3179, 3275, 3183))),
    ALIS_DISCOUNT_WARES(Collections.singletonList("Ali Morrisane"), new Location("Ali's Discount Wares (Al Kharid)", new Area(3299, 3207, 3306, 3215))),
    AUBURYS_RUNE_SHOP(Collections.singletonList("Aubury"), new Location("Aubury's Rune Shop (Varrock)", new Area(
            new int[][]{
                    { 3254, 3399 },
                    { 3252, 3399 },
                    { 3250, 3401 },
                    { 3250, 3403 },
                    { 3252, 3403 },
                    { 3252, 3405 },
                    { 3254, 3405 },
                    { 3256, 3403 },
                    { 3256, 3401 }
            }
    ))),
    BETTYS_MAGIC_EMPORIUM(Collections.singletonList("Betty"), new Location("Betty's Magic Emporium (Port Sarim)", new Area(3011, 3256, 3016, 3261))),
    LUNDAILS_ARENA_SIDE_RUNE_SHOP(Collections.singletonList("Lundail"), new Location("Lundail's Arena-side Rune Shop (Mage Arena)", new Area(2527, 4710, 2534, 4719))),
    ORE_STORE(Collections.singletonList("Hring Hring"), new Location("Ore store (Jatizso)", new Area(
            new int[][]{
                    { 2396, 3800 },
                    { 2399, 3800 },
                    { 2400, 3799 },
                    { 2402, 3799 },
                    { 2403, 3798 },
                    { 2403, 3797 },
                    { 2402, 3796 },
                    { 2397, 3796 },
                    { 2395, 3794 },
                    { 2393, 3794 },
                    { 2392, 3795 },
                    { 2392, 3798 },
                    { 2393, 3799 },
                    { 2395, 3799 }
            }
    ))),
    STONEMASON(Collections.singletonList("Stonemason"), new Location("Stonemason (Keldarim)", new Area(2846, 10182, 2851, 10186))),
    ORE_SELLER(Collections.singletonList("Ordan"), new Location("Ore Seller (Keldarim)", new Area(1935, 4965, 1937, 4968))),

    //Baba Yaga's Magic Shop - not sure this is able to be done like this
    PISCATORIS_FISHING_COLONY(Collections.singletonList("Arnold Lydspor"), new Location("Arnold's Eclectic Supplies (Piscatoris)", new Area(2307, 3697, 2312, 3703)));

    public List<String> shopKeepers;
    public Location location;

    Shop(final List<String> shopKeepers, final Location location) {

        this.shopKeepers = shopKeepers;
        this.location = location;
    }

    @Override
    public String toString() {
        return location.toString();
    }
}
