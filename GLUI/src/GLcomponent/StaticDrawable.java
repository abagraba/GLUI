package GLcomponent;

import GLui.InterleavedVBO;

public class StaticDrawable extends Drawable {
	// FIXME Make this draw using a static VBO istead of dynamic.
	private final float[] data;

	public StaticDrawable(int renderType, InterleavedVBO interleaved, String texture, float[] data) {
		super(renderType, interleaved, texture);
		this.data = data;
	}

	public StaticDrawable(InterleavedVBO interleaved, String texture, float[] data) {
		super(interleaved, texture);
		this.data = data;
	}

	public StaticDrawable(String texture, float[] data) {
		super(texture);
		this.data = data;
	}

	public StaticDrawable(int renderType, InterleavedVBO interleaved, Texture texture, float[] data) {
		super(renderType, interleaved, texture);
		this.data = data;
	}

	public StaticDrawable(InterleavedVBO interleaved, Texture texture, float[] data) {
		super(interleaved, texture);
		this.data = data;
	}

	public StaticDrawable(Texture texture, float[] data) {
		super(texture);
		this.data = data;
	}

	@Override
	public int numVertices() {
		return data.length;
	}

	@Override
	public float[] vertexData() {
		return data;
	}

}
