package Testing;

import org.lwjgl.opengl.GL11;

import GLUI.InterleavedVBO;
import GLUI.KeyEvent;
import GLUI.RenderContainer;
import GLUI.Renderable;
import GLUIRenderer.Batcher;
import GLUIRenderer.GridMap;

public class TestPane extends RenderContainer {

	int w = 0;
	int h = 0;
	Renderable t = new TestTextInput();

	public TestPane() {
		add(t);
	}

	@Override
	public void render() {
		Batcher b;
		b = new Batcher("BaseTileMap", GL11.GL_QUADS, InterleavedVBO.V2T2);
		b.batch(new GridMap(width / 32 + 1, height / 32 + 1, 32, "Blank.png"));
		b.renderBatch();
	}

	@Override
	public void validateContents() {
		t.reposition(0, 025);
		t.resize(600, 400);
		t.repack();
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		t.resize(w++ * 10, h++ * 10);
	}

}
