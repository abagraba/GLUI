package Testing;

import org.lwjgl.opengl.GL11;

import GLcomponent.Batcher;
import GLcomponent.FixedSplitPane;
import GLcomponent.GridMap;
import GLui.InterleavedVBO;

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
