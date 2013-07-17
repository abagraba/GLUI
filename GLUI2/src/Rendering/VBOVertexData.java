package Rendering;

import static Util.GLCONST.ARRAY_BUFFER;
import static Util.GLCONST.DYNAMIC;
import static Util.GLCONST.FLOAT;
import Managers.Interleaving;
import Util.GLCONST;

public class VBOVertexData {

	private final VBO vbo;
	private final Interleaving interleaving;

	/**
	 * Creates an float VBO with the STATIC hint for use with vertex data. Buffers the provided data.
	 * @param name name to be associated with the resulting vertex buffer.
	 */
	public VBOVertexData(String name, Interleaving interleaving) {
		vbo = VBOManager.createVBO(name, FLOAT);
		this.interleaving = interleaving;
	}

	/**
	 * Creates an float VBO with the STATIC hint for use with vertex data. Buffers the provided data.
	 * @param name name to be associated with the resulting vertex buffer.
	 * @param data data to be buffered.
	 */
	public VBOVertexData(String name, float[] data, Interleaving interleaving) {
		vbo = VBOManager.createStaticVBO(name, data, GLCONST.ARRAY_BUFFER);
		this.interleaving = interleaving;
	}

	public VBOVertexData(VBO vbo, Interleaving interleaving) {
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
