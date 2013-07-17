package GLui;

import GLui.Event;

public class MouseScrollEvent extends Event {

	public final int delta;
	private int x, y;

	public MouseScrollEvent(int delta, int x, int y) {
		this.delta = delta;
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public MouseScrollEvent offset(int x, int y){
		this.x -= x;
		this.y -= y;
		return this;
	}
}
