package activities.skills.melee;

import activities.activity.Activity;
import activities.activity.ActivityType;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;

public class AttackDummyActivity extends Activity {
    Area ATTACK_DUMMY_AREA = new Area(3249, 3432, 3253, 3438);
    String DUMMY_NAME = "Dummy";
    String DUMMY_ACTION = "Attack";

    public AttackDummyActivity() {
        super(ActivityType.COMBAT);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (!ATTACK_DUMMY_AREA.contains(myPlayer())) {
            getWalking().webWalk(ATTACK_DUMMY_AREA.getCentralPosition());
        } else {
            RS2Object dummy = getObjects().closest(DUMMY_NAME);
            if (dummy != null && dummy.isVisible()) {
                dummy.interact(DUMMY_ACTION);
            } else {
                getWalking().webWalk(ATTACK_DUMMY_AREA.getRandomPosition());
            }
        }
    }

    @Override
    public Activity copy() {
        return null;
    }
}
