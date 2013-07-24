package Test;

import java.awt.Point;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import Rendering.InstanceData;
import Rendering.InstanceInterleave;
import Rendering.VBOIndexData;
import Rendering.VBOInterleave;
import Rendering.VBOVertexData;

public class Map {

	public Tile[][] tiles;

	protected TileEntity floorRenderer;
	protected TileEntity wallRenderer;

	private final InstanceData floorInstance = new InstanceData("FloorData", InstanceInterleave.PR1);
	private final InstanceData wallInstance = new InstanceData("WallData", InstanceInterleave.PR1);

	public static final Point[] ortho = new Point[] {new Point(0, 1), new Point(1, 0), new Point(0, -1), new Point(-1, 0)};

	public VBOVertexData[] vertices;

	VBOIndexData floor;
	VBOIndexData wall;
	VBOIndexData cutAway;

	protected void init() {

		vertices = new VBOVertexData[] {new VBOVertexData("TileDraw", new float[] {-0.5f, -0.5f, 0, 0, 1, 0.5f, -0.5f, 0,
																					0.5f, 1, 0.5f, 0.5f, 0, 0.5f, 0, -0.5f,
																					0.5f, 0, 0, 0, 0.5f, 0.5f, 0, 0.5f, 1,
																					-0.5f, 0.5f, 0, 1, 1, -0.5f, 0.5f, 1, 1,
																					0, 0.5f, 0.5f, 1, 0.5f, 0, -0.5f, 0.5f,
																					1, 1, 0, -0.5f, 0.5f, 0, 1, 1, 0.5f,
																					0.5f, 0, 0.5f, 1, 0.5f, 0.5f, 1, 0.5f,
																					0, 0.5f, 0.5f, 0, 0.5f, 1, -0.5f, 0.5f,
																					0, 1, 1, -0.5f, 0.5f, 0.5f, 1, 0, 0.5f,
																					0.5f, 0.5f, 0.5f, 0,

		}, VBOInterleave.V3T2)};
		floor = new VBOIndexData(0, 3);
		wall = new VBOIndexData(4, 11);
		cutAway = new VBOIndexData(8, 15);
		floorRenderer = new TileEntity(vertices, floor, new InstanceData[] {floorInstance}, GL11.GL_QUADS);
		wallRenderer = new TileEntity(vertices, wall, new InstanceData[] {wallInstance}, GL11.GL_QUADS);
	}

	public void renderWall() {
		wallRenderer.setIndices(wall);
		render();
	}

	public void renderCut() {
		wallRenderer.setIndices(cutAway);
		render();
	}

	private void render() {
		FloatBuffer f;
		f = BufferUtils.createFloatBuffer(floorRenderer.active.size() * 8);
		for (TileInstance floorInstance : floorRenderer.active)
			f.put(floorInstance.getInstanceData());
		f.flip();
		floorInstance.bufferData(f);
		f = BufferUtils.createFloatBuffer(wallRenderer.active.size() * 8);
		for (TileInstance wallInstance : wallRenderer.active)
			f.put(wallInstance.getInstanceData());
		f.flip();
		wallInstance.bufferData(f);
		floorRenderer.drawInstances();
		wallRenderer.drawInstances();
	}

	protected void setTile(int x, int y, Tile tile) {
		Tile t = tiles[x][y];
		if (t != null)
			t.destroy();
		tiles[x][y] = tile;
	}

	public boolean[] getNeighborPassable(Tile tile, Point[] directions) {
		boolean[] passable = new boolean[directions.length];
		for (int i = 0; i < directions.length; i++) {
			Tile t = getTile(tile.position.x + directions[i].x, tile.position.y + directions[i].y);
			if (t != null)
				passable[i] = t.getPassabillity();
		}
		return passable;
	}

	private Tile getTile(int x, int y) {
		try {
			return tiles[x][y];
		}
		catch (ArrayIndexOutOfBoundsException aioobe) {
			return null;
		}
	}

}
