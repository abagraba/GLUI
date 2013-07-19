package Util;

public class Vectori3 {

	public int x, y, z;

	public Vectori3() {}

	public Vectori3(int x, int y, int z) {
		set(x, y, z);
	}

	public Vectori3(Vectori3 position) {
		set(position.x, position.y, position.z);
	}

	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vectori3 add(Vectori3 v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}

	public Vectori3 add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Vectori3 subtract(Vectori3 v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}

	public Vectori3 subtract(int x, int y, int z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public Vectori3 multiply(Vectori3 v) {
		x *= v.x;
		y *= v.y;
		z *= v.z;
		return this;
	}

	public Vectori3 multiply(int x, int y, int z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}

	public Vectori3 divide(Vectori3 v) {
		x /= v.x;
		y /= v.y;
		z /= v.z;
		return this;
	}

	public Vectori3 divide(int x, int y, int z) {
		this.x /= x;
		this.y /= y;
		this.z /= z;
		return this;
	}

	public Vectori3 scale(int f) {
		x *= f;
		y *= f;
		z *= f;
		return this;
	}

	public Vectori3 negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	public int[] toArray() {
		return new int[] {x, y, z};
	}

	public static Vectori3 sum(Vectori3 a, Vectori3 b) {
		return new Vectori3(a.x + b.x, a.y + b.y, a.z + b.z);
	}

	/**
	 * Returns the componentwise difference. Vector a - vector b.
	 * @param a minuend vector.
	 * @param b subtrahend vector.
	 * @return difference vector.
	 */
	public static Vectori3 difference(Vectori3 a, Vectori3 b) {
		return new Vectori3(a.x - b.x, a.y - b.y, a.z - b.z);
	}

	/**
	 * Returns the componentwise product.
	 * @param a first factor vector.
	 * @param b second factor vector.
	 * @return componentwise product vector.
	 */
	public static Vectori3 product(Vectori3 a, Vectori3 b) {
		return new Vectori3(a.x * b.x, a.y * b.y, a.z * b.z);
	}

	/**
	 * Returns the componentwise quotient.
	 * @param a dividend vector.
	 * @param b divisor vector.
	 * @return componentwise quotient vector.
	 */
	public static Vectori3 quotient(Vectori3 a, Vectori3 b) {
		return new Vectori3(a.x / b.x, a.y / b.y, a.z / b.z);
	}

	/**
	 * Returns the componentwise negator.
	 * @param a the vector.
	 * @return componentwise negator vector.
	 */
	public static Vectori3 negator(Vectori3 a) {
		return new Vectori3(-a.x, -a.y, -a.z);
	}

	public static Vectori3 xAxis() {
		return new Vectori3(1, 0, 0);
	}

	public static Vectori3 yAxis() {
		return new Vectori3(0, 1, 0);
	}

	public static Vectori3 zAxis() {
		return new Vectori3(0, 0, 1);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vectori3))
			return false;
		Vectori3 v = (Vectori3) o;
		return v.x == x && v.y == y && v.z == z;
	}

	@Override
	public int hashCode() {
		return ((x * 1543 + y) * 1543 + z) * 1543;
	}

	@Override
	public String toString() {
		return String.format("[%d, %d, %d]", x, y, z);
	}

}
