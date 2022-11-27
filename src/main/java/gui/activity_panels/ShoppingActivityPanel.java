package gui.activity_panels;

import activities.activity.Activity;
import activities.money_making.shopping.Shop;
import activities.money_making.shopping.ShoppingActivity;
import gui.styled_components.*;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.min;

public class ShoppingActivityPanel implements ActivityPanel {

    private JPanel mainPanel;

    private StyledJTextField itemsToBuy;
    private StyledJTextField quantityToLeave;
    private JComboBox<Shop> shopSelector;


    private List<String> mItemsToBuy = new ArrayList<>();
    private int mQuantityToLeave = 0;


    public ShoppingActivityPanel() {
        mainPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        mainPanel.add(new StyledJLabel("Shop: "));
        shopSelector = new StyledJComboBox<>();
        shopSelector.setModel(new DefaultComboBoxModel<>(Shop.values()));
        mainPanel.add(shopSelector);

        mainPanel.add(new StyledJLabel("Items"));
        itemsToBuy = new StyledJTextField();
        itemsToBuy.setColumns(10);
        mainPanel.add(itemsToBuy);

        mainPanel.add(new StyledJLabel("Quantity"));
        quantityToLeave = new StyledJTextField();
        quantityToLeave.setColumns(10);
        mainPanel.add(quantityToLeave);
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Activity toActivity() {
        HashMap<String, Integer> itemsToBuyHashmap = new HashMap<>();

        String[] itemList = itemsToBuy.getText().split(",");
        String[] quantityList =  quantityToLeave.getText().split(",");

        for (int i = 0; i < min(itemList.length, quantityList.length); i++) {
            itemsToBuyHashmap.put(itemList[i], Integer.parseInt(quantityList[i]));
        }

        Shop shop = (Shop) shopSelector.getSelectedItem();

        return new ShoppingActivity(itemsToBuyHashmap, shop);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("itemsToBuy", itemsToBuy.getText());
        jsonObject.put("quantityToLeave", quantityToLeave.getText());
        jsonObject.put("shopSelector", ((Shop) shopSelector.getSelectedItem()).name());

        return jsonObject;
    }

    @Override
    public void fromJSON(JSONObject jsonObject) {
        itemsToBuy.setText((String) jsonObject.get("itemsToBuy"));
        quantityToLeave.setText((String) jsonObject.get("quantityToLeave"));
        shopSelector.setSelectedItem(Shop.valueOf((String) jsonObject.get("shopSelector")));
    }
}
