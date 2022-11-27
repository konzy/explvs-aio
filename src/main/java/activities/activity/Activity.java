package activities.activity;


import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.canvas.paint.Painter;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.event.webwalk.PathPreferenceProfile;
import org.osbot.rs07.utility.Condition;
import util.Copyable;
import util.KonzyRandom;
import util.custom_method_provider.ExtendedSettings;
import util.executable.Executable;

import java.awt.*;

public abstract class Activity extends Executable implements Copyable<Activity>, Painter {

    private final ActivityType activityType;
    public boolean isComplete = false;
    private final long secondsUntilBreakToWait = 90L;

    /**
     * Current activity status to be displayed in the GUI
     */
    private String status = "";

    public Activity(final ActivityType activityType) {
        this.activityType = activityType;
    }

    /**
     * Update the activity status
     *
     * @param status to set
     */
    protected void setStatus(String status) {
        this.status = status;
    }

    public final ActivityType getActivityType() {
        return activityType;
    }

    public void dropAllExcept(String... names) {
        if (!getSettings().isShiftDropActive()) {
            getSettings().toggleSetting(ExtendedSettings.Setting.SHIFT_CLICK_TO_DROP_ITEMS);
        }
        getInventory().dropAllExcept(names);
    }

    public void dropAll(String... names) {
        if (!getSettings().isShiftDropActive()) {
            getSettings().toggleSetting(ExtendedSettings.Setting.SHIFT_CLICK_TO_DROP_ITEMS);
        }
        getInventory().dropAll(names);
    }


    public boolean runTo(Area area) {
        if (!area.contains(myPlayer())) {
            WebWalkEvent webWalkEvent = new WebWalkEvent(area);
            webWalkEvent.setMoveCameraDuringWalking(false);
            webWalkEvent.setEnergyThreshold(random(20, 40));
            webWalkEvent.setMinDistanceThreshold(0);
            return !runAndRetry(area, webWalkEvent);
        }
        return true;
    }

    private boolean runAndRetry(Area area, WebWalkEvent webWalkEvent) {
        logger.debug("Starting to walk to destination");
        execute(webWalkEvent);
        logger.debug("Walk to area complete");
        if (webWalkEvent.getCompletion() == 100.0 && !area.contains(myPlayer())) {
            logger.debug("We aren't in the specified area, trying to get in there");
            int tries = 0;
            int maxRetries = 10;
            while (!area.contains(myPlayer()) && tries < maxRetries) {
                logger.debug("try=" + tries);
                tries++;
                WebWalkEvent webWalkEventRetry = new WebWalkEvent(area.getRandomPosition());
                execute(webWalkEventRetry);
            }
            if (tries == maxRetries) {
                logger.debug("We failed to get in the specified area, quitting this task");
                isComplete = true;
                return true;
            }
        }
        return false;
    }


    public boolean runTo(Area area, Condition condition) {
        if (condition == null) return runTo(area);

        if (!condition.evaluate() || !area.contains(myPlayer())) {
            WebWalkEvent webWalkEvent = new WebWalkEvent(area);
            webWalkEvent.setMoveCameraDuringWalking(false);
            webWalkEvent.setEnergyThreshold(random(20, 40));
            webWalkEvent.setMinDistanceThreshold(0);
            webWalkEvent.setBreakCondition(condition);
            return !runAndRetry(area, webWalkEvent);
        }
        return true;
    }


    @Override
    public void run() throws InterruptedException {
//        if (getBot().getTimeUntilNextBreak() < secondsUntilBreakToWait) {
//            setStatus("Waiting for break");
//            sleep(secondsUntilBreakToWait * 1000);
//        }

        int extraBreak =  + (int)KonzyRandom.randomWait(); //TODO: Perhaps add this break to give some normal distribution
        sleep(random(350, 600));
        canExit = false;
        runActivity();
        canExit = true;
    }

    public abstract void runActivity() throws InterruptedException;

    public String toString() {
        return activityType != null ? String.format("%s : %s", activityType.toString(), status) : "";
    }

    @Override
    public void onPaint(Graphics2D graphics) {}
}
