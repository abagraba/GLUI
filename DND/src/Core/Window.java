package Core;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Mouse;

import GLUICore.FixedSplitPane;
import Renderer.TileMap;
import Renderer.TileMapRenderer;

public class Window extends GLUICore.Window {

	TileMap tm = new Map(30, 20);
	TileMapRenderer tmr;
	TileMapRenderer tmr2;

	public Window(int w, int h) {
		super(w, h);
		tmr = new MapView(tm);
		tmr2 = new MapView(tm);
		tmr.zoom(Mouse.getDWheel());
		tmr2.zoom(Mouse.getDWheel());
		
		//FixedSplitPane charSheet ;
		FixedSplitPane fsp = new FixedSplitPane(0, 0, w, h, false, true);
		fsp.setSplit(300);
		fsp.setFirst(tmr);
		fsp.setSecond(tmr2);
		
		setContentPane(fsp);
		
		glClearColor(0f, 0f, 0f, 1);
		
		repack();
		render();
	}

	public static void main(String[] argv) {
		new Window(1200, 800);
	}

	@Override
	public void glInit() {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

}
