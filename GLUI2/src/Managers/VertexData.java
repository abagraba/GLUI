package Managers;

import static Util.GLCONST.*;

public class VertexData {

	private final VBO vbo;
	private final Interleaving interleaving;

	public VertexData(String name, Interleaving interleaving) {
		vbo = VBOManager.createVBO(name, FLOAT);
		this.interleaving = interleaving;
	}

	public VertexData(VBO vbo, Interleaving interleaving) {
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
