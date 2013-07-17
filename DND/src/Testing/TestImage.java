package Testing;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import GLcomponent.Batcher;
import GLcomponent.GridMap;
import GLres.Font;
import GLres.ResourceManager;
import GLui.InterleavedVBO;
import GLui.Renderable;

public class TestImage extends Renderable {

	// FIXME turn fonts into VBOs. then use Array indices to do letters?

	// Manual cache!
	// Replace String with a string + VBO entry.
	HashMap<Font, String> cache = new HashMap<Font, String>(4);
	Font f1 = ResourceManager.getFont("Modern");

	@Override
	public void render() {
		Batcher b = new Batcher("Input", GL11.GL_QUADS, InterleavedVBO.V2T2);
		b.batch(new GridMap(width / 16 + 1, height / 16 + 1, 16, "Base"));
		b.batch(f1.getWrappedString("Four score and seven years ago. My four fathers ran this country.", 0, 0, 16, 500, 3));
		b.renderBatch();
	}

	@Override
	public void validateContents() {
		// TODO Auto-generated method stub

	}
}
