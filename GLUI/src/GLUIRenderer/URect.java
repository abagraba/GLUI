package GLUIRenderer;

public final class URect {

	public float x1, y1, x2, y2;

	private URect(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public static URect rect() {
		return new URect(0, 0, 1, 1);
	}

	public URect scale(float sx, float sy) {
		x1 *= sx;
		y1 *= sy;
		x2 *= sx;
		y2 *= sy;
		return this;
	}

	public URect translate(float dx, float dy) {
		x1 += dx;
		y1 += dy;
		x2 += dx;
		y2 += dy;
		return this;
	}

	public void fillArray(float[] f, int stride, int offset) {
		f[offset + 0 * stride + 0] = x1;
		f[offset + 0 * stride + 1] = y1;
		f[offset + 1 * stride + 0] = x1;
		f[offset + 1 * stride + 1] = y2;
		f[offset + 2 * stride + 0] = x2;
		f[offset + 2 * stride + 1] = y2;
		f[offset + 3 * stride + 0] = x2;
		f[offset + 3 * stride + 1] = y1;
	}
}
