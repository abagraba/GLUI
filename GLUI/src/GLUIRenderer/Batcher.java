package GLUIRenderer;

import java.util.HashMap;
import java.util.LinkedList;

import GLUI.Debug;
import GLUI.InterleavedVBO;
import GLUIRes.TextureManager;
import GLUIRes.VBOManager;

public class Batcher {

	private final HashMap<Texture, LinkedList<Drawable>> textureTable = new HashMap<Texture, LinkedList<Drawable>>();

	private final int type;
	private final InterleavedVBO interleaved;
	private final String name;

	public Batcher(String name, int type, InterleavedVBO interleaved) {
		this.name = name;
		this.type = type;
		this.interleaved = interleaved;
	}

	public void batch(Drawable d) {
		if (d == null)
			return;
		if (!textureTable.containsKey(d.texture))
			textureTable.put(d.texture, new LinkedList<Drawable>());
		textureTable.get(d.texture).add(d);
	}

	public void renderBatch() {
		long batchTime = 0;
		for (Texture texture : textureTable.keySet()) {
			long init = System.nanoTime();
			LinkedList<Drawable> batch = textureTable.get(texture);
			VBOManager.createDynamicVBO(name + texture.name, type, interleaved);
			TextureManager.useTexture(texture);
			init = System.nanoTime() - init;

			long get = System.nanoTime();
			float[] data = batchData(batch, interleaved);
			get = System.nanoTime() - get;

			long draw = System.nanoTime();
			VBOManager.dynamicDraw(name + texture.name, data);
			draw = System.nanoTime() - draw;

			long textureBatchTime = init + get + draw;
			batchTime += textureBatchTime;

			if (Debug.profile[Debug.verboseBatchDataPass]) {
				Debug.profile("Drawing Batch Vertex Data", draw, 2, Debug.rendererMessage);
				Debug.profile("Getting Batch Vertex Data", get, 2, Debug.rendererMessage);
				Debug.profile("Initializing Texture Batch Data", init, 2, Debug.rendererMessage);
			}
			if (Debug.profile[Debug.verboseTexturePass])
				Debug.profile("Rendering Texture Batch: " + texture.name, textureBatchTime, 1, Debug.rendererMessage);
		}
		if (Debug.profile[Debug.verboseTexturePass])
			Debug.profile("Batch Rendered: " + name, batchTime, 0, Debug.rendererMessage);
	}

	private static float[] batchData(LinkedList<Drawable> batch, InterleavedVBO interleaved) {
		int totalVertices = 0;
		for (Drawable drawable : batch)
			totalVertices += drawable.numVertices();
		float[] vert;
		if (batch.size() == 1)
			vert = batch.get(0).vertexData();
		else {
			vert = new float[totalVertices * interleaved.length];
			int verticesBuffered = 0;
			for (Drawable drawable : batch) {
				drawable.bufferData(vert, verticesBuffered * interleaved.length);
				verticesBuffered += drawable.numVertices();
			}
		}
		return vert;
	}

}
