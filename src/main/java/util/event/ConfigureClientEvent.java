package util.event;

import org.osbot.rs07.api.Display;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.event.interaction.MouseMoveProfile;
import org.osbot.rs07.event.webwalk.PathPreferenceProfile;
import util.custom_method_provider.ExtendedSettings;
import util.executable.BlockingExecutable;
import util.widget.CachedWidget;

public class ConfigureClientEvent extends BlockingExecutable {

    private boolean isAudioDisabled = false;

    @Override
    protected void blockingRun() throws InterruptedException {
        MouseMoveProfile mmp = new MouseMoveProfile();
        mmp.setSpeedBaseTime(50);
//        logger.debug("getDeviation=" + mmp.getDeviation()); // 7
//        logger.debug("getFlowSpeedModifier=" + mmp.getFlowSpeedModifier()); //
//        logger.debug("getFlowVariety=" + mmp.getFlowVariety());
//        logger.debug("getNoise=" + mmp.getNoise()); // 2.5
//        logger.debug("getOvershoots=" + mmp.getOvershoots());// 2
//        logger.debug("getMinOvershootDistance=" + mmp.getMinOvershootDistance());//25
//        logger.debug("getMinOvershootTime=" + mmp.getMinOvershootTime()); // 375
//        logger.debug("getSpeedBaseTime=" + mmp.getSpeedBaseTime()); / 185


        if (getDisplay().isResizableMode()) {
            getDisplay().setDisplayMode(Display.DisplayMode.FIXED);
        } else
            if (!getSettings().areRoofsEnabled()) {
            getSettings().toggleSetting(ExtendedSettings.Setting.HIDE_ROOFS);
        } else
//            if (!getSettings().isShiftDropActive()) {
//            getSettings().toggleSetting(ExtendedSettings.Setting.SHIFT_CLICK_TO_DROP_ITEMS);
//        } else
            if (getSettings().isAllSettingsWidgetVisible()) {
            getWidgets().closeOpenInterface();
        } else {
            setFinished();
        }
    }

    void hideRoofs() throws InterruptedException {


        getWidgets().get(134, 24,49).interact(); // Display
        sleep(random(500, 1000));
        getWidgets().get(134, 18,53).interact(); // Checkbox
        sleep(random(500, 1000));
    }

    void shiftToDrop() throws InterruptedException {
        RS2Widget controls = getWidgets().get(134, 24,39);
        if (controls != null && controls.isVisible()) {
            controls.interact();
            sleep(random(500, 1000));
        }
        RS2Widget checkbox = getWidgets().get(134, 18,29);
        if (checkbox != null && checkbox.isVisible()) {
            checkbox.interact();
            sleep(random(500, 1000));
        }
    }
}
