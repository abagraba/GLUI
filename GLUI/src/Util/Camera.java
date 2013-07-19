package Util;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;


public class Camera {

	private final Vectorf3 position;
	private float theta;
	private float phi;
	private float fov;
	private float near;
	private float far;
	private final float tolerance = 0.05f;

	private float sPhi = 0;
	private float cPhi = 1;
	private float sThe = 0;
	private float cThe = 1;

	public Camera(Vectorf3 position, float near, float far, float fov) {
		this.position = position;
		this.near = near;
		this.far = far;
		this.fov = fov;
		init();
	}

	public void updateMatrix() {
		Vectorf3 forward = forward();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GLU.gluLookAt(position.x, position.y, position.z, position.x + forward.x, position.y + forward.y, position.z
				+ forward.z, 0, 1, 0);
	}

	public void setPosition(int x, int y, int z) {
		position.set(x, y, z);
	}

	public void turnUp(float radians) {
		phi += radians;
		if (phi > Math.PI * 0.5 - tolerance)
			phi = (float) (Math.PI * 0.5 - tolerance);
		if (phi < -Math.PI * 0.5 + tolerance)
			phi = (float) (-Math.PI * 0.5 + tolerance);
		sPhi = (float) Math.sin(phi);
		cPhi = (float) Math.cos(phi);
	}

	public void turnRight(float radians) {
		theta += radians;
		sThe = (float) Math.sin(theta);
		cThe = (float) Math.cos(theta);
	}

	public void moveForward(float speed) {
		position.add(forward().scale(speed));
	}

	public void moveRight(float speed) {
		position.add(right().scale(speed));
	}

	public void moveUp(float speed) {
		position.add(up().scale(speed));
	}

	private Vectorf3 forward() {
		return new Vectorf3(cThe * cPhi, sPhi, sThe * cPhi).normalize();
	}

	private Vectorf3 right() {
		return Vectorf3.cross(forward(), up()).normalize();
	}

	private Vectorf3 up() {
		return Vectorf3.yAxis();
	}

	public void setClippingPlanes(float near, float far) {
		this.near = near;
		this.far = far;
		init();
	}

	public void setFOV(float fov) {
		this.fov = fov;
		init();
	}

	private void init() {
		float height = Display.getHeight();
		float width = Display.getWidth();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(fov, width / height, near, far);
	}
}
