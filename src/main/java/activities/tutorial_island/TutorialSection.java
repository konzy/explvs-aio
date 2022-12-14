package activities.tutorial_island;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.RS2Widget;
import util.Sleep;
import util.executable.Executable;

import java.awt.event.KeyEvent;
import java.util.Random;

public abstract class TutorialSection extends Executable {
    private final String INSTRUCTOR_NAME;

    public TutorialSection(final String INSTRUCTOR_NAME) {
        this.INSTRUCTOR_NAME = INSTRUCTOR_NAME;
    }

    protected final int getProgress() {
        return getConfigs().get(281);
    }

    protected final void talkToInstructor() {
        if (getInstructor().interact("Talk-to")) {
            Sleep.sleepUntil(this::pendingContinue, 5000, 600);
        }
    }

    protected NPC getInstructor() {
        return getNpcs().closest(INSTRUCTOR_NAME);
    }

    protected boolean pendingContinue() {
        RS2Widget continueWidget = getContinueWidget();
        return continueWidget != null && continueWidget.isVisible();
    }

    private RS2Widget getContinueWidget() {
        return getWidgets().singleFilter(getWidgets().getAll(),
                widget -> widget.isVisible()
                        && (widget.getMessage().contains("Click here to continue")
                        || widget.getMessage().contains("Click to continue"))
        );
    }
}
