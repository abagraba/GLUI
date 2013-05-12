package Core;

import java.awt.Point;

public class Layer {
	protected final int width, height;
	protected Tile[][] tiles;
	
	protected Layer (int w, int h){
		width = w;
		height = h;
		tiles = new Tile[w][h];
	}

	public void render(Point viewPos, int tileSize) {
		
	}
	
	
}
