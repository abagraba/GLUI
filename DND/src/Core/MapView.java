package Core;

import java.awt.Point;

import org.lwjgl.input.Keyboard;

import GLUICore.KeyEvent;
import GLUICore.MouseScrollEvent;
import Renderer.TileMap;
import Renderer.TileMapRenderer;

public class MapView extends TileMapRenderer {

	public MapView(int x, int y, int width, int height, TileMap tm) {
		super(x, y, width, height, tm);
	}

	public MapView(TileMap tm) {
		super(0, 0, 0, 0, tm);
	}
	
	public void keyPressed (KeyEvent ke){
		int dx = 0, dy = 0;
		switch (ke.key) {
			case Keyboard.KEY_LEFT:
				dx -= 1;
				break;
			case Keyboard.KEY_RIGHT:
				dx += 1;
				break;
			case Keyboard.KEY_DOWN:
				dy -= 1;
				break;
			case Keyboard.KEY_UP:
				dy += 1;
				break;
			default:
				System.out.println("\"" + ke.key + "\"");
		}
		move(new Point(dx, dy));
	}
	
	public void mouseScrolled(MouseScrollEvent mse){
		zoom(mse.delta/120, mse.getX(), mse.getY());
	}
	
}
