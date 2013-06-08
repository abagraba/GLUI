package Core;

public enum Skill {

	APPRAISE(Ability.INT),
	BALANCE(Ability.DEX, true),
	BLUFF(Ability.CHA),
	CLIMB(Ability.STR, true),
	CONCENTRATION(Ability.CON),
	DECIPHERSCRIPT(Ability.INT),
	DIPLOMACY(Ability.CHA),
	DISABLEDEVICE(Ability.INT),
	DISGUISE(Ability.CHA),
	ESCAPEARTIST(Ability.DEX, true),
	FORGERY(Ability.INT),
	GATHERINFORMATION(Ability.CHA),
	HANDLEANIMAL(Ability.CHA),
	HEAL(Ability.WIS),
	HIDE(Ability.DEX, true),
	INTIMIDATE(Ability.CHA),
	JUMP(Ability.STR, true),
	LISTEN(Ability.WIS),
	MOVESILENTLY(Ability.DEX, true),
	OPENLOCK(Ability.DEX),
	RIDE(Ability.DEX),
	SEARCH(Ability.INT),
	SENSEMOTIVE(Ability.WIS),
	SLEIGHTOFHAND(Ability.DEX, true),
	SPELLCRAFT(Ability.INT),
	SPOT(Ability.WIS),
	SURVIVAL(Ability.WIS),
	SWIM(Ability.STR, true),
	TUMBLE(Ability.DEX, true),
	USEMAGICDEVICE(Ability.CHA),
	USEROPE(Ability.DEX);

	public final Ability ability;
	public final boolean armorCheck;

	private Skill(Ability ability) {
		this(ability, false);
	}

	private Skill(Ability ability, boolean armorCheck) {
		this.ability = ability;
		this.armorCheck = armorCheck;
	}

}
