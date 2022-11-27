package gui.activity_panels;

import activities.activity.Activity;
import activities.money_making.idle.Loot;
import activities.money_making.leather_tanning.Leather;
import activities.money_making.leather_tanning.TannerActivity;
import activities.skills.woodcutting.Tree;
import gui.styled_components.*;
import org.json.simple.JSONObject;
import util.ResourceMode;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TanningActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<Leather> leatherSelector;

    public TanningActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new StyledJLabel("Leather: "));
        leatherSelector = new StyledJComboBox<>();
        mainPanel.add(leatherSelector);
        leatherSelector.setModel(new DefaultComboBoxModel<>(Leather.values()));

    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        return new TannerActivity((Leather) leatherSelector.getSelectedItem());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("leather",((Leather) leatherSelector.getSelectedItem()).name);
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        leatherSelector.setSelectedItem(Leather.valueOf((String) jsonObject.get("leather")));
    }
}
