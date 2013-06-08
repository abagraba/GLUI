package Core;

public enum Size implements ACMod, CostMod, SkillMod {

	Fine,
	Diminutive,
	Tiny,
	Small,
	Medium,
	Large,
	Huge,
	Gargantuan,
	Colossal;

	@Override
	public int getACMod(AC ac) {
		int base = ordinal() - 4;
		if (base == 0)
			return 0;
		return -(int) (Math.signum(base) * Math.pow(2, Math.abs(base - 1)));
	}

	@Override
	public int getSkillMod(Skill s) {
		if (s == Skill.HIDE)
			return 4 * ordinal() - 16;
		return 0;
	}

	public int getGrappleMod() {
		return 4 * ordinal() - 16;
	}

	public Size offset(int offset) {
		return Size.values()[ordinal() + offset];
	}

	@Override
	public float costAdd() {
		return 0;
	}

	@Override
	public float costMult() {
		if (ordinal() - 4 >= 0)
			return (float) Math.pow(2, ordinal() - 4);
		if (ordinal() == 3)
			return 1;
		return 0.5f;
	}

	public float weightMult() {
		if (ordinal() < 3)
			return 0.1f;
		if (ordinal() == 6)
			return 5;
		return (float) Math.pow(2, ordinal() - 4);
	}

}
