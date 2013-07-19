package Rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL33;

import Managers.ShaderManager;

public enum InstanceInterleave {

	P(3, 0, 0, 0),
	R(0, 4, 0, 0),
	S(0, 0, 3, 0),
	PR(3, 4, 0, 0),
	PS(3, 0, 3, 0),
	RS(0, 4, 3, 0),
	PRS(3, 4, 3, 0),
	P1(3, 0, 0, 1),
	R1(0, 4, 0, 1),
	S1(0, 0, 3, 1),
	PR1(3, 4, 0, 1),
	PS1(3, 0, 3, 1),
	RS1(0, 4, 3, 1),
	PRS1(3, 4, 3, 1),
	P2(3, 0, 0, 2),
	R2(0, 4, 0, 2),
	S2(0, 0, 3, 2),
	PR2(3, 4, 0, 2),
	PS2(3, 0, 3, 2),
	RS2(0, 4, 3, 2),
	PRS2(3, 4, 3, 2),
	P3(3, 0, 0, 3),
	R3(0, 4, 0, 3),
	S3(0, 0, 3, 3),
	PR3(3, 4, 0, 3),
	PS3(3, 0, 3, 3),
	RS3(0, 4, 3, 3),
	PRS3(3, 4, 3, 3),
	P4(3, 0, 0, 4),
	R4(0, 4, 0, 4),
	S4(0, 0, 3, 4),
	PR4(3, 4, 0, 4),
	PS4(3, 0, 3, 4),
	RS4(0, 4, 3, 4),
	PRS4(3, 4, 3, 4);

	public final int pos;
	public final int rot;
	public final int sca;
	public final int misc;
	public final int length;

	InstanceInterleave(int pos, int rot, int sca, int misc) {
		this.pos = pos;
		this.rot = rot;
		this.sca = sca;
		this.misc = misc;
		length = pos + rot + sca + misc;
	}

	public void initInstanceVBO() {
		int pOff = 0;
		int rOff = pos * 4;
		int sOff = rOff + rot * 4;
		int mOff = sOff + sca * 4;
		int stride = sOff + misc * 4;

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
		if (misc > 0) {
			int miscLoc = ShaderManager.instanceProgram.getAttribute("misc");
			GL20.glVertexAttribPointer(miscLoc, misc, GL11.GL_FLOAT, false, stride, mOff);
			GL20.glEnableVertexAttribArray(miscLoc);
			GL33.glVertexAttribDivisor(miscLoc, 1);
		}
	}

}
