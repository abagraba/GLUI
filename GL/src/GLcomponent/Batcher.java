package GLcomponent;

import java.util.HashMap;
import java.util.LinkedList;

import GLres.TextureManager;
import GLres.VBOManager;
import GLui.Debug;
import GLui.InterleavedVBO;

public class Batcher {

	private final HashMap<Integer, HashMap<Texture, LinkedList<Drawable>>> textureTable = new HashMap<Integer, HashMap<Texture, LinkedList<Drawable>>>();

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
		HashMap<Texture, LinkedList<Drawable>> batch = getTextureBatch(d.renderType);
		if (!batch.containsKey(d.texture))
			batch.put(d.texture, new LinkedList<Drawable>());
		batch.get(d.texture).add(d);
	}

	private HashMap<Texture, LinkedList<Drawable>> getTextureBatch(int type) {
		HashMap<Texture, LinkedList<Drawable>> batch = textureTable.get(type);
		if (batch == null)
			textureTable.put(type, batch = new HashMap<Texture, LinkedList<Drawable>>());
		return batch;
	}

	public void renderBatch() {
		long batchTime = 0;
		for (int type : textureTable.keySet())
			for (Texture texture : textureTable.get(type).keySet()) {
				String textureName = texture != null ? texture.name : "~";
				long init = System.nanoTime();
				LinkedList<Drawable> batch = textureTable.get(type).get(texture);
				VBOManager.createDynamicVBO(type + name + textureName, type, interleaved);
				TextureManager.useTexture(texture);
				init = System.nanoTime() - init;

				System.out.println(type);

				long get = System.nanoTime();
				float[] data = batchData(batch, interleaved);
				get = System.nanoTime() - get;

				System.out.println(data.length);

				long draw = System.nanoTime();
				VBOManager.dynamicDraw(type + name + textureName, data);
				draw = System.nanoTime() - draw;

				long textureBatchTime = init + get + draw;
				batchTime += textureBatchTime;

				if (Debug.profile[Debug.verboseBatchDataPass]) {
					Debug.profile("Drawing Batch Vertex Data", draw, 2, Debug.rendererMessage);
					Debug.profile("Getting Batch Vertex Data", get, 2, Debug.rendererMessage);
					Debug.profile("Initializing Texture Batch Data", init, 2, Debug.rendererMessage);
				}
				if (Debug.profile[Debug.verboseTexturePass])
					Debug.profile("Rendering Texture Batch: " + textureName, textureBatchTime, 1, Debug.rendererMessage);
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
