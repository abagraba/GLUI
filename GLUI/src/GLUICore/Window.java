package GLUICore;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glGetError;

import java.io.File;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

public abstract class Window extends EventHandler {

	private RenderContainer contentPane;
	private final int fps = 60;
	public static boolean profileInfo = false;

	public Window(int w, int h) {
		try {
			Display.setDisplayMode(new DisplayMode(w, h));
			Display.create();
		}
		catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		contentPane = new RenderContainer() {
			@Override
			public void validatePosition() {}
		};

		Mouse.getDX();
		Mouse.getDY();
		Mouse.getDWheel();

		repack();

		glInit();
		glClearColor(0f, 0f, 0f, 1);

		ResourceManager.setRoot(new File("../DND/res"));
		ResourceManager.setTextureRoot("/Texture");

	}

	public void setContentPane(RenderContainer p) {
		contentPane = p;
	}

	public abstract void glInit();

	public void repack() {
		if (contentPane != null) {
			contentPane.reposition(0, 0);
			contentPane.resize(Display.getWidth(), Display.getHeight());
			contentPane.repack();
		}
	}

	public void render() {
		while (!Display.isCloseRequested()) {
			long t = System.nanoTime();

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			if (contentPane != null)
				contentPane.prerender(0, 0);

			Display.update();
			handleEvents();
			checkError();

			if (profileInfo) {
				Debug.profile("Frame Render Time", System.nanoTime() - t, 0, 0);
				Debug.logProfile();
			}

			Display.sync(fps);

		}
		cleanup();
	}

	public void add(RenderContainer p) {
		contentPane.add(p);
	}

	public void remove(RenderContainer p) {
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
			Debug.log(GLU.gluErrorString(error));
	}

	public final void handleEvents() {
		// FIXME let window handle events too
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
