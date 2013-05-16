package Renderer;

public abstract class Pane {

	protected int x, y, width, height;

	public Pane(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void reposition(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		validatePosition();
	}

	public abstract void validatePosition();

	public abstract void render();

}
