package Util;

public class Vectorf3 {

	public float x, y, z;

	public Vectorf3() {}

	public Vectorf3(float x, float y, float z) {
		set(x, y, z);
	}

	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vectorf3 add(Vectorf3 v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}

	public Vectorf3 add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Vectorf3 subtract(Vectorf3 v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}

	public Vectorf3 subtract(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public Vectorf3 multiply(Vectorf3 v) {
		x *= v.x;
		y *= v.y;
		z *= v.z;
		return this;
	}

	public Vectorf3 multiply(float x, float y, float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}

	public Vectorf3 divide(Vectorf3 v) {
		x /= v.x;
		y /= v.y;
		z /= v.z;
		return this;
	}

	public Vectorf3 divide(float x, float y, float z) {
		this.x /= x;
		this.y /= y;
		this.z /= z;
		return this;
	}

	public Vectorf3 scale(float f) {
		x *= f;
		y *= f;
		z *= f;
		return this;
	}

	public Vectorf3 negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	public void invert() {
		x = 1.0f / x;
		y = 1.0f / y;
		z = 1.0f / z;
	}

	public Vectorf3 normalize() {
		float square = x * x + y * y + z * z;
		if (square == 1)
			return this;
		float ir = (float) (1.0f / Math.sqrt(square));
		return scale(ir);
	}

	public float[] toArray() {
		return new float[] {x, y, z};
	}

	public static Vectorf3 sum(Vectorf3 a, Vectorf3 b) {
		return new Vectorf3(a.x + b.x, a.y + b.y, a.z + b.z);
	}

	/**
	 * Returns the componentwise difference. Vector a - vector b.
	 * @param a minuend vector.
	 * @param b subtrahend vector.
	 * @return difference vector.
	 */
	public static Vectorf3 difference(Vectorf3 a, Vectorf3 b) {
		return new Vectorf3(a.x - b.x, a.y - b.y, a.z - b.z);
	}

	/**
	 * Returns the componentwise product.
	 * @param a first factor vector.
	 * @param b second factor vector.
	 * @return componentwise product vector.
	 */
	public static Vectorf3 product(Vectorf3 a, Vectorf3 b) {
		return new Vectorf3(a.x * b.x, a.y * b.y, a.z * b.z);
	}

	/**
	 * Returns the componentwise quotient.
	 * @param a dividend vector.
	 * @param b divisor vector.
	 * @return componentwise quotient vector.
	 */
	public static Vectorf3 quotient(Vectorf3 a, Vectorf3 b) {
		return new Vectorf3(a.x / b.x, a.y / b.y, a.z / b.z);
	}

	/**
	 * Returns the cross product.
	 * @param a vector.
	 * @param b vector.
	 * @return cross product vector.
	 */
	public static Vectorf3 cross(Vectorf3 a, Vectorf3 b) {
		return new Vectorf3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
	}

	/**
	 * Returns the dot product.
	 * @param a vector.
	 * @param b vector.
	 * @return dot product.
	 */
	public static float dot(Vectorf3 a, Vectorf3 b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	/**
	 * Returns the componentwise negator.
	 * @param a the vector.
	 * @return componentwise negator vector.
	 */
	public static Vectorf3 negator(Vectorf3 a) {
		return new Vectorf3(-a.x, -a.y, -a.z);
	}

	/**
	 * Returns the componentwise inverse.
	 * @param a the vector.
	 * @return componentwise inverse vector.
	 */
	public static Vectorf3 inverse(Vectorf3 a) {
		return new Vectorf3(1.0f / a.x, 1.0f / a.y, 1.0f / a.z);
	}

	public static Vectorf3 xAxis() {
		return new Vectorf3(1, 0, 0);
	}

	public static Vectorf3 yAxis() {
		return new Vectorf3(0, 1, 0);
	}

	public static Vectorf3 zAxis() {
		return new Vectorf3(0, 0, 1);
	}

}
