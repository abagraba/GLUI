package Renderer;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Point;
import java.awt.Rectangle;

import GLUICore.Pane;
import GLUICore.ShaderManager;

public abstract class TileMapRenderer extends Pane {

	public TileMapRenderer(int x, int y, int width, int height, TileMap tm) {
		super(x, y, width, height);
		this.tm = tm;
		setZoom(4, 6);
	}

	protected Position viewPos = new Position();
	private TileMap tm;
	private int zoom;
	private int minZoom;
	private int maxZoom;

	public void render() {
		int tileWidth = width / tileSize(zoom);
		int tileHeight = height / tileSize(zoom);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, tileWidth, 0, tileHeight, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		ShaderManager.useDefault(ShaderManager.TEXTURE);

		tm.render(getClip());
	}

	/**
	 * Changes the map's view position.
	 **/
	public void move(Point dp) {
		viewPos.x += dp.x;
		viewPos.y += dp.y;
		validatePosition();
	}

	public void zoom(int dz) {
		zoom(dz, width / 2, height / 2);
	}

	/**
	 * Changes the map's tileSize(). Attempts to keep the tile under the mouse cursor in that position.
	 * @param dz change in tileSize().
	 * @param p point offset from origin indicating position of cursor in the viewport.
	 */
	public void zoom(int dz, int xP, int yP) {
		//FIXME zoom does not center properly.
		int oldZoom = zoom;
		zoom += dz;
		if (zoom < minZoom)
			zoom = minZoom;
		if (zoom > maxZoom)
			zoom = maxZoom;

		if (zoom == oldZoom)
			return;
		xP -= x;
		yP -= y;
		viewPos.x += xP / tileSize(oldZoom);
		viewPos.y += yP / tileSize(oldZoom);
		viewPos.x *= tileSize(zoom);
		viewPos.y *= tileSize(zoom);
		viewPos.x /= tileSize(oldZoom);
		viewPos.y /= tileSize(oldZoom);
		viewPos.x -= xP / tileSize(zoom);
		viewPos.y -= yP / tileSize(zoom);
		validatePosition();
	}

	/**
	 * Clamps map position to valid values. Should be called after any change to the map's viewing parameters or dimensions.
	 */
	public void validatePosition() {
		if (tm.w <= tileWidth())
			viewPos.x = (tm.w - tileWidth()) / 2.0f;
		if (tm.h <= tileHeight())
			viewPos.y = (tm.h - tileHeight()) / 2.0f;

		if (tm.w > tileWidth()) {
			if (viewPos.x < 0)
				viewPos.x = 0;
			if (viewPos.x > tm.w - tileWidth())
				viewPos.x = tm.w - tileWidth();
		}
		if (tm.h > tileHeight()) {
			if (viewPos.y < 0)
				viewPos.y = 0;
			if (viewPos.y > tm.h - tileHeight())
				viewPos.y = tm.h - tileHeight();
		}

		//Center the TileMap using a viewport offset.
		int dx = 0, dy = 0, dw, dh;
		dw = tileSize(zoom) * (width / tileSize(zoom)) - width;
		dh = tileSize(zoom) * (height / tileSize(zoom)) - height;
		if (viewPos.x < 0)
			dx = (int) (-viewPos.x*tileSize(zoom));
		else 
			dx = -dw/2;
		if (viewPos.y < 0)
			dy = (int) (-viewPos.y*tileSize(zoom));
		else
			dy = -dh/2;
		viewportOffset(dx, dy, dw, dh);
	}

	private Rectangle getClip() {
		int x = (int) viewPos.x;
		if (x < 0)
			x = 0;
		int y = (int) viewPos.y;
		if (y < 0)
			y = 0;
		return new Rectangle(x, y, Math.min(tileWidth(), tm.w), Math.min(tileHeight(), tm.h));
	}

	private int tileSize(int zoom) {
		return (int) Math.pow(2, zoom);
	}
	
	public int tileWidth() {
		return width / tileSize(zoom);
	}

	public int tileHeight() {
		return height / tileSize(zoom);
	}

	public void setZoom(int minZoom, int maxZoom) {
		this.minZoom = minZoom;
		this.maxZoom = maxZoom;
		if (zoom < minZoom)
			zoom = minZoom;
		if (zoom > maxZoom)
			zoom = maxZoom;
		validatePosition();
	}

	public class Position {
		float x, y;
	}

}
