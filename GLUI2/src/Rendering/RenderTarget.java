package Rendering;

public abstract class RenderTarget {

	public abstract void init();

	protected abstract void passInit();

	protected abstract void passFinish();

}
