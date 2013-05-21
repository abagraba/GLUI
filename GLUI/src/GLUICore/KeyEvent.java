package GLUICore;

public class KeyEvent {
	
	public final int key;
	public final boolean pressed;
	private boolean consumed = false;
	
	public KeyEvent (int key, boolean pressed){
		this.key = key;
		this.pressed = pressed;
	}
	
	public boolean isConsumed(){
		return consumed;
	}
	
	public void consume(){
		consumed = true;
	}
}
