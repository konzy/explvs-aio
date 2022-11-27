package util.breaking;

import org.osbot.rs07.api.Client;
import org.osbot.rs07.script.RandomEvent;
import org.osbot.rs07.script.RandomSolver;

import java.util.ArrayList;

public class MultiBreakManager extends RandomSolver {

    ArrayList<Break> breakList = new ArrayList<>();
    Break currentBreak = null;

    public MultiBreakManager() {
        super(RandomEvent.BREAK_MANAGER);
    }

    public void add(Break br) {
        breakList.add(br);
        br.calculateNextBreak();
        currentBreak = br;
    }

    @Override
    public boolean shouldActivate() {
        for (Break br : breakList) {
            br.calculateNextBreak();
            if (currentBreak == null) {
                currentBreak = br;
            }
        }

        if (currentBreak.breakStart < System.currentTimeMillis()) {
            return true;
        }

        for (Break br : breakList) {
            if (br.breakStart < System.currentTimeMillis()) {
                currentBreak = br;
                return true;
            }
        }
        return false;
    }

    @Override
    public int onLoop() throws InterruptedException {
        System.out.println("Breaking... millis remaining=" + (currentBreak.breakEnd - System.currentTimeMillis()));

        if (currentBreak.shouldLogout && getClient().getLoginState() == Client.LoginState.LOGIN_SUCCESSFUL) {
            if (getWidgets().closeOpenInterface() && getLogoutTab().logOut()) {
                return 10000;
            }
        }
        if (getMouse().isOnScreen() && random(0, 100) > 90) {
            getMouse().moveOutsideScreen();
        }
        return random(250, 750);
    }
}
