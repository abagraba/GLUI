package Testing;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import GLUICore.Renderable;
import GLUIRenderer.CenterPane;

public class Window extends GLUICore.Window {

	Renderable r = new TestPane();

	public Window(int w, int h) {
		super(w, h);
		CenterPane cp = new CenterPane();
		setContentPane(cp);

		// TSM tsm = new TSM();
		// cp.add(tsm);

		/*
		 * tsm.addLayer(new TSL() {
		 * 
		 * @Override public Collection<T> tileData() { LinkedList<T> tiles = new LinkedList<T>(); tiles.add(new T(0, 0,
		 * "Stone", 1)); return tiles; } });
		 */

		cp.add(r);
		glClearColor(0f, 0f, 0f, 1);

		repack();
		System.out.println(r);
		GLUICore.Window.profileInfo = true;
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
