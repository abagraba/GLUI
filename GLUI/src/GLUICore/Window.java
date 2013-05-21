package GLUICore;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

public abstract class Window {

	private Pane contentPane;
	private int fps = 60;
	private boolean debugFramerate = false;

	public Window(int w, int h) {
		try {
			Display.setDisplayMode(new DisplayMode(w, h));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		contentPane = new Pane(0, 0, w, h) {
			@Override
			public void validatePosition() {}
		};

		glInit();

		Mouse.getDWheel();
		glClearColor(0f, 0f, 0f, 1);
	}

	public abstract void glInit();

	public void setContentPane(Pane p) {
		contentPane = p;
	}

	public void repack() {
		if (contentPane != null)
			contentPane.resize(0, 0, Display.getWidth(), Display.getHeight());
	}

	public void render() {
		while (!Display.isCloseRequested()) {
			long t = System.nanoTime();

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			if (contentPane != null)
				contentPane.prerender();

			Display.update();
			handleEvents();
			checkError();

			if (debugFramerate)
				System.out.println((System.nanoTime() - t) / 1000000.0f + "ms");

			Display.sync(fps);
		}
		cleanup();
	}

	public void add(Pane p) {
		contentPane.add(p);
	}

	public void remove(Pane p) {
		contentPane.remove(p);
	}

	public void cleanup() {
		VBOManager.cleanAll();
		TextureManager.cleanAll();
		ShaderManager.cleanAll();
		Display.destroy();
	}

	protected void checkError() {
		int error = glGetError();
		if (error != GL_NO_ERROR)
			System.out.println(GLU.gluErrorString(error));
	}

	public final void handleEvents() {
		while (Keyboard.next())
			if (contentPane != null)
				contentPane.handleKeyEvent(new KeyEvent(Keyboard.getEventKey(), Keyboard.getEventKeyState()));
		while (Mouse.next())
			if (contentPane != null)
				contentPane.handleMouseEvent(new MouseEvent(Mouse.getEventButton(), Mouse.getEventX(), Mouse.getEventY(),
						Mouse.getEventButtonState()));
		int x = Mouse.getX();
		int y = Mouse.getY();
		int dx = Mouse.getDX();
		int dy = Mouse.getDY();
		int delta = Mouse.getDWheel();
		if (contentPane != null) {
			if (dx != 0 || dy != 0)
				contentPane.handleMouseMoveEvent(new MouseMoveEvent(dx, dy, x, y));
			if (delta != 0)
				contentPane.handleMouseScrollEvent(new MouseScrollEvent(delta, x, y));
			// TODO ctrl key and such modifiers
			// TODO mouse drag
		}
	}

}
