package Rendering;

import static Util.GLCONST.VBO_DYNAMIC;
import static Util.GLCONST.VBO_ELEMENT_ARRAY_BUFFER;
import static Util.GLCONST.TYPE_UINT;

import java.nio.IntBuffer;

import Util.Debug;
import Util.GLCONST;

public class VBOIndexData {

	private final VBO vbo;
	private int size = 0;

	/**
	 * Creates and populates an index buffer with ints between begin and end inclusive sequentially.
	 * @param begin first index.
	 * @param end last index.
	 */
	public VBOIndexData(int begin, int end) {
		String name = new StringBuilder("~~indexRANGE ").append(begin).append(" - ").append(end).toString();
		VBO vbo = VBOManager.getVBO(name);
		size = end - begin + 1;
		if (vbo == null) {
			int[] indices = new int[size];
			for (int i = 0; i < indices.length; i++)
				indices[i] = begin + i;
			vbo = VBOManager.createStaticVBO(name, indices, VBO_ELEMENT_ARRAY_BUFFER);
		}
		this.vbo = vbo;
	}

	/**
	 * Creates an int VBO with the DYNAMIC hint for use with index data.
	 * @param name name to be associated with the resulting index buffer.
	 */
	public VBOIndexData(String name) {
		VBO vbo = VBOManager.getVBO(name);
		if (vbo != null) {
			Debug.log(Debug.VBO_MANAGER, "Failure to create dynamic index data buffer. Name collision: [", name, "].");
			this.vbo = null;
			return;
		}
		this.vbo = VBOManager.createVBO(name, TYPE_UINT);
	}

	/**
	 * Creates an int VBO with the STATIC hint for use with index data. Buffers the provided data.
	 * @param name name to be associated with the resulting index buffer.
	 * @param data data to be buffered.
	 */
	public VBOIndexData(String name, int[] data) {
		VBO vbo = VBOManager.getVBO(name);
		size = data.length;
		if (vbo != null) {
			Debug.log(Debug.VBO_MANAGER, "Failure to create static index data buffer. Name collision: [", name, "].");
			this.vbo = null;
			return;
		}
		this.vbo = VBOManager.createStaticVBO(name, data, GLCONST.VBO_ELEMENT_ARRAY_BUFFER);
	}

	// FIXME typecheck
	/**
	 * Specifies an empty int VBO to be used as the index buffer.
	 * @param vbo an empty int VBO.
	 */
	public VBOIndexData(VBO vbo) {
		this.vbo = vbo;
	}

	public void bufferData(int[] data) {
		vbo.bufferData(VBO_ELEMENT_ARRAY_BUFFER, data, VBO_DYNAMIC);
		size = data.length;
		VBOManager.unbindVBO(VBO_ELEMENT_ARRAY_BUFFER);
	}

	public void bufferData(IntBuffer data) {
		vbo.bufferData(VBO_ELEMENT_ARRAY_BUFFER, data, VBO_DYNAMIC);
		size = data.limit();
		VBOManager.unbindVBO(VBO_ELEMENT_ARRAY_BUFFER);
	}

	public void enableBuffer() {
		VBOManager.bindVBO(vbo, VBO_ELEMENT_ARRAY_BUFFER);
	}

	public static void disableBuffer() {
		VBOManager.unbindVBO(VBO_ELEMENT_ARRAY_BUFFER);
	}

	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		return vbo.name;
	}

}
