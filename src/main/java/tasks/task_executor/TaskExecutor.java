package tasks.task_executor;

import org.osbot.rs07.randoms.BreakManager;
import org.osbot.rs07.script.RandomEvent;
import org.osbot.rs07.script.RandomSolver;
import tasks.LoopTask;
import tasks.Task;
import util.breaking.Break;
import util.breaking.MultiBreakManager;
import util.executable.Executable;
import util.executable.ExecutionFailedException;

import java.util.*;

public final class TaskExecutor extends Executable {

    private final List<Task> allTasks;
    private final Queue<Task> taskQueue = new LinkedList<>();
    private final List<TaskChangeListener> taskChangeListeners = new ArrayList<>();
    private final MultiBreakManager breakManager = new MultiBreakManager();

    private Task currentTask;

    public TaskExecutor(final List<Task> tasks) {
        allTasks = tasks;
        this.taskQueue.addAll(tasks);
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setTaskQueue(final List<Task> taskQueue) {
        this.taskQueue.clear();
        this.taskQueue.addAll(taskQueue);
        currentTask = null;
    }

    public final void addTaskChangeListener(final TaskChangeListener taskChangeListener) {
        this.taskChangeListeners.add(taskChangeListener);
    }

    public final void addTaskChangeListeners(final Collection<TaskChangeListener> taskChangeListeners) {
        this.taskChangeListeners.addAll(taskChangeListeners);
    }

    public boolean isComplete() {
        return taskQueue.isEmpty() && (currentTask == null || currentTask.isComplete());
    }

    @Override
    public void onStart() throws InterruptedException {
        breakManager.add(new Break(6.0/60.0, 10.0/60.0, 1.5, 3.0, false));
        breakManager.add(new Break(1.0, 2.0, 10.0, 15.0, false));
        breakManager.add(new Break(10, 20, 30, 45, true));
        breakManager.add(new Break(35, 60, 120, 200, true));

        breakManager.exchangeContext(getBot());
        getBot().getRandomExecutor().overrideOSBotRandom(breakManager);
        RandomSolver bm = getBot().getRandomExecutor().forEvent(RandomEvent.BREAK_MANAGER);
        logger.debug("BreakManager class=" + bm.getClass());
    }

    @Override
    public final void run() throws InterruptedException {
        if (currentTask == null || (currentTask.isComplete() && currentTask.canExit())) {
            loadNextTask(taskQueue);
        } else {
            runTask(currentTask);
        }
    }

    private void loadNextTask(Queue<Task> taskQueue) throws InterruptedException {
        if (currentTask != null) {
            getBot().removePainter(currentTask);
        }

        getSkillTracker().stopAll();

        Task prevTask = currentTask;
        currentTask = taskQueue.poll();

        if (currentTask == null) {
            return;
        }

        getBot().addPainter(currentTask);

        currentTask.exchangeContext(getBot(), this);

        if (currentTask instanceof LoopTask) {
            ((LoopTask) currentTask).setup(allTasks, taskChangeListeners);
        }

        currentTask.onStart();

        if (currentTask.getActivity() != null &&
                currentTask.getActivity().getActivityType() != null &&
                currentTask.getActivity().getActivityType().gainedXPSkills != null) {
            getSkillTracker().start(currentTask.getActivity().getActivityType().gainedXPSkills);
        }

        for (final TaskChangeListener taskChangeListener : taskChangeListeners) {
            taskChangeListener.taskChanged(prevTask, currentTask);
        }
    }

    private void runTask(final Task task) throws InterruptedException {
        try {
            execute(task);
        } catch (NullPointerException nullPointer) {
            log("Found null pointer exception. Task failed, exiting.");

            StackTraceElement[] stack = nullPointer.getStackTrace();
            for (StackTraceElement element : stack) {
                log(element.toString());
            }

            currentTask = null;
            taskQueue.clear();
        } catch (ExecutionFailedException executionFailedException) {
            log("Task execution failed due to error:");
            log(executionFailedException.getMessage());
            log("Proceeding to next task");
            loadNextTask(taskQueue);
        }
    }

}
