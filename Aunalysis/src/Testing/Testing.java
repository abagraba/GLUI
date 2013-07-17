package Testing;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;

import FFt.ComponentChannel;
import FFt.ComponentControl;
import FFt.OutputComponent;
import FFt.WaveSource;

public class Testing {

	public static void main(String[] args) {
		try {
			AL.create();
		}
		catch (LWJGLException e) {
			e.printStackTrace();
		}
		OutputComponent oc = new OutputComponent();
		oc.inputFrom(0, new ComponentChannel(new WaveSource(), 0));
		while (true)
			ComponentControl.run();
	}
}
