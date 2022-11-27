package activities.skills.magic;

public enum Rune {
    AIR("Air rune", true),
    MIND("Mind rune", true),
    WATER("Water rune", true),
    EARTH("Earth rune", true),
    FIRE("Fire rune", true),
    BODY("Body rune"),
    COSMIC("Cosmic rune"),
    CHAOS("Chaos rune"),
    NATURE("Nature rune"),
    LAW("Law rune"),
    DEATH("Death rune"),
    ASTRAL("Astral rune"),
    BLOOD("Blood rune"),
    SOUL("Soul rune"),
    WRATH("Wrath rune"),
    MIST("Mist rune"),
    DUST("Dust rune"),
    MUD("Mud rune"),
    SMOKE("Smoke rune"),
    STEAM("Steam rune"),
    LAVA("Lava rune");

    private String name;
    private boolean elemental;

    Rune(final String name, final boolean elemental) {
        this.name = name;
        this.elemental = elemental;
    }

    Rune(final String name) {
        this.name = name;
        this.elemental = false;
    }

    public String toString() {
        return name;
    }
}
