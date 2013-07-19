package MCTest;

import java.util.Hashtable;

import Util.Vectori3;

public class World {

	Hashtable<Vectori3, Chunk> chunks = new Hashtable<Vectori3, Chunk>();

	public boolean addChunk(Chunk c) {
		if (c == null)
			return false;
		Vectori3 position = c.getPosition();
		if (chunks.containsKey(position))
			return false;
		chunks.put(c.getPosition(), c);
		return true;
	}

	public Chunk getChunk(Vectori3 position) {
		return chunks.get(position);
	}

	public Block getBlockFromWorld(Vectori3 position) {
		Chunk c = getChunkFromWorld(position);
		if (c == null)
			return null;
		return c.getBlock(Vectori3.difference(position, c.getWorldPosition()));
	}

	public Chunk getChunkFromWorld(Vectori3 position) {
		return chunks.get(new Vectori3((int) Math.floor(position.x / 16.0), (int) Math.floor(position.y / 64.0), (int) Math
				.floor(position.z / 16.0)));
	}

	public boolean isBlockTransparent(Vectori3 position) {
		Chunk c = getChunkFromWorld(position);
		if (c == null)
			return true;
		return c.isTransparent(Vectori3.difference(position, c.getWorldPosition()));
	}
}
