package Rendering;

import java.util.LinkedList;

import org.lwjgl.util.vector.Vector3f;

import Util.Quaternionf;
import Util.Vectorf3;

public final class Instance {

	private InstantiableStaticEntity entity;
	public Vectorf3 position;
	public Quaternionf rotation;
	public Vectorf3 scale;
	protected boolean modified = true;
	protected final float[] values = new float[10];
	private final LinkedList<DestructionListener> listeners = new LinkedList<DestructionListener>();

	protected Instance() {}

	/**
	 * Translates the instance by x, y, z.
	 * @param x distance to translate along the x-axis.
	 * @param y distance to translate along the y-axis.
	 * @param z distance to translate along the z-axis.
	 */
	public void translateBy(float x, float y, float z) {
		position.add(x, y, z);
		updatePosition();
		modified = true;
	}

	/**
	 * @see #translateBy(float, float, float)
	 */
	public void translateBy(Vector3f translation) {
		translateBy(translation.x, translation.y, translation.z);
	}

	/**
	 * Sets the position to x.
	 * @param x position on the x-axis.
	 * @param y position on the y-axis.
	 * @param z position on the z-axis.
	 */
	public void setPosition(float x, float y, float z) {
		position = new Vectorf3(x, y, z);
		updatePosition();
		modified = true;
	}

	/**
	 * @see #setPosition(float, float, float)
	 */
	public void setPosition(Vector3f position) {
		setPosition(position.x, position.y, position.z);
	}

	/**
	 * Rotates the object by the rotation specified.
	 * @param quaternion representation of the rotation.
	 */
	public void rotateBy(Quaternionf quaternion) {
		rotation.rotateBy(quaternion);
		updateRotation();
		modified = true;
	}

	/**
	 * Sets the rotation of the object.
	 * @param quaternion representation of the object.
	 */
	public void setRotation(Quaternionf quaternion) {
		rotation = quaternion;
		updateRotation();
		modified = true;
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
	 * Destroys this object. Any objects which hold references to this instance should implement
	 * {@link DestructionListener}. Any references to this object will no longer be valid after this call.
	 * @see #addDestructionListener(DestructionListener)
	 */
	public void destroy() {
		for (DestructionListener listener : listeners)
			listener.instanceDestroyed(this);
	}

	/**
	 * Adds a destruction listener.
	 * @param listener destruction listener to be added.
	 */
	public void addDestructionListener(DestructionListener listener) {
		listeners.add(listener);
	}

	protected Instance set(InstantiableStaticEntity entity, Vectorf3 position, Quaternionf rotation, Vectorf3 scale) {
		this.entity = entity;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		listeners.clear();
		updatePosition();
		updateRotation();
		updateScale();
		modified = true;
		return this;
	}

	private void updatePosition() {
		values[0] = position.x;
		values[1] = position.y;
		values[2] = position.z;
	}

	private void updateRotation() {
		values[3] = rotation.x;
		values[4] = rotation.y;
		values[5] = rotation.z;
		values[6] = rotation.w;
	}

	private void updateScale() {
		values[7] = scale.x;
		values[8] = scale.y;
		values[9] = scale.z;
	}

}
