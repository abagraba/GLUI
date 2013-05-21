package GLUICore;

import java.util.LinkedList;

public abstract class Pane extends EventHandler {

	// TODO rename to container?

	private LinkedList<Pane> contents = new LinkedList<Pane>();
	private Pane keyFocus = null;

	protected int x, y, width, height;

	public Pane(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void add(Pane p) {
		contents.add(p);
	}

	public void remove(Pane p) {
		contents.remove(p);
	}

	public void resize(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		validatePosition();
	}

	private Pane getHit(int x, int y) {
		Pane hit = null;
		for (Pane p : contents)
			if (p.inBounds(x - p.x, y - p.y))
				hit = p;
		return hit;
	}

	protected final void handleMouseEvent(MouseEvent me) {
		if (me.pressed)
			mousePressed(me);
		else
			mouseReleased(me);
		Pane hit = getHit(me.getX(), me.getY());
		if (!me.isConsumed() && hit != null) {
			if (me.pressed)
				keyFocus = hit;
			if (keyFocus != null)
				keyFocus.handleMouseEvent(me.offset(hit.x, hit.y));
		}
	}

	protected final void handleMouseMoveEvent(MouseMoveEvent mme) {
		// Handle event
		boolean prevIn = inBounds(mme.getPrevX(), mme.getPrevY());
		boolean currIn = inBounds(mme.getCurrentX(), mme.getCurrentY());
		if (prevIn && !currIn)
			mouseExited(mme);
		if (!prevIn && currIn)
			mouseEntered(mme);
		// Pass event to children.
		if (!mme.isConsumed()) {
			Pane prev = getHit(mme.getPrevX(), mme.getPrevY());
			Pane next = getHit(mme.getCurrentX(), mme.getCurrentY());
			if (prev != next)
				if (prev != null) {
					prev.handleMouseMoveEvent(mme.offset(prev.x, prev.y));
					mme.offset(-prev.x, -prev.y);
				}
			if (next != null) {
				next.handleMouseMoveEvent(mme.offset(next.x, next.y));
				mme.offset(-next.x, -next.y);
			}
		}
	}

	protected final void handleMouseScrollEvent(MouseScrollEvent mse) {
		mouseScrolled(mse);
		if (!mse.isConsumed()) {
			Pane p = getHit(mse.getX(), mse.getY());
			if (p != null)
				p.handleMouseScrollEvent(mse.offset(p.x, p.y));
		}
	}

	protected final void handleKeyEvent(KeyEvent ke) {
		if (ke.pressed)
			keyPressed(ke);
		else
			keyReleased(ke);
		if (!ke.isConsumed() && keyFocus != null)
			keyFocus.handleKeyEvent(ke);
	}

	public abstract void validatePosition();

	protected void prerender() {
		if (width > 0 && height > 0) {
			render();
			for (Pane p : contents)
				p.prerender();
		}
	}

	public void render() {}

	protected boolean inBounds(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}

}
