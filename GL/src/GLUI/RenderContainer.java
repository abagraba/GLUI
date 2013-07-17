package GLui;

import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public abstract class RenderContainer extends Renderable {

	private final LinkedList<Renderable> contents = new LinkedList<Renderable>();
	private Renderable focus = null;

	/**
	 * Adds a Pane to the contents of this Pane.
	 * @param pane Pane to be added.
	 */
	public void add(Renderable pane) {
		contents.add(pane);
		focus = pane;
	}

	/**
	 * Removes a Pane from the contents of this Pane
	 * @param pane to be removed.
	 */
	public void remove(Renderable pane) {
		contents.remove(pane);
		if (focus == pane)
			focus = null;
	}

	private Renderable getHit(int x, int y) {
		Renderable hit = null;
		for (Renderable p : contents)
			if (p.inBounds(x - p.x, y - p.y))
				hit = p;
		return hit;
	}

	@Override
	public void repack() {
		validateContents();
		for (Renderable renderable : contents)
			renderable.repack();
	}

	@Override
	protected final void handleMouseEvent(MouseEvent me) {
		super.handleMouseEvent(me);
		Renderable hit = getHit(me.getX(), me.getY());
		if (me.pressed)
			focus = hit;
		if (focus != null && !me.isConsumed())
			focus.handleMouseEvent(me.offset(hit.x, hit.y));
	}

	@Override
	protected final void handleMouseMoveEvent(MouseMoveEvent mme) {
		super.handleMouseMoveEvent(mme);
		if (!mme.isConsumed()) {
			Renderable prev = getHit(mme.getPrevX(), mme.getPrevY());
			Renderable next = getHit(mme.getCurrentX(), mme.getCurrentY());
			if (prev != null && prev != next)
				prev.handleMouseMoveEvent(mme.newOffset(-prev.x, -prev.y));
			if (next != null)
				next.handleMouseMoveEvent(mme.offset(-next.x, -next.y));
			// NOTE: If the clone created by prev is consumed, next is passed a new MouseMoveEvent that is not consumed.
			// This may create problems if consuption is assumed to stop creation of new events.
		}
	}

	@Override
	protected final void handleMouseScrollEvent(MouseScrollEvent mse) {
		super.handleMouseScrollEvent(mse);
		if (!mse.isConsumed() && focus != null)
			focus.handleMouseScrollEvent(mse.offset(focus.x, focus.y));
	}

	@Override
	protected final void handleKeyEvent(KeyEvent ke) {
		super.handleKeyEvent(ke);
		if (!ke.isConsumed() && focus != null)
			focus.handleKeyEvent(ke);
	}

	private void setMatrix() {
		if (ortho == null || ortho.right != width || ortho.bottom != height)
			ortho = new OrthoView(0, width, 0, height, 0, 1);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(ortho.left, ortho.right, ortho.bottom, ortho.top, ortho.front, ortho.back);
	}

	@Override
	protected void prerender(int xOff, int yOff) {
		if (width > 0 && height > 0) {
			setViewport(xOff + x, yOff + y);
			setMatrix();
			render();
			for (Renderable p : contents)
				p.prerender(xOff + x, yOff + y);
		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		// FIXME Testing
		if (ke.key == Keyboard.KEY_ESCAPE)
			Display.destroy();
	}

}
