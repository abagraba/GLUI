package Testing;

import java.awt.Point;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

import Core.MapView;
import GLUICore.ShaderManager;
import GLUICore.TextureManager;
import GLUICore.VBOManager;
import Renderer.TileMap;
import Renderer.TileMapRenderer;

public class TileMapTest {

	public TileMapTest() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		TextureManager.setRoot("src/Testing/");
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		TileMap tm = new Map(20, 20);
		TileMapRenderer tmr = new MapView(50, 50, 700, 500, tm);
		tmr.zoom(15);
		
		
		glClearColor(0.8f, 0.5f, 0.7f, 1);
		Mouse.getDWheel();
		
		while (!Display.isCloseRequested()) {
			while (Keyboard.next()) {
				if (Keyboard.getEventKeyState()) {
					int dx = 0, dy = 0;
					if (Keyboard.getEventKey() == Keyboard.KEY_LEFT)
						dx -= 1;
					if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT)
						dx += 1;
					if (Keyboard.getEventKey() == Keyboard.KEY_DOWN)
						dy -= 1;
					if (Keyboard.getEventKey() == Keyboard.KEY_UP)
						dy += 1;
					tmr.move(new Point(dx, dy));
				}
			}
			tmr.zoom(Mouse.getDWheel(), Mouse.getX(), Mouse.getY());

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			tmr.render();

			checkError();
			Display.update();
		}

		VBOManager.cleanAll();
		TextureManager.cleanAll();
		ShaderManager.cleanAll();
		Display.destroy();
	}

	private void checkError() {
		int error = glGetError();
		if (error != GL_NO_ERROR)
			System.out.println(GLU.gluErrorString(error));
	}

	public static void main(String[] argv) {
		new TileMapTest();
	}

}
