package Managers;

import static Util.GLCONST.*;

public class IndexData {

	private final VBO vbo;
	public int size = 0;

	/**
	 * Creates and populates an index buffer with ints between begin and end inclusive sequentially.
	 * @param begin first index.
	 * @param end last index.
	 */
	public IndexData(int begin, int end) {
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
	 * Specifies a name to be bound to the index buffer that is being created.
	 * @param name name to be associated with the resulting index buffer.
	 */
	public IndexData(String name) {
		VBO vbo = VBOManager.getVBO(name);
		if (vbo == null)
			vbo = VBOManager.createVBO(name, INT);
		this.vbo = vbo;
	}

	/**
	 * Specifies an empty int VBO to be used as the index buffer.
	 * @param vbo an empty int VBO.
	 */
	public IndexData(VBO vbo) {
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

	public void disableData() {
		VBOManager.unbindVBO(ELEMENT_ARRAY_BUFFER);
	}

	@Override
	public String toString() {
		return vbo.name;
	}

}
