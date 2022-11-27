package activities.skills.cooking;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import util.Location;

public enum CookingLocation {

    FALADOR_RANGE(new Location("Falador", new Area(3037, 3343, 3039, 3345)), CookingObject.RANGE),
    EDGEVILLE_STOVE(new Location("Edgeville Stove", new Area(3077, 3493, 3079, 3496)), CookingObject.STOVE),
//    EDGEVILLE_RANGE(new Location("Edgeville Range", new Area(new Position(3083, 3509, 1), new Position(3080, 3508, 1))), CookingObject.RANGE),
    CATHERBY_RANGE(new Location("Catherby Range", new Area(2815, 3441, 2818, 3444)), CookingObject.RANGE),
    LUMBRIDGE_RANGE(new Location("Lumbridge Range", new Area(3207, 3213, 3212, 3216)), CookingObject.RANGE),
    ROGUES_DEN_FIRE(new Location("Rogue's Den Fire",  new Area(3039, 4969, 3043, 4974).setPlane(1)), CookingObject.FIRE);

    public Location location;
    public CookingObject cookingObject;

    CookingLocation(final Location location, final CookingObject cookingObject) {
        this.location = location;
        this.cookingObject = cookingObject;
    }

    @Override
    public String toString() {
        return location.toString();
    }
}
