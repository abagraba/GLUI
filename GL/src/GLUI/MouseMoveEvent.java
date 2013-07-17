package GLui;

import java.util.LinkedList;

/**
 * Event containing information about mouse movements.
 */
public class MouseMoveEvent extends Event {
	private final int dx, dy;
	private int cx, cy;
	private final LinkedList<MouseMoveEvent> clones;

	/**
	 * Construct the a MouseMoveEvent with the specified information.
	 * @param dx change in x position during the event.
	 * @param dy change in y position during the event.
	 * @param cx final x position after the event.
	 * @param cy final y position after the event.
	 */
	protected MouseMoveEvent(int dx, int dy, int cx, int cy) {
		this.dx = dx;
		this.dy = dy;
		this.cx = cx;
		this.cy = cy;
		clones = new LinkedList<MouseMoveEvent>();
		clones.add(this);
	}

	/**
	 * Construct a clone of this Event with the specified offset.
	 * @param original the MouseMoveEvent to be cloned.
	 * @param x x offset.
	 * @param y y offset.
	 */
	protected MouseMoveEvent(MouseMoveEvent original, int x, int y) {
		dx = original.dx;
		dy = original.dy;
		cx = original.cx + x;
		cy = original.cy + y;
		clones = original.clones;
		clones.add(this);
	}

	@Override
	public void consume() {
		for (MouseMoveEvent mme : clones)
			mme.consumed = true;
	}

	/**
	 * Returns the x coordinate of the mouse before the mouse movement.
	 * @return the previous x coordinate.
	 */
	public int getPrevX() {
		return cx - dx;
	}

	/**
	 * Returns the change in x coordinate of the mouse during the mouse movement.
	 * @return the change in x coordinate.
	 */
	public int getDeltaX() {
		return dx;
	}

	/**
	 * Returns the x coordinate of the mouse after the mouse movement.
	 * @return the current x coordinate.
	 */
	public int getCurrentX() {
		return cx;
	}

	/**
	 * Returns the y coordinate of the mouse before the mouse movement.
	 * @return the previous y coordinate.
	 */
	public int getPrevY() {
		return cy - dy;
	}

	/**
	 * Returns the change in y coordinate of the mouse during the mouse movement.
	 * @return the change in y coordinate.
	 */
	public int getDeltaY() {
		return dy;
	}

	/**
	 * Returns the y coordinate of the mouse after the mouse movement.
	 * @return the current y coordinate.
	 */
	public int getCurrentY() {
		return cy;
	}

	/**
	 * Offsets this event's position by a specified amount.
	 * @param x amount of x offset.
	 * @param y amount of y offset.
	 * @return this event after the offset.
	 */
	protected MouseMoveEvent offset(int x, int y) {
		cx += x;
		cy += y;
		return this;
	}

	/**
	 * Returns a clone of this event offset by a specified amount. Consuming this event will cause all clones to be
	 * consumed and vice versa.
	 * @param x amount of x offset.
	 * @param y amount of y offset.
	 * @return the offset clone of this event.
	 */
	protected MouseMoveEvent newOffset(int x, int y) {
		return new MouseMoveEvent(this, x, y);
	}
}
