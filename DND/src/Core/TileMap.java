package Core;

import java.awt.Dimension;
import java.awt.Point;

import org.lwjgl.opengl.GL11;

public class TileMap extends Pane{

	Point viewPos;
	Dimension mapSize;
	Layer[] layers;
	int tileSize;

	
	public void render(){
		GL11.glOrtho(0, width, 0, height, -1, 1);
		for (int i =0; i<layers.length; i++){
			layers[i].render(viewPos, tileSize);
		}
	}
	
	/**
	 * Changes the map's view position.
	**/
	public void move(Point dp){
		viewPos.x += dp.x;
		viewPos.y += dp.y;
		validateView();
	}
	
	/**
	 * Changes the map's tilesize.
	 * @param dz change in tilesize.
	 * @param p point offset from origin indicating position of cursor.
	 */
	public void zoom(int dz, Point p) {
		int z = tileSize;
		tileSize += dz;
		viewPos.x += p.x;
		viewPos.y += p.y;
		viewPos.x *= tileSize;
		viewPos.y *= tileSize;
		viewPos.x /= z;
		viewPos.y /= z;
		viewPos.x -= p.x;
		viewPos.y -= p.y;
		validateView();
	}
	
	/**
	 * Clamps map position to valid values. Should be called after any change to the map's viewing parameters or dimensions.
	 */
	private void validateView() {
		int w = mapSize.width * tileSize;
		int h = mapSize.height * tileSize;
		if (width > w)
			viewPos.x = (width - w) / 2;
		else {
			if (viewPos.x < 0)
				viewPos.x = 0;
			if (viewPos.x > width - w)
				viewPos.x = width - w;
		}
		if (height > h)
			viewPos.y = (height - h) / 2;
		else {
			if (viewPos.y < 0)
				viewPos.y = 0;
			if (viewPos.y > height - h)
				viewPos.y = height - h;
		}
	}
}
