package activities.skills.woodcutting;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.utility.Logger;
import util.MyTool;
import util.MyToolUpgrade;
import util.custom_method_provider.CustomMethodProvider;

import java.util.Arrays;
import java.util.List;

public class AxeUpgrade extends MyToolUpgrade {

    public AxeUpgrade(CustomMethodProvider methods) {
        super(methods);
    }

    MyTool BRONZE = new MyTool("Bronze axe", 1, 1, Skill.WOODCUTTING, false);
    MyTool IRON = new MyTool("Iron axe", 1, 1, Skill.WOODCUTTING, false);
    MyTool STEEL = new MyTool("Steel axe", 6, 5, Skill.WOODCUTTING, false);
    MyTool BLACK = new MyTool("Black axe", 11, 10, Skill.WOODCUTTING, false);
    MyTool MITHRIL = new MyTool("Mithril axe", 21, 20, Skill.WOODCUTTING, false);
    MyTool ADAMANT = new MyTool("Adamant axe", 31, 30, Skill.WOODCUTTING, false);
    MyTool RUNE = new MyTool("Rune axe", 41, 40, Skill.WOODCUTTING, false);
    MyTool DRAGON = new MyTool("Dragon axe", 61, 60, Skill.WOODCUTTING, true);
    MyTool INFERNAL = new MyTool("Infernal axe", 61, 60, Skill.WOODCUTTING, true);

    @Override
    public List<MyTool> getAllMyTools() {
        return Arrays.asList(BRONZE, IRON, STEEL, BLACK, MITHRIL, ADAMANT, RUNE, DRAGON, INFERNAL);
    }
}
