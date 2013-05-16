package Testing;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.lwjgl.util.glu.GLU;

import Renderer.BufferUtil;
import Renderer.ShaderManager;
import Renderer.TextureManager;
import Renderer.VBOManager;

public class VBOProfile {


	public VBOProfile() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 20, 0, 15, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		VBOManager.setRoot("src/Testing/");
		VBOManager.createVBO("Tile", GL_QUADS, VBOManager.V2T2);

		glEnable(GL_TEXTURE_2D);
		TextureManager.setRoot("src/Testing/");
		TextureManager.bindTexture("Test", GL_TEXTURE_2D, GL_NEAREST);

		glClearColor(0.7f, 0.5f, 0.5f, 1);
		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			long t = System.nanoTime();
//			renderVBO();
			renderTile();
			System.out.println((System.nanoTime()-t)/1000000.0f);
			checkError();
			Display.update();
		}

		VBOManager.cleanAll();
		TextureManager.cleanAll();
		ShaderManager.cleanAll();
		Display.destroy();
	}

	private void writeData(float[] dest, float[] in, int off) {
		for (int i = 0; i < in.length; i++)
			dest[off + i] = in[i];
	}

	@SuppressWarnings("unused")
	private void renderVBO() {

		float[] in = new float[] {0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0};

		float[] f = new float[20 * 15 * 16];
		for (int x = 0; x < 20; x++)
			for (int y = 0; y < 15; y++) {
				float[] z = new float[16];
				for (int i = 0; i < 16; i++)
					z[i] = i % 4 == 0 ? in[i] + x : i % 4 == 1 ? in[i] + y : in[i];
				writeData(f, z, (y * 20 + x) * 16);
				// System.out.println(z[0] + " " + z[1] + " " + z[2] + " " + z[3] + " " + z[4] + " " + z[5] + " " + z[6] + " " + z[7] + " " + z[8] +
				// " " + z[9] + " " + z[10] + " " + z[11] + " " + z[12] + " " + z[13] + " " + z[14] + " " + z[15]);
			}

		int[] ind = new int[f.length / 2];
		for (int i = 0; i < ind.length; i++)
			ind[i] = i;

		int vboID = glGenBuffers();
		int indID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, BufferUtil.asDirectFloatBuffer(f), GL_DYNAMIC_DRAW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtil.asDirectIntBuffer(ind), GL_STATIC_DRAW);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glVertexPointer(2, GL_FLOAT, 4 * 4, 0 * 4);
		glTexCoordPointer(2, GL_FLOAT, 4 * 4, 2 * 4);

		glDrawElements(GL_QUADS, ind.length, GL_UNSIGNED_INT, 0);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDeleteBuffers(vboID);
		glDeleteBuffers(indID);
	}

	private void renderTile() {
		for (int x = 0; x < 20; x++)
			for (int y = 0; y < 15; y++) {
				glPushMatrix();
				glTranslatef(x, y, 0);
				VBOManager.getVBO("Tile").bind();
				VBOManager.getVBO("Tile").draw();
				glPopMatrix();
			}
	}

	private void checkError() {
		int error = glGetError();
		if (error != GL_NO_ERROR)
			System.out.println(GLU.gluErrorString(error));
	}

	public static void main(String[] argv) {
		new VBOProfile();
	}

}
