package gui.activity_panels;

import activities.activity.Activity;
import activities.skills.magic.MagicActivity;
import activities.skills.magic.Spell;
import activities.skills.magic.SpellTarget;
import activities.skills.magic.Staff;
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

public class MagicActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<Spell> spellSelector;
    private JComboBox<Staff> staffSelector;
    private JTextField targetTextField;

    public MagicActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new StyledJLabel("Spell: "));
        spellSelector = new StyledJComboBox<>(Spell.values());
        mainPanel.add(spellSelector);

        mainPanel.add(new StyledJLabel("Staff: "));
        staffSelector = new StyledJComboBox<>(Staff.values());
        mainPanel.add(staffSelector);

        mainPanel.add(new StyledJLabel("Target: "));
        targetTextField = new JTextField();
        targetTextField.setColumns(20);
        mainPanel.add(targetTextField);
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        return new MagicActivity((Spell) spellSelector.getSelectedItem(), (Staff) staffSelector.getSelectedItem(), targetTextField.getText());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("spell", ((Spell) spellSelector.getSelectedItem()).name());
        jsonObject.put("staff", ((Staff) staffSelector.getSelectedItem()).name());
        jsonObject.put("targetName", targetTextField.getText());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        spellSelector.setSelectedItem(Spell.valueOf((String) jsonObject.get("spell")));
        staffSelector.setSelectedItem(Staff.valueOf((String) jsonObject.get("staff")));
        targetTextField.setText((String) jsonObject.get("targetName"));
    }
}
