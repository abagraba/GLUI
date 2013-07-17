package Rendering;

import static Util.GLCONST.ARRAY_BUFFER;
import static Util.GLCONST.DYNAMIC;
import static Util.GLCONST.FLOAT;

import java.nio.FloatBuffer;

import Util.Debug;
import Util.GLCONST;

public class VBOVertexData {

	private final VBO vbo;
	private final VBOInterleave interleaving;

	/**
	 * Creates an float VBO with the STATIC hint for use with vertex data. Buffers the provided data.
	 * @param name name to be associated with the resulting vertex buffer.
	 * @param interleaving {@link VBOInterleave} that describes the layout of interleaved data.
	 */
	public VBOVertexData(String name, VBOInterleave interleaving) {
		this.interleaving = interleaving;
		VBO vbo = VBOManager.getVBO(name);
		if (vbo != null) {
			Debug.log(Debug.VBO_MANAGER, "Failure to create dynamic vertex data buffer. Name collision: [", name, "].");
			this.vbo = null;
			return;
		}
		this.vbo = VBOManager.createVBO(name, FLOAT);
	}

	/**
	 * Creates an float VBO with the STATIC hint for use with vertex data. Buffers the provided data.
	 * @param name name to be associated with the resulting vertex buffer.
	 * @param data data to be buffered.
	 * @param interleaving {@link VBOInterleave} that describes the layout of interleaved data.
	 */
	public VBOVertexData(String name, float[] data, VBOInterleave interleaving) {
		this.interleaving = interleaving;
		VBO vbo = VBOManager.getVBO(name);
		if (vbo != null) {
			Debug.log(Debug.VBO_MANAGER, "Failure to create static vertex data buffer. Name collision: [", name, "].");
			this.vbo = null;
			return;
		}
		this.vbo = VBOManager.createStaticVBO(name, data, GLCONST.ARRAY_BUFFER);
	}

	public VBOVertexData(VBO vbo, VBOInterleave interleaving) {
		this.vbo = vbo;
		this.interleaving = interleaving;
	}

	public void bufferData(float[] data) {
		vbo.bufferData(ARRAY_BUFFER, data, DYNAMIC);
		VBOManager.unbindVBO(ARRAY_BUFFER);
	}

	public void bufferData(FloatBuffer data) {
		vbo.bufferData(ARRAY_BUFFER, data, DYNAMIC);
		VBOManager.unbindVBO(ARRAY_BUFFER);
	}

	public void enableBuffer() {
		VBOManager.bindVBO(vbo, ARRAY_BUFFER);
		interleaving.enableStates();
	}

	public void disableBuffer() {
		interleaving.disableStates();
		VBOManager.unbindVBO(ARRAY_BUFFER);
	}

	@Override
	public String toString() {
		return vbo.toString();
	}

}
