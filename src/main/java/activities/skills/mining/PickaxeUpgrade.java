package activities.skills.mining;


import org.osbot.rs07.api.ui.Skill;
import org.osbot.utility.Logger;
import util.MyTool;
import util.MyToolUpgrade;
import util.custom_method_provider.CustomMethodProvider;

import java.util.Arrays;
import java.util.List;

public class PickaxeUpgrade extends MyToolUpgrade {

    MyTool BRONZE = new MyTool("Bronze pickaxe", 1, 1, Skill.MINING, false);
    MyTool IRON = new MyTool("Iron pickaxe", 1, 1, Skill.MINING, false);
    MyTool STEEL = new MyTool("Steel pickaxe", 6, 5, Skill.MINING, false);
    MyTool BLACK = new MyTool("Black pickaxe", 11, 10, Skill.MINING, false);
    MyTool MITHRIL = new MyTool("Mithril pickaxe", 21, 20, Skill.MINING, false);
    MyTool ADAMANT = new MyTool("Adamant pickaxe", 31, 30, Skill.MINING, false);
    MyTool RUNE = new MyTool("Rune pickaxe", 41, 40, Skill.MINING, false);
    MyTool DRAGON = new MyTool("Dragon pickaxe", 61, 60, Skill.MINING, true);
    MyTool INFERNAL = new MyTool("Infernal pickaxe", 61, 60, Skill.MINING, true);

    public PickaxeUpgrade(CustomMethodProvider methods) {
        super(methods);
    }


    @Override
    public List<MyTool> getAllMyTools() {
        return Arrays.asList(BRONZE, IRON, STEEL, BLACK, MITHRIL, ADAMANT, RUNE, DRAGON, INFERNAL);
    }
}
