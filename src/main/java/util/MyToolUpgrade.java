package util;

import org.osbot.rs07.api.Worlds;
import org.osbot.rs07.api.ui.Skill;
import util.custom_method_provider.CustomMethodProvider;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MyToolUpgrade {
    private final CustomMethodProvider methods;

    public MyToolUpgrade(final CustomMethodProvider methods) {
        this.methods = methods;
    }

    public MyTool bestMyToolForWorld() {
        return bestMyToolForWorld(bestMembersTool());
    }

    private MyTool bestMyToolForWorld(MyTool tool) {
        Worlds worlds = new Worlds();
        if(worlds.isMembersWorld()) {
            return tool;
        } else {
            if (membersTools().contains(tool)) {
                return bestNonMembersTool();
            } else {
                return tool;
            }
        }
    }

    public List<MyTool> membersTools() {
        return getAllMyTools().stream().filter(myTool -> !myTool.membersOnly).collect(Collectors.toList());
    }

    public List<MyTool> nonMembersTools() {
        return getAllMyTools().stream().filter(myTool -> myTool.membersOnly).collect(Collectors.toList());
    }

    public MyTool bestNonMembersTool() {
        return nonMembersTools().stream().max(Comparator.comparingInt(MyTool::getLevelRequired)).orElse(worstTool());
    }

    public MyTool bestMembersTool() {
        return membersTools().stream().max(Comparator.comparingInt(MyTool::getLevelRequired)).orElse(bestNonMembersTool());
    }

    public MyTool worstTool() {
        return getAllMyTools().stream().max(Comparator.comparingInt(tool -> -tool.getLevelRequired())).orElse(null);

    }

    abstract public List<MyTool> getAllMyTools();

    public MyTool defaultMyTool() {
        return worstTool();
    }

    public MyTool bestMyToolToEquip() {
        int attackLevel = methods.getSkills().getStatic(Skill.ATTACK);
        MyTool best = getAllMyTools().stream().filter(myTool -> myTool.attLevelRequired < attackLevel)
                .max(Comparator.comparingInt(MyTool::getLevelRequired))
                .orElse(defaultMyTool());
        return bestMyToolForWorld(best);
    }

    public MyTool bestMyToolToUse() {
        int skillLevel = methods.getSkills().getStatic(defaultMyTool().skillRequired);
        MyTool best = getAllMyTools().stream().filter(myTool -> myTool.attLevelRequired < skillLevel)
                .max(Comparator.comparingInt(MyTool::getLevelRequired))
                .orElse(worstTool());
        return bestMyToolForWorld(best);
    }

    public MyTool bestToolInInventory() {
        List<MyTool> tools = getAllMyTools();
        return tools.stream().filter(tool -> methods.getInventory().getItem(tool.getName()) != null)
                .max(Comparator.comparingInt(MyTool::getLevelRequired))
                .orElse(null);
    }

    public MyTool bestToolInEquipment() {
        List<MyTool> tools = getAllMyTools();
        return tools.stream().filter(tool -> methods.getEquipment().getItem(tool.getName()) != null)
                .max(Comparator.comparingInt(MyTool::getLevelRequired))
                .orElse(null);
    }

    public MyTool bestToolOnPlayer() {
        MyTool bestEquipped = bestToolInEquipment();
        MyTool bestInInventory = bestToolInInventory();
        if (bestEquipped != null && bestInInventory == null) return bestEquipped;
        if (bestEquipped != null && bestEquipped.skillLevelRequired >= bestInInventory.skillLevelRequired) {
            return bestEquipped;
        }
        return bestInInventory;
    }

}
