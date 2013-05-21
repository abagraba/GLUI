package Renderer;


import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;

import GLUICore.RenderData;
import GLUICore.TextureManager;
import GLUICore.VBOManager;


public abstract class TileMap {

	protected int w, h;
	private TileMapTexture[] layers = null;
	private boolean[] activeLayers;
	
	public abstract RenderData[][] getRenderData(Rectangle clip, int renderlayerID);

	public TileMap(int w, int h, TileMapTexture[] layerTextures) {
		this.w = w;
		this.h = h;
		this.layers = layerTextures;
		activeLayers = new boolean[layers.length];
		for (int i = 0; i < activeLayers.length; i++)
			activeLayers[i] = true;
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public void render(Rectangle clip) {
		for (int i = 0; i < layers.length; i++) {
			if (activeLayers[i] && layers[i] != null) {
				TextureManager.bindTexture(layers[i].texture, GL11.GL_TEXTURE_2D, GL11.GL_NEAREST);
				RenderData[][] tiles = getRenderData(clip, i);
				float[] f = new float[clip.width * clip.height * 16];
				for (int x = 0; x < clip.width; x++)
					for (int y = 0; y < clip.height; y++) {
						RenderData d = tiles[x][y];
						if (d != null) {
							int off = (x + y * clip.width) * 16;
							fillVert(x, y, f, off);
							fillTex(layers[i], d.texID, d.rotation, f, off);
						}
					}
				VBOManager.createDynamicVBO(layers[i].texture + "~" + i, GL11.GL_QUADS, VBOManager.V2T2);
				VBOManager.DynamicDraw(layers[i].texture + "~" + i, f);
			}
		}
	}

	private void fillVert(int x, int y, float[] data, int off) {
		data[off + 0] = x;
		data[off + 1] = y;
		data[off + 4] = x;
		data[off + 5] = y + 1;
		data[off + 8] = x + 1;
		data[off + 9] = y + 1;
		data[off + 12] = x + 1;
		data[off + 13] = y;
	}

	private void fillTex(TileMapTexture tex, int id, int rotation, float[] data, int off) {
		int tx = id % tex.texWidth;
		int ty = id / tex.texWidth;
		for (int i = 0; i < 4; i++) {
			float rot = rotation + i;
			rot %= 4;
			rot += 4;
			rot %= 4;
			int x = 0;
			int y = 0;
			if (rot > 0 && rot < 3)
				y = 1;
			if (rot > 1)
				x = 1;
			data[off + i * 4 + 2] = (x + tx) / (float) tex.texWidth;
			data[off + i * 4 + 3] = (y + ty) / (float) tex.texHeight;
		}
	}

}
