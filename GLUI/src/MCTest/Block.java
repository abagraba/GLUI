package MCTest;

import Util.Vectori3;

public class Block {

	public static final Vectori3 down = Vectori3.yAxis().negate();
	public static final Vectori3 up = Vectori3.yAxis();
	public static final Vectori3 left = Vectori3.xAxis().negate();
	public static final Vectori3 right = Vectori3.xAxis();
	public static final Vectori3 back = Vectori3.zAxis().negate();
	public static final Vectori3 front = Vectori3.zAxis();

	public static final Vectori3[] directions = new Vectori3[] {up, down, left, right, back, front};

	private final Chunk chunk;
	private final Vectori3 position;
	private final BlockData id;

	protected final boolean[] visibility = new boolean[] {true, true, true, true, true, true};

	public Block(BlockData id, int x, int y, int z, Chunk chunk, byte orientation) {
		position = new Vectori3(x, y, z);
		this.chunk = chunk;
		this.id = id;
	}

	public Chunk getChunk() {
		return chunk;
	}

	private World getWorld() {
		return chunk.getWorld();
	}

	public Vectori3 getLocalPosition() {
		return new Vectori3(position);
	}

	public Vectori3 getChunkPosition() {
		return chunk.getPosition();
	}

	public Vectori3 getWorldPosition() {
		return chunk.getWorldPosition().add(position);
	}

	public boolean isTransparent() {
		return id.transparent;
	}

	public void updateBlock(int range) {
		updateSelf();
		if (range > 0)
			for (Vectori3 direction : directions)
				getWorld().getBlockFromWorld(getWorldPosition().add(direction)).updateBlock(range - 1);
	}

	public void updateSelf() {
		if (id == BlockData.none)
			for (int i = 0; i < directions.length; i++)
				visibility[i] = false;
		for (int i = 0; i < directions.length; i++) {
			visibility[i] = true;
			if (!getWorld().isBlockTransparent(getWorldPosition().add(directions[i])))
				visibility[i] = false;
			else if (isTransparent())
				visibility[i] = false;
		}
	}

	public float[] getPositionArray() {
		Vectori3 pos = getWorldPosition();
		return new float[] {pos.x, pos.y, pos.z};
	}

	public float getTexID(int i) {
		switch (i) {
			case 0:
				return id.top;
			case 1:
				return id.bot;
			case 2:
			case 3:
			case 4:
				return id.bot;
			case 5:
				return id.front;
			default:
				return -1;
		}
	}

}
