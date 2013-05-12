package Testing;


import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.lwjgl.util.glu.GLU;

import Renderer.BufferUtil;

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

		int vbo = glGenBuffers();
		int ind = glGenBuffers();
		
		float min = -0.5f, max = 0.5f;
		float[] vertices = new float[] {min, max, min, min, max, min, max, max};
		int[] indices = new int[] {0, 1, 2, 3};
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtil.asDirectFloatBuffer(vertices), GL_DYNAMIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ind);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtil.asDirectIntBuffer(indices), GL_DYNAMIC_DRAW);

		
		while ( !Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glColor3f(0.5f, 0.1f, 0.1f);
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glEnableClientState(GL_VERTEX_ARRAY);
			glVertexPointer(2, GL_FLOAT, 2 * 4, 0);
			glDrawElements(GL_QUADS, 4, GL_UNSIGNED_INT, 0);
			glDisableClientState(GL_VERTEX_ARRAY);
			checkError();
			Display.update();
		}
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
