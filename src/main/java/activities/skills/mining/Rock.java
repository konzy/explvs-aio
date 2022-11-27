package activities.skills.mining;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Rock {

    CLAY(new short[]{6705}, "Clay"),
    COPPER(new short[]{4645, 4510}, "Copper ore"),
    TIN(new short[]{53}, "Tin ore"),
    IRON(new short[]{2576}, "Iron ore"),
    SILVER(new short[]{74}, "Silver"),
    COAL(new short[]{10508}, "Coal"),
    GOLD(new short[]{8885}, "Gold"),
    MITHRIL(new short[]{-22239}, "Mithril ore"),
    ADAMANTITE(new short[]{21662}, "Adamantite ore"),
    RUNITE(new short[]{-31437}, "Runite ore"),
    RUNE_ESSENCE(null, "Rune essence");

    public String oreName;
    public short[] colours;

    Random random = new Random();

    Rock(final short[] colours, final String oreName) {
        this.colours = colours;
        this.oreName = oreName;
    }

    public RS2Object getClosestWithOre(final MethodProvider methods, final Filter<RS2Object>... filters) {
        //noinspection unchecked
        int rand = random.nextInt(100);
        if (rand > 90) {
            List<RS2Object> ores = methods.getObjects().getAll().stream()
                    .filter(obj -> Stream.of(filters).allMatch(f -> f.match(obj))
                            && hasOre(obj)).collect(Collectors.toList());
            Collections.shuffle(ores);
            return ores.get(0);
        }

        RS2Object courteousRock = methods.getObjects().closest(
                obj -> Stream.of(filters).allMatch(f -> f.match(obj))
                        && hasOre(obj)
                        && distanceFromAnotherPlayer(methods, obj) > 1
        );

        if (courteousRock == null) {
            return methods.getObjects().closest(
                    obj -> Stream.of(filters).allMatch(f -> f.match(obj))
                            && hasOre(obj)
            );
        }

        return courteousRock;
    }

    public double distanceFromAnotherPlayer(final MethodProvider methods, RS2Object ore) {
        int distance = 100;
        Player myPlayer = methods.myPlayer();
        for (Player player : methods.getPlayers().getAll()) {
            if (!player.equals(myPlayer)) {
                int tempDistance = player.getPosition().distance(ore.getPosition());
                distance = Math.min(distance, tempDistance);
            }
        }
        return distance;
    }


    public static List<String> allOreNames() {
        return Arrays.stream(values()).map(rock -> rock.oreName).collect(Collectors.toList());
    }

    public static String[] allOreNamesArray() {
        Object[] arr = Rock.allOreNames().toArray();

        String[] ores = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ores[i] = (String)arr[i];
        }

        return ores;
    }

    public boolean hasOre(final Entity rockEntity) {
        if (rockEntity.getDefinition() == null) {
            return false;
        }

        short[] colours = rockEntity.getDefinition().getModifiedModelColors();

        if (colours == null) {
            return false;
        }

        for (short rockColour : this.colours) {
            for (short entityColour : colours) {
                if (rockColour == entityColour) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        char[] name = name().toLowerCase().replace("_", " ").toCharArray();
        name[0] = Character.toUpperCase(name[0]);
        return new String(name);
    }
}