package activities.tutorial_island;

import activities.activity.Activity;
import org.osbot.rs07.api.ui.RS2Widget;
import util.Sleep;

import java.awt.event.KeyEvent;
import java.util.Random;

public final class TutorialIsland extends Activity {

    private final TutorialSection rsGuideSection = new RuneScapeGuideSection();
    private final TutorialSection survivalSection = new SurvivalSection();
    private final TutorialSection cookingSection = new CookingSection();
    private final TutorialSection questSection = new QuestSection();
    private final TutorialSection miningSection = new MiningSection();
    private final TutorialSection fightingSection = new FightingSection();
    private final TutorialSection bankSection = new BankSection();
    private final TutorialSection priestSection = new PriestSection();
    private final TutorialSection wizardSection = new WizardSection();
    private final Random rand = new Random();

    public TutorialIsland() {
        super(null);
    }

    @Override
    public void onStart() throws InterruptedException {
        Sleep.sleepUntil(() -> getClient().isLoggedIn() && myPlayer().isVisible(), 6000, 500);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (pendingContinue()) {
            selectContinue();
            return;
        }
        switch (getTutorialSection()) {
            case 0:
            case 1:
                logger.debug("Guide Section");
                execute(rsGuideSection);
                break;
            case 2:
            case 3:
                logger.debug("Survival Section");
                execute(survivalSection);
                break;
            case 4:
            case 5:
                logger.debug("Cooking Section");
                execute(cookingSection);
                break;
            case 6:
            case 7:
                logger.debug("Quest Section");
                execute(questSection);
                break;
            case 8:
            case 9:
                logger.debug("Mining Section");
                execute(miningSection);
                break;
            case 10:
            case 11:
            case 12:
                logger.debug("Fighting Section");
                execute(fightingSection);
                break;
            case 14:
            case 15:
                logger.debug("Bank Section");
                execute(bankSection);
                break;
            case 16:
            case 17:
                logger.debug("Priest Section");
                execute(priestSection);
                break;
            case 18:
            case 19:
            case 20:
                logger.debug("Wizard Section");
                execute(wizardSection);
                break;
        }
    }

    private int getTutorialSection() {
        return getConfigs().get(406);
    }

    private boolean pendingContinue() {
        RS2Widget continueWidget = getContinueWidget();
        return continueWidget != null && continueWidget.isVisible();
    }

    private boolean selectContinue() {
        RS2Widget continueWidget = getContinueWidget();
        if (continueWidget == null) {
            return false;
        }
        if (continueWidget.getMessage().contains("Click here to continue") || continueWidget.getMessage().contains("Click to continue")) {
            if (random(0, 100) < 33) {
                getKeyboard().pressKey(KeyEvent.VK_SPACE);
            } else {
                continueWidget.interact();
            }
            Sleep.sleepUntil(() -> !continueWidget.isVisible(), 1000, random(300, 700));
            return true;
        } else if (continueWidget.interact()) {
            Sleep.sleepUntil(() -> !continueWidget.isVisible(), 1000, random(300, 700));
            return true;
        }
        return false;
    }

    private RS2Widget getContinueWidget() {
        return getWidgets().singleFilter(getWidgets().getAll(),
                widget -> widget.isVisible()
                        && (widget.getMessage().contains("Click here to continue")
                        || widget.getMessage().contains("Click to continue"))
        );
    }

    @Override
    public TutorialIsland copy() {
        return new TutorialIsland();
    }
}
