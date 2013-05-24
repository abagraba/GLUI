package Renderer;

import java.util.Collection;

public abstract class TSL {

	private boolean enabled = true;
	
	
	protected Collection<T> getRenderData(){
		if (enabled)
			return tileData();
		return null;
	}
	
	public abstract Collection<T> tileData();

	public void enable(boolean enable){
		enabled = enable;
	}
	
}
