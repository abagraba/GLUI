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

	public void add(Vectorf3 v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}

	public void add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public void subtract(Vectorf3 v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}

	public void subtract(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	public void multiply(Vectorf3 v) {
		x *= v.x;
		y *= v.y;
		z *= v.z;
	}

	public void multiply(float x, float y, float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
	}

	public void divide(Vectorf3 v) {
		x /= v.x;
		y /= v.y;
		z /= v.z;
	}

	public void divide(float x, float y, float z) {
		this.x /= x;
		this.y /= y;
		this.z /= z;
	}

	public void scale(float f) {
		x *= f;
		y *= f;
		z *= f;
	}

	public void negate() {
		x = -x;
		y = -y;
		z = -z;
	}

	public void invert() {
		x = 1.0f / x;
		y = 1.0f / y;
		z = 1.0f / z;
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
