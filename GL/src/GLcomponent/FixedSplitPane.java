package GLcomponent;

import GLui.Debug;
import GLui.RenderContainer;
import GLui.Renderable;

public class FixedSplitPane extends RenderContainer {

	private int split;
	boolean anchorBotLeft;
	boolean horizontal;
	Renderable botleft;
	Renderable topright;

	public FixedSplitPane(boolean anchorBotLeft, boolean horizontal) {
		this(anchorBotLeft, horizontal, Integer.MIN_VALUE);
	}

	public FixedSplitPane(boolean anchorBotLeft, boolean horizontal, int split) {
		this.split = split;
		this.anchorBotLeft = anchorBotLeft;
		this.horizontal = horizontal;
	}

	@Override
	@Deprecated
	public void add(Renderable p) {
		Debug.log("FixedSplitPane: Deprecated Method: Add");
	}

	@Override
	@Deprecated
	public void remove(Renderable p) {
		Debug.log("FixedSplitPane: Deprecated Method: Remove");
	}

	public void setFirst(Renderable p) {
		if (botleft != null)
			super.remove(botleft);
		super.add(p);
		botleft = p;
	}

	public void setSecond(Renderable p) {
		if (topright != null)
			super.remove(topright);
		super.add(p);
		topright = p;
	}

	public void setSplit(int split) {
		this.split = split;
	}

	@Override
	public void validateContents() {
		if (split == Integer.MIN_VALUE)
			split = horizontal ? width / 2 : height / 2;
		if (split < 0)
			split = 0;
		if (split > (horizontal ? width : height))
			split = horizontal ? width : height;
		int splitFromAnchor = anchorBotLeft ? split : (horizontal ? width : height) - split;

		if (botleft != null) {
			botleft.reposition(0, 0);
			botleft.resize(horizontal ? splitFromAnchor : width, horizontal ? height : splitFromAnchor);
			botleft.repack();
		}
		if (topright != null) {
			topright.reposition(horizontal ? splitFromAnchor : 0, horizontal ? 0 : splitFromAnchor);
			topright.resize(width - (horizontal ? splitFromAnchor : 0), height - (horizontal ? 0 : splitFromAnchor));
			topright.repack();
		}
	}

	@Override
	public void render() {

	}

}
