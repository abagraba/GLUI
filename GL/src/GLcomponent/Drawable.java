package GLcomponent;

import org.lwjgl.opengl.GL11;

import GLui.InterleavedVBO;

public abstract class Drawable {

	public final int renderType;
	public final InterleavedVBO interleaved;
	public final Texture texture;

	/*
	 * public Drawable(int renderType, InterleavedVBO interleaved, String texture) { this.renderType = renderType;
	 * this.interleaved = interleaved; this.texture = ResourceManager.getTexture(texture); }
	 * 
	 * public Drawable(InterleavedVBO interleaved, String texture) { this(GL11.GL_QUADS, interleaved, texture); }
	 * 
	 * public Drawable(String texture) { this(InterleavedVBO.V2T2, texture); }
	 */
	public Drawable(int renderType, InterleavedVBO interleaved, Texture texture) {
		this.renderType = renderType;
		this.interleaved = interleaved;
		this.texture = texture;
	}

	public Drawable(InterleavedVBO interleaved, Texture texture) {
		this(GL11.GL_QUADS, interleaved, texture);
	}

	public Drawable(Texture texture) {
		this(InterleavedVBO.V2T2, texture);
	}

	public abstract int numVertices();

	public abstract float[] vertexData();

	public void bufferData(float[] data, int offset) {
		float[] vert = vertexData();
		for (int i = 0; i < numVertices(); i++)
			data[offset + i] = vert[i];
	}

}
