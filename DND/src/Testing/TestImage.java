package Testing;

import org.lwjgl.opengl.GL11;

import GLUI.InterleavedVBO;
import GLUI.Renderable;
import GLUIRenderer.Batcher;
import GLUIRenderer.GridMap;

public class TestImage extends Renderable {
	@Override
	public void render() {
		Batcher b = new Batcher("Input", GL11.GL_QUADS, InterleavedVBO.V2T2);
		b.batch(new GridMap(width / 16 + 1, height / 16 + 1, 16, "Base"));
		b.renderBatch();
	}

	@Override
	public void validateContents() {
		// TODO Auto-generated method stub

	}
}
