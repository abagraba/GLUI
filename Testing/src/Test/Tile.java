package Test;

import java.util.LinkedList;

import Util.Quaternionf;
import Util.Vectorf3;
import Util.Vectori3;

public class Tile {

	public int terrainType;

	private final Map map;
	private boolean passable;
	protected final Vectori3 position;
	private boolean visible = false;

	private static final Quaternionf[] rotationCache;

	static {
		rotationCache = new Quaternionf[4];
		for (int i = 0; i < rotationCache.length; i++)
			rotationCache[i] = Quaternionf.fromAxisAngle(Vectorf3.zAxis(), (float) -(i * Math.PI / 2.0));
	}

	private final LinkedList<TileInstance> cache = new LinkedList<TileInstance>();

	public Tile(Map map, int x, int y, int terrainType, boolean passable) {
		this.map = map;
		this.terrainType = terrainType;
		this.passable = passable;
		position = new Vectori3(x, y, 0);
	}

	public void see() {
		if (!visible)
			for (TileInstance instance : cache)
				instance.activate();
		visible = true;
	}

	public void unsee() {
		if (visible)
			for (TileInstance instance : cache)
				instance.deactivate();
		visible = false;
	}

	public void setPosition(int x, int y) {
		position.set(x, y, 0);
	}

	public Vectori3 getPosition() {
		return new Vectori3(position);
	}

	public void setTerrain(int type) {
		terrainType = type;
		for (TileInstance instance : cache)
			instance.terrain = type;
	}

	public int getTerrain() {
		return terrainType;
	}

	public void setPassability(boolean passable) {
		this.passable = passable;
		updateCache();
	}

	public boolean getPassabillity() {
		return passable;
	}

	protected void updateCache() {
		destroy();
		if (passable) {
			cache.add(TileInstance.createInactiveInstance(map.floorRenderer, position, rotationCache[0], terrainType));
			boolean[] neighbors = map.getNeighborPassable(this, Map.ortho);
			for (int i = 0; i < neighbors.length; i++)
				if (!neighbors[i])
					cache.add(TileInstance.createInactiveInstance(map.wallRenderer, position, rotationCache[i], terrainType));
		}
	}

	protected void destroy() {
		for (TileInstance instance : cache)
			instance.destroy();
		cache.clear();
	}
}
