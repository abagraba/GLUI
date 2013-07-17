package Testing;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import GLcomponent.Batcher;
import GLcomponent.CenterPane;
import GLui.InterleavedVBO;

public class Window extends GLui.Window {

	public Window(int width, int height) {
		super(width, height);
		int w, h;
		w = width;
		h = height;
		final GraphData g = new GraphData(400, 300, -10, 10, -10, 10);
		CenterPane a = new CenterPane() {
			@Override
			public void render() {
				Batcher b = new Batcher("a", GL11.GL_QUADS, InterleavedVBO.V2C3);
				b.batch(new Quad(0, 0, getWidth(), getHeight(), Color.white));
				b.renderBatch();
			}
		};
		a.setMinimumSize(w, h);
		a.setMaximumSize(w, h);
		w = 500;
		h = 400;
		CenterPane aa = new CenterPane() {
			@Override
			public void render() {
				Batcher b = new Batcher("aa", GL11.GL_QUADS, InterleavedVBO.V2C3);
				b.batch(new Quad(0, 0, getWidth(), getHeight(), Color.blue));
				b.renderBatch();
				b.batch(new Line(100, 100, 1000, 1000, Color.green));
				b.renderBatch();
				// gg.batch(g);
			}
		};
		aa.setMinimumSize(w, h);
		aa.setMaximumSize(w, h);

		setContentPane(a);
		a.add(aa);

		g.graph(new float[] {0, 0, 1, 1, 2, 2, 3});

		glClearColor(0f, 0f, 0f, 1);

		repack();
		GLui.Window.profileInfo = false;

		render();
	}

	@SuppressWarnings("unused")
	public static void main(String[] argv) {
		new Window(1200, 800);
	}

	@Override
	public void glInit() {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

}
