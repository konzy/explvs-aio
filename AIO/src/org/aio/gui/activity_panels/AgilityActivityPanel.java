package org.aio.gui.activity_panels;

import org.aio.activities.activity.Activity;
import org.aio.activities.skills.agility.AgilityActivity;
import org.aio.activities.skills.agility.AgilityCourse;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;

public class AgilityActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<AgilityCourse> courseSelector;

    public AgilityActivityPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new JLabel("Course:"));

        courseSelector = new JComboBox<>(AgilityCourse.values());
        mainPanel.add(courseSelector);
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        return new AgilityActivity((AgilityCourse) courseSelector.getSelectedItem());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("course", ((AgilityCourse) courseSelector.getSelectedItem()).name());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        courseSelector.setSelectedItem(AgilityCourse.valueOf((String) jsonObject.get("course")));
    }
}
