package Rendering;

import static org.lwjgl.opengl.GL11.*;

public enum VBOInterleave {

	V2(2, 0, 0, 0),
	V3(3, 0, 0, 0),
	C3(0, 3, 0, 0),
	T2(0, 0, 2, 0),
	T3(0, 0, 3, 0),
	N3(0, 0, 0, 3),
	//
	V2T2(2, 0, 2, 0),
	//
	V3T2(3, 0, 2, 0),
	V3T2N3(3, 0, 2, 3),
	//
	V2C3(2, 3, 0, 0),
	V2C4(2, 4, 0, 0),
	//
	V3C3(3, 3, 0, 0),
	V3C3N3(3, 3, 3, 0),
	V3C4(3, 4, 0, 0),
	V3C4N3(3, 4, 3, 0),
	//
	V2C3T2(2, 3, 2, 0),
	V2C4T2(2, 4, 2, 0),
	//
	V3C3T2(3, 3, 2, 0),
	V3C4T2(3, 4, 2, 0),
	V3C3T2N3(3, 3, 2, 3),
	V3C4T2N3(3, 4, 2, 3);

	public final int V;
	public final int C;
	public final int T;
	/**
	 * Number of components in the normal vector. Valid values are 0, 3. Defaults to 0 if invalid.
	 */
	public final int N;
	public final int length;

	private VBOInterleave(int V, int C, int T, int N) {
		this.V = V;
		this.C = C;
		this.T = T;
		if (N != 3)
			N = 0;
		this.N = N;
		length = V + C + T + N;
	}

	public void enableStates() {
		int vOff = 0;
		int cOff = V * 4;
		int tOff = cOff + C * 4;
		int nOff = tOff + T * 4;
		int stride = nOff + N * 4;

		if (V > 0) {
			glEnableClientState(GL_VERTEX_ARRAY);
			glVertexPointer(V, GL_FLOAT, stride, vOff);
		}
		if (C > 0) {
			glEnableClientState(GL_COLOR_ARRAY);
			glColorPointer(C, GL_FLOAT, stride, cOff);
		}
		if (T > 0) {
			glEnableClientState(GL_TEXTURE_COORD_ARRAY);
			glTexCoordPointer(T, GL_FLOAT, stride, tOff);
		}
		if (N > 0) {
			glEnableClientState(GL_NORMAL_ARRAY);
			glNormalPointer(GL_FLOAT, stride, nOff);
		}
	}

	public void disableStates() {
		if (V > 0)
			glDisableClientState(GL_VERTEX_ARRAY);
		if (C > 0)
			glDisableClientState(GL_COLOR_ARRAY);
		if (T > 0)
			glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		if (N > 0)
			glDisableClientState(GL_NORMAL_ARRAY);
	}

}
