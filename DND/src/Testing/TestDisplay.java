package Testing;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import GLUICore.InterleavedVBO;
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
		}
		catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 1, 0, 1, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		if (vbos)
			VBOManager.createStaticVBO("Tile", GL_QUADS, InterleavedVBO.V2T2);
		if (shaders) {
			ShaderManager.createProgram("Main", "Basic/Textured", "Basic/Textured");
			ShaderManager.useProgram("Main");
		}

		if (textures) {
			glEnable(GL_TEXTURE_2D);
			TextureManager.useTexture("Test", GL_TEXTURE_2D, GL_NEAREST);
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

	private static void checkError() {
		int error = glGetError();
		if (error != GL_NO_ERROR)
			System.out.println(GLU.gluErrorString(error));
	}

	@SuppressWarnings("unused")
	public static void main(String[] argv) {
		new TestDisplay();
	}

}
