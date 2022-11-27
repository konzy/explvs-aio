package script;

import gui.Gui;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import paint.MouseTrail;
import paint.Paint;
import tasks.LevelTask;
import tasks.Task;
import tasks.TutorialIslandTask;
import tasks.task_executor.TaskExecutor;
import util.custom_method_provider.CustomMethodProvider;
import util.event.ConfigureClientEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ScriptManifest(author = "Explv", name = "Explv's AIO " + AIO.VERSION, info = "AIO", version = 0, logo = "http://i.imgur.com/58Zz0fb.png")
public class Tutorial extends Script {

    public static final String VERSION = "v3.4.0";

    private Gui gui;
    private Paint paint;
    private MouseTrail mouseTrail;
    private TaskExecutor taskExecutor;
    CustomMethodProvider customMethodProvider = new CustomMethodProvider();

    private boolean osrsClientIsConfigured;

    @Override
    public void onStart() throws InterruptedException {
        customMethodProvider.init(bot);

        log("Current version: " + AIO.VERSION);
        List<Task> tasks = new ArrayList<>();
        Task task = new TutorialIslandTask();
        tasks.add(task);

        taskExecutor = new TaskExecutor(tasks);
        taskExecutor.exchangeContext(getBot(), customMethodProvider);
        taskExecutor.addTaskChangeListener((oldTask, newTask) -> {
            paint.setCurrentTask(newTask);
        });
        taskExecutor.onStart();

        paint = new Paint(getBot(), taskExecutor.getSkillTracker());
        getBot().addPainter(paint);
        mouseTrail = new MouseTrail(getMouse(), 20, Color.CYAN);
        getBot().addPainter(mouseTrail);
    }

    @Override
    public int onLoop() throws InterruptedException {
        if (!getClient().isLoggedIn()) {
            return random(1500, 5000);
        } else if (!osrsClientIsConfigured && osrsClientIsConfigurable()) {
            configureOSRSClient();
            osrsClientIsConfigured = true;
        } else if (taskExecutor.isComplete()) {
            stop(true);
        } else {
            customMethodProvider.execute(taskExecutor);
        }
        int delay = 0;
        if(!getBot().isMirrorMode()) {
            delay = getRecommendedMirrorReactionTime();
        }
        return delay + random(100, 250);
    }

    private boolean osrsClientIsConfigurable() {
        return !Tab.SETTINGS.isDisabled(getBot()) &&
                !getDialogues().isPendingContinuation() &&
                !myPlayer().isAnimating() &&
                taskExecutor.getCurrentTask() != null &&
                !(taskExecutor.getCurrentTask() instanceof TutorialIslandTask) &&
                getNpcs().closest("Adventurer Jon") == null;
    }

    private void configureOSRSClient() throws InterruptedException {
        customMethodProvider.execute(new ConfigureClientEvent());
    }

    @Override
    public int getRecommendedMirrorReactionTime() {
        return 150;
    }

    @Override
    public int getRecommendedMirrorFps() {
        return 20;
    }

    @Override
    public void pause() {
        if (paint != null) {
            paint.pause();
        }
        if (customMethodProvider.getSkillTracker() != null) {
            customMethodProvider.getSkillTracker().pauseAll();
        }
    }

    @Override
    public void resume() {
        if (paint != null) {
            paint.resume();
        }
        if (customMethodProvider.getSkillTracker() != null) {
            customMethodProvider.getSkillTracker().resumeAll();
        }
    }

    @Override
    public void onExit() {
        if (gui != null && gui.isOpen()) {
            gui.close();
        }
        if (paint != null) {
            getBot().removePainter(paint);
        }
        if (mouseTrail != null) {
            getBot().removePainter(mouseTrail);
        }
    }
}
