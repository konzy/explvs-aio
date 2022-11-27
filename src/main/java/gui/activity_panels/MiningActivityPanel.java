package gui.activity_panels;

import activities.activity.Activity;
import activities.skills.mining.Mine;
import activities.skills.mining.MiningActivity;
import activities.skills.mining.Rock;
import activities.skills.mining.RuneEssMiningActivity;
import gui.styled_components.StyledJComboBox;
import gui.styled_components.StyledJLabel;
import gui.styled_components.StyledJPanel;
import org.json.simple.JSONObject;
import util.ResourceMode;

import javax.swing.*;
import java.awt.*;

public class MiningActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<Rock> rockSelector;
    private JComboBox<Mine> mineSelector;
    private JComboBox<ResourceMode> resourceModeSelector;

    public MiningActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new StyledJLabel("Rock: "));

        rockSelector = new StyledJComboBox<>(Rock.values());
        mainPanel.add(rockSelector);

        JLabel mineLabel = new StyledJLabel("Mine:");
        mainPanel.add(mineLabel);
        mineSelector = new StyledJComboBox<>(Mine.getMinesWithRock(Rock.values()[0]));
        mainPanel.add(mineSelector);

        mainPanel.add(new StyledJLabel("Collection Mode:"));
        resourceModeSelector = new StyledJComboBox<>(ResourceMode.values());
        mainPanel.add(resourceModeSelector);

        rockSelector.addActionListener(e -> {
            Rock selectedRock = (Rock) rockSelector.getSelectedItem();
            if (selectedRock == Rock.RUNE_ESSENCE) {
                mineLabel.setVisible(false);
                mineSelector.setVisible(false);
            } else {
                mineLabel.setVisible(true);
                mineSelector.setVisible(true);
                mineSelector.setModel(new DefaultComboBoxModel<>(Mine.getMinesWithRock(selectedRock)));
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        Rock selectedRock = (Rock) rockSelector.getSelectedItem();

        if (selectedRock == Rock.RUNE_ESSENCE) {
            return new RuneEssMiningActivity((ResourceMode) resourceModeSelector.getSelectedItem());
        }

        return new MiningActivity((Mine) mineSelector.getSelectedItem(), (Rock) rockSelector.getSelectedItem(), (ResourceMode) resourceModeSelector.getSelectedItem(), true);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rock", ((Rock) rockSelector.getSelectedItem()).name());
        jsonObject.put("mine", ((Mine) mineSelector.getSelectedItem()).name());
        jsonObject.put("resource_mode", ((ResourceMode) resourceModeSelector.getSelectedItem()).name());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        rockSelector.setSelectedItem(Rock.valueOf((String) jsonObject.get("rock")));
        mineSelector.setSelectedItem(Mine.valueOf((String) jsonObject.get("mine")));
        resourceModeSelector.setSelectedItem(ResourceMode.valueOf((String) jsonObject.get("resource_mode")));
    }
}
