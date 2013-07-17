package Rendering;

import static Util.GLCONST.DYNAMIC;
import static Util.GLCONST.ELEMENT_ARRAY_BUFFER;
import static Util.GLCONST.INT;
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
			vbo = VBOManager.createStaticVBO(name, indices, ELEMENT_ARRAY_BUFFER);
		}
		this.vbo = vbo;
	}

	/**
	 * Creates an int VBO used to store indexData using the DYNAMIC hint. For STATIC see
	 * {@link #VBOIndexData(String, int[])}.
	 * @param name name to be associated with the resulting index buffer.
	 */
	public VBOIndexData(String name) {
		VBO vbo = VBOManager.getVBO(name);
		if (vbo == null)
			vbo = VBOManager.createVBO(name, INT);
		this.vbo = vbo;
	}

	/**
	 * Creates an int VBO used to store indexData using the STATIC hint. Buffers the provided data. For DYNAMIC see
	 * {@link #VBOIndexData(String)}
	 * @param name name to be associated with the resulting index buffer.
	 * @param data data to be buffered initially.
	 */
	public VBOIndexData(String name, int[] data) {
		VBO vbo = VBOManager.getVBO(name);
		if (vbo == null)
			vbo = VBOManager.createStaticVBO(name, data, GLCONST.ELEMENT_ARRAY_BUFFER);
		this.vbo = vbo;
	}

	/**
	 * Specifies an empty int VBO to be used as the index buffer.
	 * @param vbo an empty int VBO.
	 */
	public VBOIndexData(VBO vbo) {
		this.vbo = vbo;
	}

	public void bufferData(int[] data) {
		vbo.bufferData(ELEMENT_ARRAY_BUFFER, data, DYNAMIC);
		size = data.length;
		VBOManager.unbindVBO(ELEMENT_ARRAY_BUFFER);
	}

	public void enableData() {
		VBOManager.bindVBO(vbo, ELEMENT_ARRAY_BUFFER);
	}

	@SuppressWarnings("static-method")
	public void disableData() {
		VBOManager.unbindVBO(ELEMENT_ARRAY_BUFFER);
	}

	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		return vbo.name;
	}

}