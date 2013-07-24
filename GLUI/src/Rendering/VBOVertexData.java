package Rendering;

import static Util.GLCONST.VBO_ARRAY_BUFFER;
import static Util.GLCONST.VBO_DYNAMIC;
import static Util.GLCONST.TYPE_FLOAT;

import java.nio.FloatBuffer;

import Util.Debug;
import Util.GLCONST;

public class VBOVertexData {

	private final VBO vbo;
	private final VBOInterleave interleaving;

	/**
	 * Creates an float VBO with the DYNAMIC hint for use with vertex data. Buffers the provided data.
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
		this.vbo = VBOManager.createVBO(name, TYPE_FLOAT);
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
		this.vbo = VBOManager.createStaticVBO(name, data, GLCONST.VBO_ARRAY_BUFFER);
	}

	public VBOVertexData(VBO vbo, VBOInterleave interleaving) {
		this.vbo = vbo;
		this.interleaving = interleaving;
	}

	public void bufferData(float[] data) {
		vbo.bufferData(VBO_ARRAY_BUFFER, data, VBO_DYNAMIC);
		VBOManager.unbindVBO(VBO_ARRAY_BUFFER);
	}

	public void bufferData(FloatBuffer data) {
		vbo.bufferData(VBO_ARRAY_BUFFER, data, VBO_DYNAMIC);
		VBOManager.unbindVBO(VBO_ARRAY_BUFFER);
	}

	public void enableBuffer() {
		VBOManager.bindVBO(vbo, VBO_ARRAY_BUFFER);
		interleaving.enableStates();
	}

	public void disableBuffer() {
		interleaving.disableStates();
		VBOManager.unbindVBO(VBO_ARRAY_BUFFER);
	}

	@Override
	public String toString() {
		return vbo.toString();
	}

}
