package Test;

import Util.Quaternionf;
import Util.Vectorf3;
import Util.Vectori3;

public final class TileInstance {

	private static final float[] rotationCache = new float[] {0, 1, 0.130526f, 0.991445f, 0.258819f, 0.965926f, 0.382683f,
																0.92388f, 0.5f, 0.866025f, 0.608761f, 0.793353f, 0.707107f,
																0.707107f, 0.793353f, 0.608761f, 0.866025f, 0.5f, 0.92388f,
																0.382683f, 0.965926f, 0.258819f, 0.991445f, 0.130526f, 1, 0,
																0.991445f, -0.130526f, 0.965926f, -0.258819f, 0.92388f,
																-0.382683f, 0.866025f, -0.5f, 0.793353f, -0.608761f,
																0.707107f, -0.707107f, 0.608761f, -0.793353f, 0.5f,
																-0.866025f, 0.382683f, -0.92388f, 0.258819f, -0.965926f,
																0.130526f, -0.991445f};

	protected TileEntity entity;
	protected Vectorf3 position;
	protected int terrain;
	protected boolean modified = true;
	protected Quaternionf rotation;

	private TileInstance() {}

	public static TileInstance createActiveInstance(TileEntity entity, Vectorf3 position, Quaternionf rotation, int terrain) {
		TileInstance tile = new TileInstance(entity, position, rotation, terrain);
		entity.active.add(tile);
		return tile;
	}

	public static TileInstance createInactiveInstance(TileEntity entity, Vectorf3 position, Quaternionf rotation, int terrain) {
		TileInstance tile = new TileInstance(entity, position, rotation, terrain);
		entity.inactive.add(tile);
		return tile;
	}

	public static TileInstance createActiveInstance(TileEntity entity, Vectori3 position, Quaternionf rotation, int terrain) {
		return createActiveInstance(entity, new Vectorf3(position), rotation, terrain);
	}

	public static TileInstance createInactiveInstance(TileEntity entity, Vectori3 position, Quaternionf rotation, int terrain) {
		return createInactiveInstance(entity, new Vectorf3(position), rotation, terrain);
	}

	private TileInstance(TileEntity entity, Vectorf3 position, Quaternionf rotation, int terrain) {
		this.entity = entity;
		this.position = position;
		this.rotation = rotation;
		this.terrain = terrain;
		modified = true;
	}

	public float[] getInstanceData() {
		return new float[] {position.x, position.y, position.z, rotation.x, rotation.y, rotation.z, rotation.w, terrain};
	}

	/**
	 * Activates this instance.
	 */
	public void activate() {
		modified = true;
		entity.activateInstance(this);
	}

	/**
	 * Deactivates this instance. Deactivated instances are not rendered.
	 */
	public void deactivate() {
		entity.deactivateInstance(this);
	}

	/**
	 * Destroys this object.
	 */
	public void destroy() {
		entity.instanceDestroyed(this);
	}

}
