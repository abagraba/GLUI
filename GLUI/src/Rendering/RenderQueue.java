package Rendering;

import java.util.HashMap;
import java.util.LinkedList;

public class RenderQueue {

	private static final HashMap<String, RenderQueue> queues = new HashMap<String, RenderQueue>();

	private final LinkedList<Renderable> renderables = new LinkedList<Renderable>();
	private final String name;

	public RenderQueue(String name) {
		this.name = name;
	}

	public void addToQueue(Renderable renderable) {
		renderables.add(renderable);
	}

	public void render() {
		for (Renderable r : renderables)
			r.render();
	}

	public void clearQueue() {
		renderables.clear();
	}

	public void destroy() {
		clearQueue();
		queues.remove(name);
	}

}
