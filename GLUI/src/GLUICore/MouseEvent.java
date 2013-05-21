package GLUICore;


public class MouseEvent extends Event{
	public final int button;
	private int x, y;
	public final boolean pressed;
	
	public MouseEvent (int button, int x, int y, boolean pressed){
		this.button = button;
		this.x = x;
		this.y = y;
		this.pressed = pressed;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	protected MouseEvent offset(int x, int y){
		this.x -= x;
		this.y -= y;
		return this;
	}
}
