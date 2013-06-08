package Tileset;

import java.awt.Dimension;
import java.util.LinkedList;

import GLUICore.TextureManager;

public class TSM extends TSR {

	private final int w = 10, h = 10;

	private final LinkedList<TSL> layers = new LinkedList<TSL>();

	@Override
	public void render() {
		TextureManager.setRoot(root);
		for (TSL layer : layers)
			renderTiles(layer.getRenderData());
	}

	@Override
	public void onResize(int w, int h) {
		int tileWidth = width / resolution;
		int tileHeight = height / resolution;
		maximumSize = new Dimension(Math.min(this.w, tileWidth) * resolution, Math.min(this.h, tileHeight) * resolution);
		super.resize(w, h);
	}

	public void addLayer(TSL layer) {
		layers.add(layer);
	}

	public void removeLayer(TSL layer) {
		layers.remove(layer);
	}

}
