package ALdio;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.Display;

import ALWave.Kick;

public class Testing {

	public static boolean run = true;
	public static boolean play = false;

	public static Waveform w;
	public static Source s;

	public static void main(String[] args) {
		try {
			Display.create();
			AL.create();
			w = new Waveform(new Kick(0.1f, 0.2f));
			s = new Source(w);
			while (!Display.isCloseRequested() && run) {
				while (Keyboard.next())
					if (Keyboard.getEventKeyState())
						parseKey(Keyboard.getEventKey());
				Display.update();
			}
		}
		catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	private static void parseKey(int key) {
		switch (key) {
			case Keyboard.KEY_ESCAPE:
				run = false;
				break;
			case Keyboard.KEY_SPACE:
				play = !play;
				if (play)
					AL10.alSourcePlay(s.source);
				else
					AL10.alSourcePause(s.source);
				break;
			default:
				break;
		}
	}
}
