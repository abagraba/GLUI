package Testing;

import java.awt.Rectangle;

import GLUICore.RenderData;
import Renderer.TileMap;
import Renderer.TileMapTexture;

public class Map extends TileMap {

	private static final TileMapTexture[] textures = new TileMapTexture[] {new TileMapTexture("Test", 1, 1)};

	public Map(int w, int h) {
		super(w, h, textures);
	}

	@Override
	public RenderData[][] getRenderData(Rectangle clip, int renderlayerID) {
		RenderData[][] res = new RenderData[w][h];
		for (int x = 0; x < res.length; x++)
			for (int y = 0; y < res[0].length; y++)
				res[x][y] = new RenderData(0, (x % 2 == 0 ? 0 : 2) + ((x % 2) + (y % 2) == 1 ? 2 : 1));
		return res;
	}
}
