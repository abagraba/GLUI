package Core;

public class Race implements AbilityMod, ACMod, SkillMod {

	public final int[] abilityMod = new int[Ability.values().length];
	public final int[] skillMod = new int[Skill.values().length];
	public Size size;
	public int naturalArmor;

	@Override
	public int getAbilityMod(Ability a) {
		return abilityMod[a.ordinal()];
	}

	@Override
	public int getSkillMod(Skill s) {
		return skillMod[s.ordinal()] + size.getSkillMod(s);
	}

	@Override
	public int getACMod(AC ac) {
		if (ac == AC.TOUCH)
			return size.getACMod(ac);
		return naturalArmor + size.getACMod(ac);
	}

}
