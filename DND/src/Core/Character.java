package Core;

import java.util.LinkedList;

public class Character implements AbilityMod, ACMod, SkillMod {

	private final int[] ability = new int[Ability.values().length];
	private final int[] miscAbility = new int[Ability.values().length];
	private final int[] skill = new int[Skill.values().length];
	private final int[] miscSkill = new int[Skill.values().length];

	private String name;
	private int level;
	private Race race;
	private Classs classs;

	public int sizeOffset;

	private Armor armor;
	private final LinkedList<Equipment> equipment = new LinkedList<Equipment>();

	public int getEquipmentAbilityMod(Ability a) {
		int abilityMod = 0;
		for (Equipment equip : equipment)
			abilityMod += equip.getAbilityMod(a);
		return abilityMod;
	}

	public int getEquipmentSkillMod(Skill s) {
		int skillMod = 0;
		for (Equipment equip : equipment)
			skillMod += equip.skillMod[s.ordinal()];
		return skillMod;
	}

	public int getEquipmentACMod(AC ac) {
		int acMod = 0;
		for (Equipment equip : equipment)
			acMod += equip.getACMod(ac);
		return acMod;
	}

	public int getAbilityBase(Ability a) {
		return ability[a.ordinal()];
	}

	@Override
	public int getAbilityMod(Ability a) {
		return getAbilityBase(a) + miscAbility[a.ordinal()] + getEquipmentAbilityMod(a) + race.getAbilityMod(a);
	}

	public int getSkillBase(Skill s) {
		return skill[s.ordinal()];
	}

	@Override
	public int getSkillMod(Skill s) {
		return getSkillBase(s) + miscSkill[s.ordinal()] + getEquipmentSkillMod(s) + race.getSkillMod(s)
				+ getAbilityModifier(s.ability);
	}

	@Override
	public int getACMod(AC ac) {
		int dexMod = getAbilityModifier(Ability.DEX);
		if (ac == AC.FLATFOOT)
			if (dexMod > 0)
				dexMod = 0;
		if (ac == AC.HELPLESS)
			dexMod = -5;
		return 10 + dexMod + race.getACMod(ac) + armor.getACMod(ac) + getEquipmentACMod(ac);
	}

	public int getAbilityModifier(Ability a) {
		return getAbilityMod(a) / 2 - 5;
	}

	public Size getSize() {
		return race.size.offset(sizeOffset);
	}
}
