package Testing;

import static org.lwjgl.opengl.GL11.*;

import java.util.Collection;
import java.util.LinkedList;
import Renderer.T;
import Renderer.TSL;
import Renderer.TSR;

public class Window extends GLUICore.Window {

	public Window(int w, int h) {
		super(w, h);
		TSR tsr = new TSR();
		setContentPane(tsr);
		tsr.addLayer(new TSL() {

			@Override
			public Collection<T> tileData() {
				LinkedList<T> tiles = new LinkedList<T>();
				tiles.add(new T(0, 0, "Stone", 1));
				return tiles;
			}
		});

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
