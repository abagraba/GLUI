package Rendering;

import static Util.GLCONST.ARRAY_BUFFER;
import static Util.GLCONST.DYNAMIC;
import static Util.GLCONST.FLOAT;
import Util.GLCONST;

public class VBOVertexData {

	private final VBO vbo;
	private final VBOInterleave interleaving;

	/**
	 * Creates an float VBO used to store vertexData using the DYNAMIC hint. For STATIC see
	 * {@link #VBOVertexData(String, float[], VBOInterleave)}.
	 * @param name name to be associated with the resulting vertex buffer.
	 */
	public VBOVertexData(String name, VBOInterleave interleaving) {
		vbo = VBOManager.createVBO(name, FLOAT);
		this.interleaving = interleaving;
	}

	/**
	 * Creates an float VBO used to store vertexData using the STATIC hint. Buffers the provided data. For STATIC see
	 * {@link #VBOVertexData(String, float[], VBOInterleave)}.
	 * @param name name to be associated with the resulting vertex buffer.
	 */
	public VBOVertexData(String name, float[] data, VBOInterleave interleaving) {
		vbo = VBOManager.createStaticVBO(name, data, GLCONST.ARRAY_BUFFER);
		this.interleaving = interleaving;
	}

	public VBOVertexData(VBO vbo, VBOInterleave interleaving) {
		this.vbo = vbo;
		this.interleaving = interleaving;
	}

	public void bufferData(float[] data) {
		vbo.bufferData(ARRAY_BUFFER, data, DYNAMIC);
		VBOManager.unbindVBO(ARRAY_BUFFER);
	}

	public void preDraw() {
		VBOManager.bindVBO(vbo, ARRAY_BUFFER);
		interleaving.enableStates();
	}

	public void postDraw() {
		interleaving.disableStates();
		VBOManager.unbindVBO(ARRAY_BUFFER);
	}

	@Override
	public String toString() {
		return vbo.name;
	}

}
