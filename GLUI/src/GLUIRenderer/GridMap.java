package GLUIRenderer;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import GLUICore.InterleavedVBO;

public class GridMap extends Drawable {

	int width;
	int height;
	int tileSize = 16;

	public GridMap(int w, int h, String texture) {
		super(GL11.GL_QUADS, InterleavedVBO.V2T2, texture);
		width = w;
		height = h;
	}

	@Override
	public int numVertices() {
		return width * height * 4;
	}

	@Override
	public float[] vertexData() {
		float[] data = new float[16 * width * height];
		Random r = new Random((int) (System.nanoTime() / 100000000));
		for (int i = 0; i < width * height; i++) {
			URect vertices = URect.rect().translate(i % width, i / width).scale(tileSize, tileSize);
			URect texes = texture.getTile(r.nextInt() % 4);
			vertices.fillArray(data, 4, 16 * i);
			texes.fillArray(data, 4, 16 * i + 2);
		}
		return data;
	}

	/*
	 * 
	@off
	@Override
	public float[] vertexData() {
		float[] data = new float[16 * width * height];
		float[] square = new float[] {0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0};
		for (int i = 0; i < width * height; i++) {
			for (int j = 0; j < square.length; j++)
				if (j % 4 < 2)
					data[i * 16 + j] = (square[j] + (j % 4 == 0 ? i % width : i / width)) * 16;
				else
					data[i * 16 + j] = square[j];
		}
		return data;
	}
*/
}
