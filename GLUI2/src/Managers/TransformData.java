package Managers;

import static Util.GLCONST.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL33;

import Rendering.VBO;
import Rendering.VBOManager;

public class TransformData {
	private final VBO vbo;

	/**
	 * Specifies a name to be bound to the index buffer that is being created.
	 * @param name name to be associated with the resultin index buffer.
	 */
	public TransformData(String name) {
		VBO vbo = VBOManager.getVBO(name);
		if (vbo == null)
			vbo = VBOManager.createVBO(name, FLOAT);
		this.vbo = vbo;
	}

	/**
	 * Specifies an empty int VBO to be used as the index buffer.
	 * @param vbo an empty int VBO.
	 */
	public TransformData(VBO vbo) {
		this.vbo = vbo;
	}

	public void bufferData(float[] data) {
		vbo.bufferData(ARRAY_BUFFER, data, DYNAMIC);
		VBOManager.unbindVBO(ARRAY_BUFFER);
	}

	public void enableData() {
		VBOManager.bindVBO(vbo, ELEMENT_ARRAY_BUFFER);
		int pos = ShaderManager.instanceProgram.getAttribute("position");
		int rot = ShaderManager.instanceProgram.getAttribute("rotation");
		GL20.glVertexAttribPointer(pos, 3, GL11.GL_FLOAT, false, 28, 0);
		GL20.glVertexAttribPointer(rot, 4, GL11.GL_FLOAT, false, 28, 12);
		GL20.glEnableVertexAttribArray(pos);
		GL20.glEnableVertexAttribArray(rot);
		GL33.glVertexAttribDivisor(pos, 1);
		GL33.glVertexAttribDivisor(rot, 1);
	}

	public void disableData() {
		int pos = ShaderManager.instanceProgram.getAttribute("position");
		int rot = ShaderManager.instanceProgram.getAttribute("rotation");
		GL20.glDisableVertexAttribArray(pos);
		GL20.glDisableVertexAttribArray(rot);
		VBOManager.unbindVBO(ELEMENT_ARRAY_BUFFER);
	}

	@Override
	public String toString() {
		return vbo.name;
	}

}
