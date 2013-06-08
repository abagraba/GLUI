package Testing;


import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

import GLUICore.TextureManager;
import GLUICore.VBOManager;

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
		VBOManager.createStaticVBO("Square", GL_QUADS, VBOManager.V2T2);
		TextureManager.setRoot("src/Testing/");

		glEnable(GL_TEXTURE_2D);
		TextureManager.useTexture("Test", GL_TEXTURE_2D, GL_NEAREST);
		
		while ( !Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			VBOManager.getVBO("Square").bind();
			VBOManager.getVBO("Square").draw();
			
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

	@SuppressWarnings("unused")
	public static void main(String[] argv) {
		new VBOTest();
	}

}
