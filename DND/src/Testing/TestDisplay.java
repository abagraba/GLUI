package Testing;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

import GLUICore.ShaderManager;
import GLUICore.TextureManager;
import GLUICore.VBOManager;

public class TestDisplay {

	private static final boolean shaders = true;
	private static final boolean textures = true;
	private static final boolean vbos = true;
	private static final boolean immediate = false;

	public TestDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 1, 0, 1, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		if (vbos)
			VBOManager.createVBO("Tile", GL_QUADS, VBOManager.V2T2);
		if (shaders) {
			ShaderManager.createProgram("Main", "Basic/Textured", "Basic/Textured");
			ShaderManager.useProgram("Main");
		}

		if (textures) {
			glEnable(GL_TEXTURE_2D);
			TextureManager.bindTexture("Test", GL_TEXTURE_2D, GL_NEAREST);
		}
		// glViewport(0, 0, 300, 300);

		glClearColor(0.7f, 0.5f, 0.5f, 1);
		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			if (immediate) {
				glBegin(GL_QUADS);
				glTexCoord2f(0, 1);
				glVertex2f(-0.5f, -0.5f);
				glTexCoord2f(0, 0);
				glVertex2f(-0.5f, 0.5f);
				glTexCoord2f(1, 0);
				glVertex2f(0.5f, 0.5f);
				glTexCoord2f(1, 1);
				glVertex2f(0.5f, -0.5f);
				glEnd();
			}
			if (vbos) {
				VBOManager.VBO v = VBOManager.getVBO("Tile");
				v.bind();
				v.draw();
			}
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
		new TestDisplay();
	}

}
