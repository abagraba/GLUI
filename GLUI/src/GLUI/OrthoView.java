package GLui;

public class OrthoView {
	public final float left, right, top, bottom, back, front;

	public OrthoView(float left, float right, float top, float bottom, float front, float back) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.back = back;
		this.front = front;
	}
}
