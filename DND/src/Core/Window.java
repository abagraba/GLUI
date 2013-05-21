package Core;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

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
		tmr2 = new MapView(new Map(15, 50) {
			public int random(int x, int y) {
				if (x == 0 || y == 0 || x == w - 1 || y == h - 1)
					return 0;
				Random r = new Random(x * x * 15400 + y * y * y * 50);
				return r.nextBoolean() ? 1 : 0;
			}
		});
		tmr.zoom(Mouse.getDWheel());
		tmr2.zoom(Mouse.getDWheel());

		FixedSplitPane fsp = new FixedSplitPane(false, true, 500);
		FixedSplitPane charSheet = new FixedSplitPane(true, false, 300);
		fsp.setFirst(tmr);
		fsp.setSecond(charSheet);

		charSheet.setSecond(tmr2);
		
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
