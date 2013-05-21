package GLUICore;

public class MouseMoveEvent extends Event {
	private int dx, dy, cx, cy;

	public MouseMoveEvent(int dx, int dy, int cx, int cy) {
		this.dx = dx;
		this.dy = dy;
		this.cx = cx;
		this.cy = cy;
	}

	public int getPrevX() {
		return cx - dx;
	}

	public int getDeltaX() {
		return dx;
	}

	public int getCurrentX() {
		return cx;
	}

	public int getPrevY() {
		return cy - dy;
	}

	public int getDeltaY() {
		return dy;
	}

	public int getCurrentY() {
		return cy;
	}

	protected MouseMoveEvent offset(int x, int y) {
		dx -= x;
		dy -= y;
		cx -= x;
		cy -= y;
		return this;
	}
}
