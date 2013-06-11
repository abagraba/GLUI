package Testing;

import java.util.LinkedList;

import org.lwjgl.input.Keyboard;

import GLUI.KeyEvent;
import GLUIRenderer.CenterPane;
import GLUIRenderer.FixedSplitPane;

public class TestContentPane extends CenterPane {

	LinkedList<Byte> baos = new LinkedList<Byte>();
	TestTextInput txt = new TestTextInput();
	boolean shift = false;

	public TestContentPane() {
		FixedSplitPane r = new TestSplitPane();
		r.setMaximumSize(800, 420);
		add(r);

		r.setFirst(new TestImage());
		r.setSecond(txt);

	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.key == Keyboard.KEY_LSHIFT)
			shift = false;
		if (ke.key == Keyboard.KEY_RSHIFT)
			shift = false;
	}

}
