package activities.favour;

public enum Town {

    ARCEUUS("Arceuus"),
    HOSIDIUS("Hosidius"),
    LOVAKENGJ("Lovakengj"),
    PISCARILIUS("Piscarilius");

    public String name;

    Town(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        char[] name = name().toLowerCase().replace("_", " ").toCharArray();
        name[0] = Character.toUpperCase(name[0]);
        return new String(name);
    }
}