//package util;
//
//import org.osbot.rs07.api.Skills;
//import org.osbot.rs07.api.ui.Skill;
//
//public class Weapon {
//    String name;
//    int skillLevelRequired;
//    int attLevelRequired;
//    boolean membersOnly;
//    Skill skillRequired;
//
//    public MyTool(final String name, final int attLevelRequired, ) {
//        this(name, skillLevelRequired, attLevelRequired, skillRequired, false);
//    }
//
//    public MyTool(final String name, final int skillLevelRequired, final int attLevelRequired, final Skill skillRequired, final boolean membersOnly) {
//        this.name = name;
//        this.skillLevelRequired = skillLevelRequired;
//        this.attLevelRequired = attLevelRequired;
//        this.skillRequired = skillRequired;
//        this.membersOnly = membersOnly;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public boolean isMembersOnly() {
//        return membersOnly;
//    }
//
//    public int getLevelRequired(){
//        return skillLevelRequired;
//    }
//
//    public boolean canUse(Skills skills) {
//        return skills.getStatic(skillRequired) >= skillLevelRequired;
//    }
//
//    public boolean canEquip(Skills skills) {
//        return skills.getStatic(Skill.ATTACK) >= attLevelRequired;
//    }
//}