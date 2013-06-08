package Testing;

import org.lwjgl.opengl.GL11;

import GLUICore.InterleavedVBO;
import GLUICore.RenderContainer;
import GLUIRenderer.Batcher;
import GLUIRenderer.GridMap;

public class TestPane extends RenderContainer {

	@Override
	public void render() {
		Batcher b = new Batcher("Tester", GL11.GL_QUADS, InterleavedVBO.V2T2);
		b.batch(new GridMap(100, 1000, "Base"));
		b.renderBatch();
	}

}
