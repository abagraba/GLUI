package Testing;

import java.awt.Color;

import GLcomponent.Drawable;
import GLui.InterleavedVBO;

public class Quad extends Drawable {

	public final int x, y, w, h;
	public final Color c;

	public Quad(int x, int y, int w, int h, Color c) {
		super(InterleavedVBO.V2C3, null);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.c = c;
	}

	@Override
	public int numVertices() {
		return 4;
	}

	@Override
	public float[] vertexData() {
		return new float[] {x, y, c.getRed(), c.getGreen(), c.getBlue(), x + w, y, c.getRed(), c.getGreen(), c.getBlue(),
							x + w, y + h, c.getRed(), c.getGreen(), c.getBlue(), x, y + h, c.getRed(), c.getGreen(),
							c.getBlue()};
	}

}
