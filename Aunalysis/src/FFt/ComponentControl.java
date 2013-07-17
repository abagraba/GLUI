package FFt;

import java.util.LinkedList;

import org.lwjgl.openal.AL10;

public final class ComponentControl {

	private static int bufferSize = 1024;
	private static int newBufferSize = 1024;
	private static final LinkedList<Component> components = new LinkedList<Component>();
	private static final LinkedList<OutputComponent> outputs = new LinkedList<OutputComponent>();

	public static void run() {
		if (getBufferSize() != newBufferSize) {
			bufferSize = newBufferSize;
			for (Component c : components)
				c.updateOutputBuffer();
		}
		for (OutputComponent output : outputs) {
			output.parse();
			AL10.alSourcePlay(output.source);
		}
	}

	public static int getBufferSize() {
		return bufferSize;
	}

	protected static void addComponent(Component component) {
		if (component instanceof OutputComponent)
			outputs.add((OutputComponent) component);
		components.add(component);
	}

}
