package Util;

public class Quaternionf {

	public float x, y, z, w;

	public Quaternionf() {
		set(0, 0, 0, 1);
	}

	public Quaternionf(float x, float y, float z, float w) {
		set(x, y, z, w);
	}

	public void set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Rotates this quaternion by the rotation represented by q.
	 * @param q rotation.
	 */
	public void rotateBy(Quaternionf q) {
		normalize();
		q.normalize();
		float newX = q.w * x + q.x * w + q.y * z - q.z * y;
		float newY = q.w * y + q.y * w - q.x * z + q.z * x;
		float newZ = q.w * z + q.z * w + q.x * y - q.y * x;
		float newW = q.w * w - q.x * x - q.y * y - q.z * z;
		set(newX, newY, newZ, newW);
	}

	/**
	 * Normalizes the Quaternion.
	 */
	public void normalize() {
		float square = (float) Math.sqrt(x * x + y * y + z * z + w * w);
		if (square == 1.0f)
			return;
		float isqrt = 1.0f / square;
		x *= isqrt;
		y *= isqrt;
		z *= isqrt;
		w *= isqrt;
	}

	public float[] toArray() {
		return new float[] {x, y, z, w};
	}

	/**
	 * Returns the identity Quaternion.
	 * @return the identity Quaternion.
	 */
	public static Quaternionf getIdentity() {
		return new Quaternionf();
	}

	/**
	 * Returns a Quaternion representing the rotation specified by the given Euler Angles in the Body 321 sequence.
	 * @param psi rotation around the x-axis.
	 * @param theta rotation around the y-axis.
	 * @param phi rotation around the z-axis.
	 * @return Quaternion representing the rotation.
	 */
	public static Quaternionf fromEulerAngle321(float psi, float theta, float phi) {
		float sPhi = (float) Math.sin(0.5f * psi);
		float cPhi = (float) Math.cos(0.5f * psi);
		float sThe = (float) Math.sin(0.5f * theta);
		float cThe = (float) Math.cos(0.5f * theta);
		float sPsi = (float) Math.sin(0.5f * phi);
		float cPsi = (float) Math.cos(0.5f * phi);
		return new Quaternionf(sPhi * cThe * cPsi - cPhi * sThe * sPsi, cPhi * sThe * cPsi + sPhi * cThe * sPsi, cPhi * cThe
				* sPsi - sPhi * sThe * cPsi, cPhi * cThe * cPsi + sPhi * sThe * sPsi);
	}

	/**
	 * Returns a Quaternion representing a rotation around the specified axis.
	 * @param axis axis to rotate around.
	 * @param angle angle of rotation in radians.
	 * @return Quaternion representing the rotation.
	 */
	public static Quaternionf fromAxisAngle(Vectorf3 axis, float angle) {
		axis.normalize();
		float hAngle = 0.5f * angle;
		float hSin = (float) Math.sin(hAngle);
		float hCos = (float) Math.cos(hAngle);
		return new Quaternionf(axis.x * hSin, axis.y * hSin, axis.z * hSin, hCos);
	}

	@Override
	public String toString() {
		return String.format("[%f, %f, %f : %f]", x, y, z, w);
	}

}
