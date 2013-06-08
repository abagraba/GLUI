package GLUIRenderer;

import GLUICore.RenderContainer;
import GLUICore.Renderable;

public class CenterPane extends RenderContainer {

	private Renderable current;

	@Override
	public void add(Renderable p) {
		if (current != null)
			super.remove(current);
		current = p;
		super.add(p);
	}

	@Override
	public void remove(Renderable p) {
		if (current == p)
			super.remove(current);
	}

	@Override
	public void validatePosition() {
		if (current != null) {
			current.resize(width, height);
			current.reposition((width - current.getWidth()) / 2, (height - current.getHeight()) / 2);
		}
	}

}
