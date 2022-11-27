package gui.activity_panels;

import activities.activity.Activity;
import activities.favour.HosidiusFavourActivity;
import activities.favour.Town;
import activities.money_making.shopping.Shop;
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

public class FavourActivityPanel implements ActivityPanel {

    private JPanel mainPanel;
    private JComboBox<Town> townSelector;


    public FavourActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new StyledJLabel("Town: "));

        townSelector = new StyledJComboBox<>(Town.values());
        mainPanel.add(townSelector);

    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        Town selectedTown = (Town) townSelector.getSelectedItem();
        switch(selectedTown) {
            case HOSIDIUS:
                return new HosidiusFavourActivity();
            case ARCEUUS:
            case LOVAKENGJ:
            case PISCARILIUS:
                return null;
        }

        return null;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("townSelector", ((Town) townSelector.getSelectedItem()).name());
        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        townSelector.setSelectedItem(Town.valueOf((String) jsonObject.get("townSelector")));
    }
}
