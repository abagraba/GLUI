package Testing;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

public class Window extends GLui.Window {

	public Window(int w, int h) {
		super(w, h);
		setContentPane(new TestContentPane());

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
