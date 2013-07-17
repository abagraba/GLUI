package Testing;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import GLcomponent.Drawable;
import GLui.InterleavedVBO;

public class Line extends Drawable {

	int x1, x2, y1, y2;
	Color c;

	public Line(int x1, int x2, int y1, int y2, Color c) {
		super(GL11.GL_LINE, InterleavedVBO.V2C3, null);
		this.c = c;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	@Override
	public int numVertices() {
		return 2;
	}

	@Override
	public float[] vertexData() {
		float r = c.getRed() / 255.0f;
		float g = c.getGreen() / 255.0f;
		float b = c.getBlue() / 255.0f;
		return new float[] {x1, y1, r, g, b, x2, y2, r, g, b};
	}

}
