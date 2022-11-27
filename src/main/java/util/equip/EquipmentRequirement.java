//package util.equip;
//
//import org.osbot.rs07.api.Skills;
//import org.osbot.rs07.api.ui.Skill;
//import util.Tool;
//
//
//public enum EquipmentRequirement {
//
//    ATTACK("Bronze axe", 1, 1),
//    DEFENCE("Iron axe", 1, 1),
//    MAGIC("Steel axe", 6, 5),
//
//
//    String name;
//    int wcLevelRequired;
//    int attLevelRequired;
//    boolean membersOnly;
//
//    EquipmentRequirement(final String name, final int wcLevelRequired, final int attLevelRequired) {
//        this.name = name;
//        this.wcLevelRequired = wcLevelRequired;
//        this.attLevelRequired = attLevelRequired;
//    }
//
//    EquipmentRequirement(final String name, final int wcLevelRequired, final int attLevelRequired, final boolean membersOnly) {
//        this(name, wcLevelRequired, attLevelRequired);
//        this.membersOnly = membersOnly;
//    }
//
//    @Override
//    public String getName() {
//        return name;
//    }
//
//    @Override
//    public boolean isMembersOnly() {
//        return membersOnly;
//    }
//
//    @Override
//    public int getLevelRequired() {
//        return wcLevelRequired;
//    }
//
//    @Override
//    public boolean canUse(Skills skills) {
//        return skills.getStatic(Skill.WOODCUTTING) >= wcLevelRequired;
//    }
//
//    @Override
//    public boolean canEquip(Skills skills) {
//        return skills.getStatic(Skill.ATTACK) >= attLevelRequired;
//    }
//
//    @Override
//    public String toString() {
//        return name;
//    }
//}
