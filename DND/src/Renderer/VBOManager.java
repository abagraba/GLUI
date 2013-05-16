package Renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.util.Hashtable;

public class VBOManager {

	public static final int[] V2T2 = new int[] {2, 0, 2, 0};

	public static final int[] V3T2 = new int[] {3, 0, 2, 0};
	public static final int[] V3T2N3 = new int[] {3, 0, 2, 3};

	public static final int[] V2C3 = new int[] {2, 3, 0, 0};
	public static final int[] V2C4 = new int[] {2, 4, 0, 0};

	public static final int[] V3C3 = new int[] {3, 3, 0, 0};
	public static final int[] V3C3N3 = new int[] {3, 3, 3, 0};
	public static final int[] V3C4 = new int[] {3, 4, 0, 0};
	public static final int[] V3C4N3 = new int[] {3, 4, 3, 0};

	public static final int[] V2C3T2 = new int[] {2, 3, 2, 0};
	public static final int[] V2C4T2 = new int[] {2, 4, 2, 0};

	public static final int[] V3C3T2 = new int[] {3, 3, 2, 0};
	public static final int[] V3C4T2 = new int[] {3, 4, 2, 0};
	public static final int[] V3C3T2N3 = new int[] {3, 3, 2, 3};
	public static final int[] V3C4T2N3 = new int[] {3, 4, 2, 3};

	private static final int V = 0;
	private static final int C = 1;
	private static final int T = 2;
	private static final int N = 3;

	private static Hashtable<String, VBO> vbos = new Hashtable<String, VBO>();
	private static String root = "src/Texture/";

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
	 * @param filename name of the ITM file. The ITM file must exist in the folder specified by root. The filename is specified without filetype. Ex:
	 *        "Object" not "Object.itm".
	 * @param type render type of the VBO. Valid values: GL_TRIANGLES, GL_TRIANGLE_FAN, GL_TRIANGLE_STRIP, GL_QUADS, GL_QUAD_STRIP.
	 * @param interleaving specifies the contents of the interleaved array in the format [V, C, T, N].
	 */
	public static void createVBO(String filename, int type, int[] interleaving) {
		int vboID = glGenBuffers();
		int indID = glGenBuffers();

		float[] f = ITMReader.readData(root + filename + ".itm");
		VBO v = new VBO(vboID, indID, f.length, type, interleaving);

		int[] ind = new int[f.length / v.vertexSize()];
		for (int i = 0; i < ind.length; i++)
			ind[i] = i;

		v.bind();
		glBufferData(GL_ARRAY_BUFFER, BufferUtil.asDirectFloatBuffer(f), GL_STATIC_DRAW);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtil.asDirectIntBuffer(ind), GL_STATIC_DRAW);
		v.unbind();

		vbos.put(filename, v);
	}

	public static void createDynamicVBO(String name, int type, int[] interleaving) {
		if (!vbos.containsKey("~" + name))
			vbos.put("~" + name, new VBO(glGenBuffers(), glGenBuffers(), 0, type, interleaving));
	}

	public static void DynamicDraw(String name, float[] vertices) {
		if (!vbos.containsKey("~" + name))
			return;
		VBO v = vbos.get("~" + name);

		v.bind();

		glBufferData(GL_ARRAY_BUFFER, BufferUtil.asDirectFloatBuffer(vertices), GL_DYNAMIC_DRAW);

		if (v.size != vertices.length) {
			int[] ind = new int[vertices.length / v.vertexSize()];
			for (int i = 0; i < ind.length; i++)
				ind[i] = i;
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtil.asDirectIntBuffer(ind), GL_DYNAMIC_DRAW);
			v.size = vertices.length;
		}

		v.enableStates();
		glDrawElements(v.type, v.size/v.vertexSize(), GL_UNSIGNED_INT, 0);
		v.disableStates();
		v.unbind();
	}

	/**
	 * Sets the root directory for the VBOManager. All ITM files will be searched for in the root directory.
	 * @param root new root directory.
	 */
	public static void setRoot(String root) {
		VBOManager.root = root;
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
		for (int i = 0; i < vbos.length; i++)
			clean(vbos[i]);
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
		int[] interleaving;

		private VBO(int vboID, int indID, int size, int type, int[] interleaving) {
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
		public void unbind(){
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		}

		public int vertexSize() {
			return interleaving[V] + interleaving[C] + interleaving[T] + interleaving[N];
		}

		public void draw() {
			enableStates();
			glDrawElements(type, size, GL_UNSIGNED_INT, 0);
			disableStates();
		}

		public void enableStates() {
			int vOff = 0;
			int cOff = interleaving[V] * 4;
			int tOff = cOff + interleaving[C] * 4;
			int nOff = tOff + interleaving[T] * 4;
			int stride = nOff + interleaving[N] * 4;

			if (interleaving[V] > 0) {
				glEnableClientState(GL_VERTEX_ARRAY);
				glVertexPointer(interleaving[V], GL_FLOAT, stride, vOff);
			}
			if (interleaving[C] > 0) {
				glEnableClientState(GL_COLOR_ARRAY);
				glColorPointer(interleaving[C], GL_FLOAT, stride, cOff);
			}
			if (interleaving[T] > 0) {
				glEnableClientState(GL_TEXTURE_COORD_ARRAY);
				glTexCoordPointer(interleaving[T], GL_FLOAT, stride, tOff);
			}
			if (interleaving[N] == 3) {
				glEnableClientState(GL_NORMAL_ARRAY);
				glNormalPointer(GL_FLOAT, stride, nOff);
			}
		}

		public void disableStates() {
			if (interleaving[V] > 0)
				glDisableClientState(GL_VERTEX_ARRAY);
			if (interleaving[C] > 0)
				glDisableClientState(GL_COLOR_ARRAY);
			if (interleaving[T] > 0)
				glDisableClientState(GL_TEXTURE_COORD_ARRAY);
			if (interleaving[N] > 0)
				glDisableClientState(GL_NORMAL_ARRAY);
		}
	}

}
