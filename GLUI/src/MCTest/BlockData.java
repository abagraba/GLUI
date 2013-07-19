package MCTest;

public enum BlockData {

	none(0, true),
	stone(1),
	dirt(2),
	cobble(16),
	wood(4),
	saplings(15),
	bedrock(17);

	final int id;
	final int top;
	final int side;
	final int bot;
	final int front;
	final boolean transparent;

	private BlockData(int top) {
		this(top, top);
	}

	private BlockData(int top, boolean transparent) {
		this(top, top, transparent);
	}

	private BlockData(int top, int side) {
		this(top, side, top);
	}

	private BlockData(int top, int side, boolean transparent) {
		this(top, side, top, transparent);
	}

	private BlockData(int top, int side, int bot) {
		this(top, side, bot, side);
	}

	private BlockData(int top, int side, int bot, boolean transparent) {
		this(top, side, bot, side, transparent);
	}

	private BlockData(int top, int side, int bot, int front) {
		this(top, side, bot, front, false);
	}

	private BlockData(int top, int side, int bot, int front, boolean transparent) {
		id = ordinal();
		this.top = top;
		this.side = side;
		this.bot = bot;
		this.front = front;
		this.transparent = transparent;
	}

}
