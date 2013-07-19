package MCTest;

import java.util.Random;

import Util.Vectori3;

public class Chunk {

	public static Vectori3 worldScale = new Vectori3(16, 1, 16);

	private final World world;
	private final Vectori3 position;
	private final Block[][][] blocks = new Block[64][16][16];

	public Chunk(int x, int z, World w) {
		position = new Vectori3(x, 0, z);
		world = w;
	}

	public boolean placeBlock(BlockData id, int x, int y, int z, byte orientation) {
		if (blocks[y][x][z] != null)
			return false;
		blocks[y][x][z] = new Block(id, x, y, z, this, orientation);
		return true;
	}

	public Block removeBlock(int x, int y, int z) {
		Block b = blocks[y][x][z];
		blocks[y][x][z] = null;
		return b;
	}

	public Vectori3 getPosition() {
		return new Vectori3(position);
	}

	public Vectori3 getWorldPosition() {
		return Vectori3.product(worldScale, position);
	}

	public World getWorld() {
		return world;
	}

	public Block getBlock(int x, int y, int z) {
		return blocks[y][x][z];
	}

	public Block getBlock(Vectori3 position) {
		return blocks[position.y][position.x][position.z];
	}

	public boolean isTransparent(Vectori3 position) {
		Block b = getBlock(position);
		if (b == null)
			return true;
		return b.isTransparent();
	}

	public void updateChunk() {
		for (int y = 0; y < 64; y++)
			for (int x = 0; x < 16; x++)
				for (int z = 0; z < 16; z++) {
					Block b = blocks[y][x][z];
					if (b != null)
						b.updateSelf();
				}
	}

	public void generateRandomTerrain() {
		int[][] height = new int[16][16];
		for (int x = 0; x < 16; x++)
			for (int z = 0; z < 16; z++)
				height[x][z] = random(x + position.x * 16, z + position.z * 16);
		smooth(height);
		for (int y = 0; y < 64; y++)
			for (int x = 0; x < 16; x++)
				for (int z = 0; z < 16; z++)
					if (y < height[x][z])
						placeBlock(randomType(y), x, y, z, (byte) 0);
		updateChunk();
	}

	public static BlockData randomType(int depth) {
		double r = Math.random();
		if (depth < 3) {
			if (r < 0.9)
				return BlockData.bedrock;
			return BlockData.none;
		}
		if (depth < 6) {
			if (r < 0.4)
				return BlockData.bedrock;
			if (r < 0.9)
				return BlockData.stone;
			return BlockData.none;
		}
		if (depth < 20) {
			if (r < 0.5)
				return BlockData.stone;
			return BlockData.none;
		}
		if (depth < 25) {
			if (r < 0.7)
				return BlockData.stone;
			if (r < 0.9)
				return BlockData.dirt;
			return BlockData.none;
		}
		if (r < 0.7)
			return BlockData.dirt;
		if (r < 0.9)
			return BlockData.stone;
		return BlockData.none;

	}

	public static void smooth(int[][] data) {
		int threshold = 4;
		int noise = 2;
		for (int x = 1; x < data.length; x++)
			for (int z = 1; z < data[0].length; z++) {
				float midpointX = (data[x][z] + data[x - 1][z]) / 2.0f;
				float midpointZ = (data[x][z] + data[x][z - 1]) / 2.0f;
				if (delta(midpointX, midpointZ) > threshold)
					data[x][z] = (int) ((midpointX + midpointZ) / 2 + noise * Math.random() - noise / 2);
				float d = Math.max(delta(midpointX, data[x][z]), delta(midpointX, data[x][z]));
				if (d > threshold)
					data[x][z] = (int) ((midpointX + midpointZ) / 2.0f + noise * Math.random() - noise / 2);

			}
		for (int z = 1; z < data[0].length; z++)
			for (int x = 1; x < data.length; x++) {
				float midpointX = (data[x][z] + data[x - 1][z]) / 2.0f;
				float midpointZ = (data[x][z] + data[x][z - 1]) / 2.0f;
				if (delta(midpointX, midpointZ) > threshold)
					data[x][z] = (int) ((midpointX + midpointZ) / 2.0f + noise * Math.random() - noise / 2);
				float d = Math.max(delta(midpointX, data[x][z]), delta(midpointX, data[x][z]));
				if (d > threshold)
					data[x][z] = (int) ((midpointX + midpointZ) / 2.0f + noise * Math.random() - noise / 2);

			}
	}

	public static float delta(float a, float b) {
		return Math.abs(a - b);
	}

	public static int random(int x, int y) {
		Random r = new Random((x * 13 + y) * (x - y));
		return (int) (28 + r.nextFloat() * 16);

	}
}
