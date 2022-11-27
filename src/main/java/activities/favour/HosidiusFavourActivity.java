package activities.favour;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.Banking;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.Player;
import util.Sleep;
import util.executable.Executable;

import java.util.*;
import java.util.stream.Collectors;

public class HosidiusFavourActivity extends Activity {
    Area hammerArea = new Area(1773, 3502, 1780, 3509);
    Area fieldArea = new Area(1762, 3521, 1780, 3557);

    Area[] walkingAreas = {
            new Area(1780, 3552, 1762, 3552),
            new Area(1762, 3548, 1780, 3548),
            new Area(1772, 3539, 1772, 3521),
            new Area(1768, 3521, 1768, 3539)
    };

    List<Position> startingPositions = Arrays.asList(new Position(1780, 3552, 0),
            new Position(1780, 3548, 0),
            new Position(1762, 3552, 0),
            new Position(1762, 3548, 0),
            new Position(1768, 3539, 0),
            new Position(1772, 3539, 0),
            new Position(1768, 3521, 0),
            new Position(1772, 3521, 0));

    Area[] ploughAreas = {
            new Area(1762, 3551, 1780, 3553),
            new Area(1762, 3547, 1780, 3549),
            new Area(1771, 3521, 1773, 3539),
            new Area(1769, 3521, 1767, 3539)
    };

    Area[] ploughTurnAroundAreas = {
            new Area(1780, 3543, 1777, 3549),
            new Area(1777, 3551, 1780, 3557),
            new Area(1762, 3543, 1765, 3549),
            new Area(1762, 3551, 1765, 3557),
            new Area(1777, 3539, 1771, 3536),
            new Area(1769, 3539, 1763, 3536),
            new Area(1763, 3521, 1769, 3524),
            new Area(1771, 3521, 1777, 3524)
    };

    Area[] playerTurnAroundAreas = {
            new Area(1766, 3557, 1766, 3543),
            new Area(1776, 3557, 1776, 3543),
            new Area(1763, 3535, 1777, 3535),
            new Area(1777, 3525, 1763, 3525)
    };


    private Executable bankNode = new HosidiusFavourActivity.FavourBank();
    private NPC myPlough;
    private Area myPlowArea;
    private Area myWalkingArea;

    private static final String HAMMER = "Hammer";
    private static final String TAKE = "Take";
    private static final String REPAIR = "Repair";
    private static final String PUSH = "Push";

    public HosidiusFavourActivity() {
        super(ActivityType.FAVOUR);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if(getInventory().isFull()) {
            setStatus("Full Inventory, Banking");
            execute(bankNode);
        } else if (!getInventory().contains(HAMMER) && getGroundItems().closestThatContains(HAMMER) != null && getGroundItems().closestThatContains(HAMMER).isVisible()) {
            setStatus("Picking up Hammer");
            getGroundItems().closestThatContains(HAMMER).interact(TAKE);
            Sleep.sleepUntil(() -> getInventory().contains(HAMMER), 10000, random(500, 1000));
        } else if (!getInventory().contains(HAMMER)) {
            setStatus("Walking to Hammer");
            getWalking().webWalk(hammerArea);
            if (getGroundItems().closestThatContains(HAMMER) == null || !getGroundItems().closestThatContains(HAMMER).isVisible()) {
                setStatus("Getting closer to Hammer");
                getWalking().webWalk(getGroundItems().closestThatContains(HAMMER).getPosition());
            }
        } else if (!fieldArea.contains(myPlayer())) {
            setStatus("Walking to plough area");
            getWalking().webWalk(fieldArea.getRandomPosition());
        } else if (myPlough == null) {
            setStatus("Picking a plough");
            logger.info("Picking a plough");
            pickPlough();
        } else if (!myWalkingArea.contains(myPlayer()) || shouldTurnAround()) {
            setStatus("Moving to side of plough");
            Position pos = turnAroundPosition();
            getWalking().webWalk(pos);
            Sleep.sleepUntil(() -> myPlayer().getPosition().equals(pos) && !myPlayer().isMoving(), 5_000);
            if(!myPlayer().getPosition().equals(pos)) {
                pos.interact(getBot(), "Walk here");
                Sleep.sleepUntil(() -> myPlayer().getPosition().equals(pos) && !myPlayer().isMoving(), 10_000);
            }
        } else if (Arrays.asList(myPlough.getActions()).contains(REPAIR)) { // repair
            setStatus("Repairing Plough");
            myPlough.interact(REPAIR);
            Sleep.sleepUntil(() -> !Arrays.asList(myPlough.getActions()).contains(REPAIR), 15_000);
            sleep(random(1000, 3_000));
        } else if (myPlowArea.contains(myPlayer())) { // push
            setStatus("Pushing Plough");
            myPlough.interact(PUSH);
            Sleep.sleepUntil(() -> Arrays.asList(myPlough.getActions()).contains(REPAIR) || shouldTurnAround(), 30_000);
            sleep(random(1000, 3_000));
        }

    }

    @Override
    public Activity copy() {
        return null;
    }

    private void pickPlough() {
        List<Player> players = getPlayers().getAll();
        if (myPlowArea == null) {
            Arrays.stream(ploughAreas)
                    .filter(area -> players.stream().noneMatch(area::contains))
                    .findAny()
                    .ifPresent(area -> myPlowArea = area);

            getWalking().walk(myPlowArea.getCentralPosition());

            getNpcs().getAll().stream()
                    .filter(npc -> myPlowArea.contains(npc))
                    .findAny()
                    .ifPresent(npc -> myPlough = npc);

            Arrays.stream(walkingAreas)
                    .filter(area -> myPlowArea.contains(area.getCentralPosition()))
                    .findAny()
                    .ifPresent(area -> myWalkingArea = area);
        } else {
            getNpcs().getAll().stream()
                    .filter(npc -> myPlowArea.contains(npc))
                    .findAny()
                    .ifPresent(npc -> myPlough = npc);
        }
    }

    private Position turnAroundPosition() {
        int dist = 9001;
        Position shortestPosition = startingPositions.get(0);
        for (Position p: startingPositions) {
            int currentDistance = p.distance(myPlough);
            if (currentDistance < dist) {
                dist = currentDistance;
                shortestPosition = p;
            }
        }
        return shortestPosition;
    }

    private boolean shouldTurnAround() {
        logger.info("playerAtEndOfRow=" + playerAtEndOfRow());
        logger.info("ploughAtEndOfRow=" + ploughAtEndOfRow());
        return playerAtEndOfRow() && ploughAtEndOfRow();
    }

    private boolean playerAtEndOfRow() {
        for (Area area: playerTurnAroundAreas){
            if (area.contains(myPlayer())) return true;
        }
        return false;
    }

    private boolean ploughAtEndOfRow() {
        for (Area area: ploughTurnAroundAreas){
            if (area.contains(myPlough)) return true;
        }
    return false;
    }

    private class FavourBank extends Banking {
        @Override
        public void bank(final BankType currentBankType) {
            if (getInventory().isFull()) {
                getBank().depositAll();
                getBank().close();
            }
        }
    }
}

