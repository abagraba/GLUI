package Renderer;

public class RenderData {

	public int texID;
	public int rotation;
	public boolean flip;
	
	public RenderData(int texID){
		this(texID, 0);
	}
	public RenderData(int texID, int rotation){
		this(texID, rotation, false);
	}
	public RenderData(int texID, int rotation, boolean flip){
		this.texID = texID;
		this.rotation = rotation;
		this.flip = flip;
	}
	
}
