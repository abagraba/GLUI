package GLui;

import java.awt.Dimension;

import org.lwjgl.opengl.GL11;

import GLui.Event.ConsumedException;

public abstract class Renderable extends EventHandler {

	protected int x, y, width, height;

	OrthoView ortho;

	protected Dimension minimumSize;
	protected Dimension maximumSize;

	/**
	 * Resizes the Renderable. Clamps the values to remain within the maximum and minimum dimensions if they are
	 * specified.
	 * @param width the new width.
	 * @param height the new height.
	 */
	public final void resize(int width, int height) {
		onResize(width, height);
		if (maximumSize != null) {
			if (width > maximumSize.width)
				width = maximumSize.width;
			if (height > maximumSize.height)
				height = maximumSize.height;
		}
		if (minimumSize != null) {
			if (width < minimumSize.width)
				width = minimumSize.width;
			if (height < minimumSize.height)
				height = minimumSize.height;
		}
		this.width = width;
		this.height = height;
	}

	/**
	 * Repositions the Renderable relative to its {@link GLui.RenderContainer}.
	 * @param x new x Position.
	 * @param y new y Position.
	 */
	public final void reposition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public final void setMinimumSize(Dimension min) {
		minimumSize = min;
	}

	public final void setMinimumSize(int w, int h) {
		setMinimumSize(new Dimension(w, h));
	}

	public final void setMaximumSize(Dimension max) {
		maximumSize = max;
	}

	public final void setMaximumSize(int w, int h) {
		setMaximumSize(new Dimension(w, h));
	}

	/**
	 * Overridable method intended for implementations to use to determine how to render this object. If overriding an
	 * already defined implementation, include a call to super.render() first.
	 */
	public abstract void render();

	public void repack() {
		validateContents();
	}

	/**
	 * Overridable method intended for implementations to use to ensure validity of position. If invalid, the position
	 * and dimensions should be assigned valid values using {@link GLui.RenderContainer#reposition(int, int)} and
	 * {@link GLui.RenderContainer#resize(int, int)}. If overriding an already defined implementation, include a call to
	 * super.render() first.
	 */
	public abstract void validateContents();

	protected void prerender(int xOff, int yOff) {
		if (width > 0 && height > 0) {
			setViewport(xOff + x, yOff + y);
			setMatrix();
			render();
		}
	}

	private void setMatrix() {
		if (ortho == null || ortho.right != width || ortho.bottom != height)
			ortho = new OrthoView(0, width, 0, height, 0, 1);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(ortho.left, ortho.right, ortho.bottom, ortho.top, ortho.front, ortho.back);
	}

	protected boolean inBounds(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}

	protected void setViewport(int xOff, int yOff) {
		GL11.glViewport(xOff, yOff, width, height);
	}

	protected void handleMouseEvent(MouseEvent me) {
		if (me.isConsumed())
			throw new ConsumedException("MouseEvent already consumed.");
		if (me.pressed)
			mousePressed(me);
		else
			mouseReleased(me);
	}

	protected void handleMouseMoveEvent(MouseMoveEvent mme) {
		if (mme.isConsumed())
			throw new ConsumedException("MouseMoveEvent already consumed.");
		boolean prevIn = inBounds(mme.getPrevX(), mme.getPrevY());
		boolean currIn = inBounds(mme.getCurrentX(), mme.getCurrentY());
		if (prevIn && !currIn)
			mouseExited(mme);
		if (!prevIn && currIn)
			mouseEntered(mme);
	}

	protected void handleMouseScrollEvent(MouseScrollEvent mse) {
		if (mse.isConsumed())
			throw new ConsumedException("MouseScrollEvent already consumed.");
		mouseScrolled(mse);
	}

	protected void handleKeyEvent(KeyEvent ke) {
		if (ke.isConsumed())
			throw new ConsumedException("KeyEvent already consumed.");
		if (ke.pressed)
			keyPressed(ke);
		else
			keyReleased(ke);
	}

	@Override
	public String toString() {
		return getClass() + " X: " + x + " Y: " + y + " Width: " + width + " Height: " + height;
	}

}
