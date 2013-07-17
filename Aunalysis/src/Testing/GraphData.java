package Testing;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import GLcomponent.Drawable;
import GLui.InterleavedVBO;

public class GraphData extends Drawable {

	private final float xMin, xMax, yMin, yMax;
	private final Color lineColor, gridColor, backColor;

	int width, height;

	private final float xStep = 1;

	private float[] graphData = new float[0];

	public GraphData(int w, int h, int xMin, int xMax, int yMin, int yMax) {
		this(w, h, xMin, xMax, yMin, yMax, Color.black);
	}

	public GraphData(int w, int h, int xMin, int xMax, int yMin, int yMax, Color lineColor) {
		this(w, h, xMin, xMax, yMin, yMax, lineColor, Color.black);
	}

	public GraphData(int w, int h, int xMin, int xMax, int yMin, int yMax, Color lineColor, Color gridColor) {
		this(w, h, xMin, xMax, yMin, yMax, lineColor, gridColor, Color.white);
	}

	public GraphData(int w, int h, int xMin, int xMax, int yMin, int yMax, Color lineColor, Color gridColor, Color backColor) {
		super(GL11.GL_LINE, InterleavedVBO.V2C3, null);
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.lineColor = lineColor;
		this.gridColor = gridColor;
		this.backColor = backColor;
		width = w;
		height = h;
	}

	@Override
	public int numVertices() {
		return 16 + 2 * (graphData.length > 0 ? graphData.length - 1 : 0);
	}

	public void graph(float[] data) {
		graphData = data;
	}

	@Override
	public float[] vertexData() {
		float lr = lineColor.getRed() / 255.0f;
		float lg = lineColor.getGreen() / 255.0f;
		float lb = lineColor.getBlue() / 255.0f;
		float br = backColor.getRed() / 255.0f;
		float bg = backColor.getGreen() / 255.0f;
		float bb = backColor.getBlue() / 255.0f;
		float[] f = new float[numVertices() * 5];

		float dx = xMax - xMin;
		float dy = yMax - yMin;
		float idx = 1.0f / dx;
		float idy = 1.0f / dy;

		float dxStep = xStep / dx;

		drawBorders(f);

		float pointPrev = 0;
		float pointCur = 0;
		if (graphData.length >= 1)
			pointCur = (graphData[0] - yMin) * idy;
		for (int i = 1; i < graphData.length; i++) {
			pointPrev = pointCur;
			pointCur = (graphData[i] - yMin) * idy;
			f[(i + 16) * 5 + 0] = (i - 1) * dxStep;
			f[(i + 16) * 5 + 1] = pointPrev;
			f[(i + 16) * 5 + 2] = lr;
			f[(i + 16) * 5 + 3] = lg;
			f[(i + 16) * 5 + 4] = lb;
			f[(i + 16) * 5 + 5] = i * dxStep;
			f[(i + 16) * 5 + 1] = pointCur;
			f[(i + 16) * 5 + 7] = lr;
			f[(i + 16) * 5 + 8] = lg;
			f[(i + 16) * 5 + 9] = lb;
		}
		return f;
	}

	private void drawBorders(float[] f) {
		float gr = gridColor.getRed() / 255.0f;
		float gg = gridColor.getGreen() / 255.0f;
		float gb = gridColor.getBlue() / 255.0f;
		f[0] = 0;
		f[1] = 0;
		f[2] = gr;
		f[3] = gg;
		f[4] = gb;
		f[5] = width;
		f[6] = 0;
		f[7] = gr;
		f[8] = gg;
		f[9] = gb;
		f[10] = width;
		f[11] = 0;
		f[12] = gr;
		f[13] = gg;
		f[14] = gb;
		f[15] = width;
		f[16] = height;
		f[17] = gr;
		f[18] = gg;
		f[19] = gb;
		f[20] = width;
		f[21] = height;
		f[22] = gr;
		f[23] = gg;
		f[24] = gb;
		f[25] = 0;
		f[26] = height;
		f[27] = gr;
		f[28] = gg;
		f[29] = gb;
		f[30] = 0;
		f[31] = height;
		f[32] = gr;
		f[33] = gg;
		f[34] = gb;
		f[35] = 0;
		f[36] = 0;
		f[37] = gr;
		f[38] = gg;
		f[39] = gb;
		float dx = xMax - xMin;
		float dy = yMax - yMin;
		float x0 = -xMin / dx * width;
		float y0 = yMax / dy * height;
		f[40] = x0;
		f[41] = 0;
		f[42] = gr;
		f[43] = gg;
		f[44] = gb;
		f[45] = x0;
		f[46] = height;
		f[47] = gr;
		f[48] = gg;
		f[49] = gb;
		f[50] = x0 - 1;
		f[51] = 0;
		f[52] = gr;
		f[53] = gg;
		f[54] = gb;
		f[55] = x0 - 1;
		f[56] = height;
		f[57] = gr;
		f[58] = gg;
		f[59] = gb;
		f[60] = 0;
		f[61] = y0;
		f[62] = gr;
		f[63] = gg;
		f[64] = gb;
		f[65] = width;
		f[66] = y0;
		f[67] = gr;
		f[68] = gg;
		f[69] = gb;
		f[70] = 0;
		f[71] = y0 - 1;
		f[72] = gr;
		f[73] = gg;
		f[74] = gb;
		f[75] = width;
		f[76] = y0 - 1;
		f[77] = gr;
		f[78] = gg;
		f[79] = gb;
	}
}
