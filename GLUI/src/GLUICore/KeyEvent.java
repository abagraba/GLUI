package GLUICore;

/**
 * A KeyEvent defines a Keyboard Event. It contains information on which key triggered the Event and whether the key was
 * pressed or released.
 */
public class KeyEvent extends Event {

	/**
	 * The key that triggered the KeyEvent.
	 */
	public final int key;
	/**
	 * Whether the key was pressed or released.
	 */
	public final boolean pressed;

	// FIXME add support for modifiers (SHIFT/CTRL/ALT)

	protected KeyEvent(int key, boolean pressed) {
		// FIXME convert Keyboard.Key to Key enum
		this.key = key;
		this.pressed = pressed;
	}

}