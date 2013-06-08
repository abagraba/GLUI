package GLUIRenderer;

public class Square extends Drawable {

	public Square(String texture) {
		super(texture);
	}

	public URect vertices = URect.rect();
	public URect texes = URect.rect();

	public void translate(int x, int y) {
		vertices.translate(x, y);
	}

	public void scale(int x, int y) {
		vertices.scale(x, y);
	}

	@Override
	public int numVertices() {
		return 4;
	}

	@Override
	public float[] vertexData() {
		float[] data = new float[16];
		data[0] = vertices.x1;
		data[1] = vertices.y1;
		data[4] = vertices.x1;
		data[5] = vertices.y2;
		data[8] = vertices.x2;
		data[9] = vertices.y2;
		data[12] = vertices.x2;
		data[13] = vertices.y1;
		data[2] = texes.x1;
		data[3] = texes.y1;
		data[6] = texes.x1;
		data[7] = texes.y2;
		data[10] = texes.x2;
		data[11] = texes.y2;
		data[14] = texes.x2;
		data[15] = texes.y1;
		return data;
	}

}
