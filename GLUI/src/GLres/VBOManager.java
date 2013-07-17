package GLres;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.util.Hashtable;

import Util.BufferUtil;

import GLui.Debug;
import GLui.InterleavedVBO;

public class VBOManager {

	private static Hashtable<String, VBO> vbos = new Hashtable<String, VBO>();

	/**
	 * Returns a VBO object with all the necessary rendering information.
	 * @param name name of the VBO.
	 * @return the VBO.
	 */
	public static VBO getVBO(String name) {
		return vbos.get(name);
	}

	/**
	 * Initializes a VBO from the specified ITM file.
	 * @param filename name of the ITM file. The ITM file must exist in the folder specified by root. The filename is
	 *        specified without filetype. Ex: "Object" not "Object.itm".
	 * @param type render type of the VBO. Valid values: GL_TRIANGLES, GL_TRIANGLE_FAN, GL_TRIANGLE_STRIP, GL_QUADS,
	 *        GL_QUAD_STRIP.
	 * @param interleaving specifies the contents of the interleaved array in the format [V, C, T, N].
	 */
	public static void createStaticVBO(String filename, int type, InterleavedVBO interleaving, float[] data) {
		int vboID = glGenBuffers();
		int indID = glGenBuffers();

		VBO v = new VBO(vboID, indID, data.length, type, interleaving);

		v.bufferData(data, GL_STATIC_DRAW);

		vbos.put(filename, v);
	}

	public static void createDynamicVBO(String name, int type, InterleavedVBO interleaving) {
		if (!vbos.containsKey("~" + name))
			vbos.put("~" + name, new VBO(glGenBuffers(), glGenBuffers(), 0, type, interleaving));
	}

	public static void dynamicDraw(String name, float[] vertices) {
		if (!vbos.containsKey("~" + name))
			return;
		VBO v = vbos.get("~" + name);
		long profileBuffer = System.nanoTime();
		v.bufferData(vertices, GL_DYNAMIC_DRAW);
		profileBuffer = System.nanoTime() - profileBuffer;

		long profileDraw = System.nanoTime();
		v.draw();
		profileDraw = System.nanoTime() - profileDraw;

		if (Debug.profile[Debug.verboseVBO]) {
			Debug.profile("Drawing VBO Data", profileDraw, 0, Debug.drawMessage);
			Debug.profile("Buffering VBO Data", profileBuffer, 0, Debug.drawMessage);
		}

	}

	/**
	 * Cleans up the specified VBO.
	 * @param vbo the specified VBO.
	 */
	public static void clean(String vbo) {
		VBO v = vbos.remove(vbo);
		glDeleteBuffers(v.vboID);
		glDeleteBuffers(v.indID);
	}

	/**
	 * Cleans up the specified VBOs
	 * @param vbos the specified VBOs.
	 */
	public static void clean(String[] vbos) {
		for (String vbo : vbos)
			clean(vbo);
	}

	/**
	 * Cleans up all the VBOs.
	 */
	public static void cleanAll() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		for (VBO v : vbos.values()) {
			glDeleteBuffers(v.vboID);
			glDeleteBuffers(v.indID);
		}
		vbos.clear();
	}

	public static class VBO {
		int vboID;
		int indID;
		int size;
		int type;
		InterleavedVBO interleaving;

		protected VBO(int vboID, int indID, int size, int type, InterleavedVBO interleaving) {
			this.vboID = vboID;
			this.indID = indID;
			this.size = size;
			this.type = type;
			this.interleaving = interleaving;
		}

		public void bind() {
			glBindBuffer(GL_ARRAY_BUFFER, vboID);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indID);
		}

		public void unbind() {
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		}

		public void bufferData(float[] vert, int[] ind, int VBOtype) {
			if (vert == null) {
				Debug.log("VBOManager: Vertex data is null.");
				return;
			}
			if (ind == null) {
				Debug.log("VBOManager: Index data is null.");
				return;
			}
			if (vert.length % interleaving.length != 0) {
				Debug.log("VBOManager: Vertex data is of invalid length.");
				return;
			}
			size = vert.length / interleaving.length;
			bind();
			glBufferData(GL_ARRAY_BUFFER, BufferUtil.asDirectFloatBuffer(vert), VBOtype);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtil.asDirectIntBuffer(ind), VBOtype);
			unbind();
		}

		public void bufferData(float[] vert, int VBOtype) {
			if (vert == null) {
				Debug.log("VBOManager: Vertex data is null.");
				return;
			}
			if (vert.length % interleaving.length != 0) {
				Debug.log("VBOManager: Vertex data is of invalid length.");
				return;
			}
			int newsize = vert.length / interleaving.length;
			bind();
			glBufferData(GL_ARRAY_BUFFER, BufferUtil.asDirectFloatBuffer(vert), VBOtype);
			if (newsize != size) {
				int[] ind = new int[newsize];
				for (int i = 0; i < ind.length; i++)
					ind[i] = i;
				glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtil.asDirectIntBuffer(ind), VBOtype);
				size = newsize;
			}
			unbind();
		}

		public void draw() {
			bind();
			interleaving.enableStates();
			glDrawElements(type, size, GL_UNSIGNED_INT, 0);
			interleaving.disableStates();
			unbind();
		}

	}

}
