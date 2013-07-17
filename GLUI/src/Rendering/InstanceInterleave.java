package Rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL33;

import Managers.ShaderManager;

public enum InstanceInterleave {

	P(3, 0, 0),
	R(0, 4, 0),
	S(0, 0, 3),
	PR(3, 4, 0),
	PS(3, 0, 3),
	RS(0, 4, 3),
	PRS(3, 4, 3);

	public final int pos;
	public final int rot;
	public final int sca;
	public final int length;

	private InstanceInterleave(int pos, int rot, int sca) {
		this.pos = pos;
		this.rot = rot;
		this.sca = sca;
		length = pos + rot + sca;
	}

	public void initInstanceVBO() {
		int pOff = 0;
		int rOff = pos * 4;
		int sOff = rOff + rot * 4;
		int stride = sOff + sca * 4;

		if (pos > 0) {
			int posLoc = ShaderManager.instanceProgram.getAttribute("position");
			GL20.glVertexAttribPointer(posLoc, pos, GL11.GL_FLOAT, false, stride, pOff);
			GL20.glEnableVertexAttribArray(posLoc);
			GL33.glVertexAttribDivisor(posLoc, 1);
		}
		if (rot > 0) {
			int rotLoc = ShaderManager.instanceProgram.getAttribute("rotation");
			GL20.glVertexAttribPointer(rotLoc, rot, GL11.GL_FLOAT, false, stride, rOff);
			GL20.glEnableVertexAttribArray(rotLoc);
			GL33.glVertexAttribDivisor(rotLoc, 1);
		}
		if (sca > 0) {
			int scaLoc = ShaderManager.instanceProgram.getAttribute("scale");
			GL20.glVertexAttribPointer(scaLoc, sca, GL11.GL_FLOAT, false, stride, sOff);
			GL20.glEnableVertexAttribArray(scaLoc);
			GL33.glVertexAttribDivisor(scaLoc, 1);
		}
	}

}
