package Rendering;

import static Util.GLCONST.ARRAY_BUFFER;
import static Util.GLCONST.DYNAMIC;
import static Util.GLCONST.FLOAT;

import java.nio.FloatBuffer;

import Util.Debug;
import Util.GLCONST;

public class InstanceData {
	private final VBO vbo;
	private final InstanceInterleave interleaving;

	/**
	 * Creates an float VBO with the SYNAMIC hint for use with instance data.
	 * @param name name to be associated with the resulting instance buffer.
	 * @param interleaving {@link InstanceInterleave} that describes the layout of interleaved data.
	 */
	public InstanceData(String name, InstanceInterleave interleaving) {
		this.interleaving = interleaving;
		VBO vbo = VBOManager.getVBO(name);
		if (vbo != null) {
			Debug.log(Debug.VBO_MANAGER, "Failure to create dynamic instance data buffer. Name collision: [", name, "].");
			this.vbo = null;
			return;
		}
		vbo = VBOManager.createVBO(name, FLOAT);
		this.vbo = vbo;
	}

	/**
	 * Creates an float VBO with the STATIC hint for use with instance data. Buffers the provided data.
	 * @param name name to be associated with the resulting instance buffer.
	 * @param data data to be buffered.
	 * @param interleaving {@link InstanceInterleave} that describes the layout of interleaved data.
	 */
	public InstanceData(String name, float[] data, InstanceInterleave interleaving) {
		this.interleaving = interleaving;
		VBO vbo = VBOManager.getVBO(name);
		if (vbo != null) {
			Debug.log(Debug.VBO_MANAGER, "Failure to create static instance data buffer. Name collision: [", name, "].");
			this.vbo = null;
			return;
		}
		vbo = VBOManager.createStaticVBO(name, data, GLCONST.ARRAY_BUFFER);
		this.vbo = vbo;
	}

	// FIXME Check type
	/**
	 * Specifies an empty float VBO to be used as the index buffer.
	 * @param vbo an empty float VBO.
	 */
	public InstanceData(VBO vbo, InstanceInterleave interleaving) {
		this.interleaving = interleaving;
		this.vbo = vbo;
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
		interleaving.initInstanceVBO();
	}

	@Override
	public String toString() {
		return vbo.name;
	}

}
