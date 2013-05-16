package Testing;


import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

import Renderer.TextureManager;
import Renderer.VBOManager;

public class VBOTest {

	public VBOTest() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho( -1, 1, -1, 1, -1, 1);
		glMatrixMode(GL_MODELVIEW);

		glClearColor(0.5f, 0.5f, 0.7f, 1);

		VBOManager.setRoot("src/Testing/");
		VBOManager.createVBO("Tile", GL_QUADS, VBOManager.V2T2);
		TextureManager.setRoot("src/Testing/");

		glEnable(GL_TEXTURE_2D);
		TextureManager.bindTexture("Test", GL_TEXTURE_2D, GL_NEAREST);
		
		while ( !Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			VBOManager.getVBO("Tile").bind();
			VBOManager.getVBO("Tile").draw();
			
			checkError();
			Display.update();
		}
		VBOManager.cleanAll();
		TextureManager.cleanAll();
		Display.destroy();
	}

	private void checkError() {
		int error = glGetError();
		if (error != GL_NO_ERROR)
			System.out.println(GLU.gluErrorString(error));
	}

	public static void main(String[] argv) {
		new VBOTest();
	}

}
