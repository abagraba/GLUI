package Managers;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import Rendering.VBOIndexData;
import Rendering.VBOInterleave;
import Rendering.VBOVertexData;

public class Test2 {

	public static void main(String[] args) {
		try {
			Display.create();
		}
		catch (LWJGLException e) {
			e.printStackTrace();
		}

		int d = 500;
		GL11.glOrtho(0, d, 0, d, -1, 1);
		float[] data = new float[d * d * 20];
		for (int x = 0; x < d; x++)
			for (int y = 0; y < d; y++) {
				int i = x * d + y;
				data[i * 20] = x;
				data[i * 20 + 1] = y;
				data[i * 20 + 2] = 1;
				data[i * 20 + 3] = 0;
				data[i * 20 + 4] = 0;
				data[i * 20 + 5] = x + 1;
				data[i * 20 + 6] = y;
				data[i * 20 + 7] = 1;
				data[i * 20 + 8] = 1;
				data[i * 20 + 9] = 1;
				data[i * 20 + 10] = x + 1;
				data[i * 20 + 11] = y + 1;
				data[i * 20 + 12] = 0;
				data[i * 20 + 13] = 1;
				data[i * 20 + 14] = 0;
				data[i * 20 + 15] = x;
				data[i * 20 + 16] = y + 1;
				data[i * 20 + 17] = 0;
				data[i * 20 + 18] = 0;
				data[i * 20 + 19] = 1;
			}

		VBOVertexData vd = new VBOVertexData("TestVertices", VBOInterleave.V2C3);
		vd.bufferData(data);
		VBOIndexData id = new VBOIndexData(0, d * d * 4);
		System.out.println(GLU.gluErrorString(GL11.glGetError()));

		while (!Display.isCloseRequested()) {
			long t = System.nanoTime();
			vd.bufferData(data);
			System.out.println((System.nanoTime() - t) * .000001f + "ms");

			long tt = System.nanoTime();
			vd.enableBuffer();
			id.enableBuffer();
			GL11.glDrawElements(GL11.GL_QUADS, d * d * 4, GL11.GL_UNSIGNED_INT, 0);
			id.disableBuffer();
			vd.disableBuffer();
			System.out.println((System.nanoTime() - tt) * .000001f);
			Display.update();

		}
	}

}
