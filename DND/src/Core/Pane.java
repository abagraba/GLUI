package Core;

import org.lwjgl.opengl.GL11;

public abstract class Pane {
	
	protected int x = 0, y = 0, width = -1, height = -1;
	
	protected void prerender(){
		GL11.glScissor(x, y+height, width, height);
		render();
	}
	abstract void render();
	
}
