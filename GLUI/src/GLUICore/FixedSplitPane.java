package GLUICore;

import GLUICore.Pane;

public class FixedSplitPane extends Pane {

	private int split;
	boolean anchorTopLeft;
	boolean horizontal;
	Pane topleft;
	Pane botright;

	public FixedSplitPane(boolean anchorTopLeft, boolean horizontal) {
		this(anchorTopLeft, horizontal, Integer.MIN_VALUE);
	}

	public FixedSplitPane(boolean anchorTopLeft, boolean horizontal, int split) {
		this.split = split;
		this.anchorTopLeft = anchorTopLeft;
		this.horizontal = horizontal;
	}

	@Override
	@Deprecated
	public void add(Pane p) {
		System.out.println("Unused Method: Add");
	}

	@Override
	@Deprecated
	public void remove(Pane p) {
		System.out.println("Unused Method: Remove");
	}

	public void setFirst(Pane p) {
		if (topleft != null)
			super.remove(topleft);
		super.add(p);
		topleft = p;
	}

	public void setSecond(Pane p) {
		if (botright != null)
			super.remove(botright);
		super.add(p);
		botright = p;
	}

	public void setSplit(int split) {
		this.split = split;
	}

	@Override
	public void validatePosition() {
		if (split == Integer.MIN_VALUE)
			split = horizontal ? width / 2 : height / 2;
		if (split < 0)
			split = 0;
		if (split > (horizontal ? width : height))
			split = horizontal ? width : height;
		int splitFromAnchor = anchorTopLeft ? split : (horizontal ? width : height) - split;
		if (topleft != null)
			topleft.resize(0, 0, horizontal ? splitFromAnchor : width, horizontal ? height : splitFromAnchor);
		if (botright != null)
			botright.resize(horizontal ? splitFromAnchor : 0, horizontal ? 0 : splitFromAnchor, width
					- (horizontal ? splitFromAnchor : 0), height - (horizontal ? 0 : splitFromAnchor));
	}


}
