package util.custom_method_provider;

import org.osbot.rs07.api.Settings;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.event.Event;
import util.widget.CachedWidget;
import util.widget.filters.WidgetActionFilter;

import java.util.Arrays;
import java.util.Optional;

public class ExtendedSettings extends Settings {

    public enum Setting {
        HIDE_ROOFS("Hide roofs", "Hide roofs"),
        SHIFT_CLICK_TO_DROP_ITEMS("Shift click to drop items", "Shift click");

        String name;
        String searchString;

        Setting(final String name, final String searchString) {
            this.name = name;
            this.searchString = searchString;
        }
    }

    private final CachedWidget searchWidget = new CachedWidget(w -> w.getMessage().contains("To search for a setting"));

    public enum AllSettingsTab {
        SEARCH("Search", 0xb, 0x1b, 0x2b, 0x3b, 0x4b, 0x5b, 0x70b),
        DISPLAY("Display", 0x8),
        AUDIO("Audio", 0x18),
        CHAT("Chat", 0x28),
        CONTROLS("Controls", 0x38),
        KEYBINDS("Keybinds", 0x48),
        GAMEPLAY("Gameplay", 0x58);

        static final int CONFIG_ID = 2855;
        String tabText;
        int[] configValues;

        AllSettingsTab(final String text, final int... configValues) {
            this.tabText = text;
            this.configValues = configValues;
        }
    }

    public enum BasicSettingsTab {
        CONTROLS("Controls", 0x0),
        AUDIO("Audio", 0x400),
        DISPLAY("Display", 0x800);

        static final int CONFIG_ID = 1795;
        String actionText;
        int configValue;

        BasicSettingsTab(String actionText, int configValue) {
            this.actionText = actionText;
            this.configValue = configValue;
        }
    }

    public boolean isAllSettingsTabOpen(final AllSettingsTab allSettingsTab) {
        final int configValue =  getConfigs().get(AllSettingsTab.CONFIG_ID);
        return Arrays.stream(allSettingsTab.configValues).anyMatch(value -> value == configValue);
    }

    public boolean isBasicSettingsTabOpen(final BasicSettingsTab basicSettingsTab) {
        return getConfigs().get(BasicSettingsTab.CONFIG_ID) == basicSettingsTab.configValue;
    }

    public boolean toggleSetting(final Setting setting) {
        return execute(new Event() {
            private boolean clickedSearchBar;
            private boolean typedString;

            @Override
            public int execute() throws InterruptedException {

                if (Tab.SETTINGS.isDisabled(getBot())) {
                    setFailed();
                } else if (getTabs().getOpen() != Tab.SETTINGS) {
                    getTabs().open(Tab.SETTINGS);
                } else if (!isAllSettingsWidgetVisible()) {
                    logger.debug("Opening all settings widget");
                    RS2Widget allSettingsWidget = getWidgets().getWidgetContainingText("All Settings");
                    if (allSettingsWidget != null) {
                        allSettingsWidget.interact();
                    }
                } else if (!clickedSearchBar) {
                    logger.debug("Clicking on search bar");
                    RS2Widget searchWidget = getWidgets().singleFilter(134, new WidgetActionFilter("Search"));
                    if (searchWidget != null) {
                        searchWidget.interact();
                    }
                    clickedSearchBar = true;
                    sleep(random(1000, 2000));
                } else if (!typedString) {
                    logger.debug("typing string");
                    for (char b : setting.searchString.toCharArray()) {
                        typedString = getKeyboard().typeKey(b);
                        sleep(random(200, 500));
                    }
                    getKeyboard().typeEnter();
                    sleep(random(1000, 2000));
                } else{
                    logger.debug("getting search result widget");
                    RS2Widget firstSearchResult = getFirstSearchResult();
                    if (firstSearchResult != null) {
                        if (firstSearchResult.interact()) {
                            sleep(random(1000, 2000));
                            getWidgets().closeOpenInterface();
                            sleep(random(1000, 2000));
                            clickedSearchBar = true;
                            typedString = false;
                            setFinished();
                        }
                    } else {
                        logger.debug("Failed to get Search result");
                        setFailed();
                    }
                }
                return 0;
            }

            private RS2Widget getFirstSearchResult() {
                RS2Widget[] searchResults = getWidgets().get(134, 18).getChildWidgets();
                Optional<RS2Widget> targetWidget = Arrays.stream(searchResults).filter(RS2Widget::isVisible).findFirst();
                return targetWidget.orElse(null);
            }
        }).hasFinished();
    }

    public boolean openAllSettingsTab(final AllSettingsTab allSettingsTab) {
        if (isAllSettingsTabOpen(allSettingsTab)) {
            return true;
        }
        RS2Widget targetWidget = getWidgets().singleFilter(widget -> widget.getMessage().equals(allSettingsTab.tabText));
        if (targetWidget != null) {
            return targetWidget.interact();
        }
        return false;
    }

    public boolean openBasicSettingsTab(final BasicSettingsTab basicSettingsTab) {
        if (isBasicSettingsTabOpen(basicSettingsTab)) {
            return true;
        }
        RS2Widget targetWidget = getWidgets().singleFilter(116, new WidgetActionFilter(basicSettingsTab.actionText));
        if (targetWidget != null) {
            return targetWidget.interact();
        }
        return false;
    }

    public boolean isAllSettingsWidgetVisible() {
        return getWidgets().singleFilter(widget -> widget.getMessage().equals("Settings")) != null;
    }
}
