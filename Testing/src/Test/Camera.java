package Test;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import Util.Vectorf3;

public class Camera {

	private final Vectorf3 up = Vectorf3.zAxis();

	private final float moveSpeed = 1.0f;

	private final Vectorf3 target;
	private final Vectorf3 newTarget;
	private float theta;
	protected float phi = 0.785398f;
	private float fov;
	private float nearPlane;
	private float farPlane;
	private final float minimumAngle = 0.5f;
	private final float maximumAngle = (float) (Math.PI * 0.5 - 0.01);

	private float sPhi = 0.707f;
	private float cPhi = 0.707f;
	private float sThe = 0;
	private float cThe = 1;

	private float zoom = 5;
	private final float minZoom = 1;
	private final float maxZoom = 10;

	private final float zoomSpeed = 1.1f;
	private final float izoomSpeed = 1.0f / zoomSpeed;

	public Camera(Vectorf3 position, float near, float far, float fov) {
		target = position;
		newTarget = new Vectorf3(position);
		nearPlane = near;
		farPlane = far;
		this.fov = fov;
		init();
	}

	public void setPosition(float x, float y, float z) {
		target.set(x, y, z);
		newTarget.set(x, y, z);
	}

	public void lookTo(float x, float y, float z) {
		newTarget.set(x, y, z);
	}

	public void orbitUp(float radians) {
		phi -= radians;
		if (phi < minimumAngle)
			phi = minimumAngle;
		if (phi > maximumAngle)
			phi = maximumAngle;
		sPhi = (float) Math.sin(phi);
		cPhi = (float) Math.cos(phi);
	}

	public void orbitRight(float radians) {
		theta -= radians;
		sThe = (float) Math.sin(theta);
		cThe = (float) Math.cos(theta);
	}

	public void moveForward(float speed) {
		target.add(speed * cThe, speed * sThe, 0);
		newTarget.add(speed * cThe, speed * sThe, 0);
	}

	public void moveRight(float speed) {
		target.add(speed * sThe, -speed * cThe, 0);
		newTarget.add(speed * sThe, -speed * cThe, 0);
	}

	public void moveUp(float speed) {
		target.add(Vectorf3.product(up, speed));
		newTarget.add(Vectorf3.product(up, speed));
	}

	public void zoom(float speed) {
		zoom += speed;
		if (zoom > maxZoom)
			zoom = maxZoom;
		if (zoom < minZoom)
			zoom = minZoom;
	}

	private Vectorf3 toCamera() {
		return new Vectorf3(cThe * cPhi, sThe * cPhi, -sPhi);
	}

	public void updateCamera() {
		if (!target.equals(newTarget))
			target.add(Vectorf3.difference(newTarget, target).normalize().scale(moveSpeed));
		Vectorf3 forward = toCamera();
		GL11.glLoadIdentity();
		GLU.gluLookAt(target.x - zoom * forward.x, target.y - zoom * forward.y, target.z - zoom * forward.z, target.x,
				target.y, target.z, 0, 0, 1);
	}

	private void init() {
		float height = Display.getHeight();
		float width = Display.getWidth();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(fov, width / height, nearPlane, farPlane);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public void setClippingPlanes(float near, float far) {
		nearPlane = near;
		farPlane = far;
		init();
	}

	public void setFOV(float fov) {
		this.fov = fov;
		init();
	}

}
