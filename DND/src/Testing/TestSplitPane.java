package Testing;

import org.lwjgl.opengl.GL11;

import GLUI.InterleavedVBO;
import GLUIRenderer.Batcher;
import GLUIRenderer.FixedSplitPane;
import GLUIRenderer.GridMap;

public class TestSplitPane extends FixedSplitPane {

	public TestSplitPane() {
		super(false, false, 25);
	}

	@Override
	public void render() {
		Batcher b;
		b = new Batcher("BaseTileMap", GL11.GL_QUADS, InterleavedVBO.V2T2);
		b.batch(new GridMap(width / 32 + 1, height / 32 + 1, 32, "Blank.png"));
		b.renderBatch();
	}

}
