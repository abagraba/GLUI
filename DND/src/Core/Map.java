package Core;

import java.awt.Rectangle;
import java.util.Random;

import Renderer.RenderData;
import Renderer.TileMap;
import Renderer.TileMapTexture;

public class Map extends TileMap {

	private static final TileMapTexture[] textures = new TileMapTexture[] {new TileMapTexture("Base", 2, 1),
																			new TileMapTexture("Wall", 5, 1),
																			new TileMapTexture("DWall", 5, 1),
																			new TileMapTexture("Entities", 5, 1)};

	public Map(int w, int h) {
		super(w, h, textures);
	}

	@Override
	public RenderData[][] getRenderData(Rectangle clip, int renderlayerID) {
		RenderData[][] res = new RenderData[clip.width][clip.height];
		for (int x = 0; x < clip.width; x++)
			for (int y = 0; y < clip.height; y++) {
				int xn = x + clip.x;
				int yn = y + clip.y;
				if (renderlayerID == 0)
					res[x][y] = new RenderData(random(xn, yn), (xn + yn) % 4, false);
				if (renderlayerID == 1 || renderlayerID == 2)
					res[x][y] = random(xn, yn) == 0 ? getWall(renderlayerID == 1 ? getNeighbors(xn, yn) : getDNeighbors(xn,
							yn)) : null;
				if (renderlayerID == 3)
					res[x][y] = random(xn, yn) == 0 ? getWall(getNeighbors(xn, yn)) : null;
			}
		return res;
	}

//	147a + 10b + 25c = 49d
//	147a + 10b + 16c = 42d
//	147a + 10b + 11c = 38d
//	c = 0.75
	
	private RenderData getWall(boolean[] n) {
		if (n[0] && n[2]) {
			if (n[1] && n[3])
				return new RenderData(4);
			if (n[1])
				return new RenderData(3, 0);
			if (n[3])
				return new RenderData(3, 2);
			return new RenderData(1);
		}
		if (n[1] && n[3]) {
			if (n[0])
				return new RenderData(3, 3);
			if (n[2])
				return new RenderData(3, 1);
			return new RenderData(1, 1);
		}
		if (n[0]) {
			if (n[1])
				return new RenderData(2);
			if (n[3])
				return new RenderData(2, 3);
			return new RenderData(0);
		}
		if (n[2]) {
			if (n[1])
				return new RenderData(2, 1);
			if (n[3])
				return new RenderData(2, 2);
			return new RenderData(0, 2);
		}
		if (n[1])
			return new RenderData(0, 1);
		if (n[3])
			return new RenderData(0, 3);
		return null;
	}

	private boolean[] getNeighbors(int x, int y) {
		boolean[] neighbors = new boolean[4];
		for (int i = 0; i < 4; i++) {
			int xx = x;
			int yy = y;
			if (i == 0)
				yy -= 1;
			if (i == 1)
				xx += 1;
			if (i == 2)
				yy += 1;
			if (i == 3)
				xx -= 1;
			if (xx < 0 || yy < 0 || xx >= w || yy >= h)
				neighbors[i] = random(x, y) == 1;
			else
				neighbors[i] = random(xx, yy) == 1;
		}
		return neighbors;
	}

	private boolean[] getDNeighbors(int x, int y) {
		boolean[] neighbors = getNeighbors(x, y);
		boolean[] dneighbors = new boolean[4];
		for (int i = 0; i < 4; i++) {
			int xx = x;
			int yy = y;
			if (i == 0) {
				xx++;
				yy--;
			}
			if (i == 1) {
				xx++;
				yy++;
			}
			if (i == 2) {
				xx--;
				yy++;
			}
			if (i == 3) {
				xx--;
				yy--;
			}
			if (xx < 0 || yy < 0 || xx >= w || yy >= h)
				dneighbors[i] = random(x, y) == 1;
			else
				dneighbors[i] = random(xx, yy) == 1;
		}
		for (int i = 0; i < 4; i++) {
			if (neighbors[i] || neighbors[(i + 1) % 4])
				dneighbors[i] = false;
		}
		return dneighbors;
	}

	public int random(int x, int y) {
		Random r = new Random(x * x * x * 1000 + y * y * 8000);
		return r.nextBoolean() ? 1 : 0;
		// return ((y + x * x) % (x + 1) + (x * y + y)) % 2;
	}

}
