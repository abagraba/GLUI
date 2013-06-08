package Core;

public class Equipment extends Item implements AbilityMod, ACMod, SkillMod {

	public final int[] abilityMod = new int[Ability.values().length];
	public final int[] skillMod = new int[Skill.values().length];
	public final int[] acMod = new int[AC.values().length];

	@Override
	public int getAbilityMod(Ability a) {
		return abilityMod[a.ordinal()];
	}

	@Override
	public int getSkillMod(Skill s) {
		return skillMod[s.ordinal()];
	}

	@Override
	public int getACMod(AC ac) {
		return acMod[ac.ordinal()];
	}
}
