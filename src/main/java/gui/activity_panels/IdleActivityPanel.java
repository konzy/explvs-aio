package gui.activity_panels;

import activities.activity.Activity;
import activities.money_making.idle.IdleActivity;
import activities.money_making.idle.Loot;
import gui.styled_components.*;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class IdleActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private StyledJCheckBox shouldLoot;
    private List<StyledJCheckBox> locations = new ArrayList<>();

    private StyledJTextField itemWhitelist;
    private StyledJTextField itemBlacklist;
    private StyledJTextField minItemValue;

    private HashSet<Loot> mLootHashSet = new HashSet<>();
    private boolean mShouldLoot = false;
    private List<String> mItemWhitelist = new ArrayList<>();
    private List<String> mItemBlacklist = new ArrayList<>();
    private int mMinItemValue = 0;


    public IdleActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new StyledJLabel("Should Loot: "));
        shouldLoot = new StyledJCheckBox();
        mainPanel.add(shouldLoot);

        for (Loot loot : Loot.values()) {
            mainPanel.add(new StyledJLabel(loot.name()));
            StyledJCheckBox lootCheckbox = new StyledJCheckBox();
            lootCheckbox.setEnabled(false);
            lootCheckbox.setText(loot.name());
            mainPanel.add(lootCheckbox);
            locations.add(lootCheckbox);
        }

        mainPanel.add(new StyledJLabel("Whitelist (separate with ','): "));
        itemWhitelist = new StyledJTextField();
        itemWhitelist.setEnabled(false);
        mainPanel.add(itemWhitelist);

        mainPanel.add(new StyledJLabel("Blacklist (separate with ','): "));
        itemBlacklist = new StyledJTextField();
        itemBlacklist.setEnabled(false);
        mainPanel.add(itemBlacklist);

        mainPanel.add(new StyledJLabel("Min Item Value: "));
        minItemValue = new StyledJTextField();
        minItemValue.setEnabled(false);
        mainPanel.add(minItemValue);

        shouldLoot.addActionListener(e -> {
            if (shouldLoot.isSelected()) {
                for (StyledJCheckBox box : locations) {
                    box.setEnabled(true);
                }
                itemWhitelist.setEnabled(true);
                itemBlacklist.setEnabled(true);
                minItemValue.setEnabled(true);
            } else {
                for (StyledJCheckBox box : locations) {
                    box.setEnabled(false);
                }
                itemWhitelist.setEnabled(false);
                itemBlacklist.setEnabled(false);
                minItemValue.setEnabled(false);
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        if (shouldLoot.isSelected()) {
            mShouldLoot = true;
            for (StyledJCheckBox box : locations) {
                if (box.isSelected()) {
                    mLootHashSet.add(Loot.valueOf(box.getText()));
                }
            }
            mItemWhitelist = Arrays.stream(itemWhitelist.getText().split(","))
                    .map(String::trim).collect(Collectors.toList());
            mItemBlacklist = Arrays.stream(itemBlacklist.getText().split(","))
                    .map(String::trim).collect(Collectors.toList());
            mMinItemValue = Integer.parseInt(minItemValue.getText());
        }

        return new IdleActivity(mItemWhitelist, mItemBlacklist, mMinItemValue, mShouldLoot, mLootHashSet);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mShouldLoot", mShouldLoot);
        jsonObject.put("mLootHashSet", String.join(",", mLootHashSet.stream().map(Loot::toString).collect(Collectors.toList())));
        jsonObject.put("mItemWhitelist", itemWhitelist.getText());
        jsonObject.put("mItemBlacklist", itemBlacklist.getText());
        jsonObject.put("mMinItemValue", mMinItemValue);

        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        shouldLoot.setSelected((Boolean) jsonObject.get("mShouldLoot"));

        List<String> lootLocations = Arrays.asList(((String) jsonObject.get("mLootHashSet")).split(","));
        for (StyledJCheckBox box : locations) {
            if (lootLocations.contains(box.getText())) {
                box.setSelected(true);
            }
        }
        itemWhitelist.setText((String) jsonObject.get("mItemWhitelist"));
        itemBlacklist.setText((String) jsonObject.get("mItemBlacklist"));
        minItemValue.setText((String) jsonObject.get("mMinItemValue"));
    }
}
