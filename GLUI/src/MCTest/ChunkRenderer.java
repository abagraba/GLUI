package MCTest;

import java.nio.FloatBuffer;
import java.util.Collection;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL31;

import Managers.ShaderManager;
import Managers.TextureManager;
import Rendering.InstanceData;
import Rendering.InstanceInterleave;
import Rendering.VBOIndexData;
import Rendering.VBOInterleave;
import Rendering.VBOVertexData;

public class ChunkRenderer {

	public static VBOVertexData block;

	public static InstanceData inst;

	public static VBOVertexData noTint;
	public static VBOVertexData tint;

	public static VBOIndexData yPass;
	public static VBOIndexData nyPass;
	public static VBOIndexData xPass;
	public static VBOIndexData nxPass;
	public static VBOIndexData zPass;
	public static VBOIndexData nzPass;

	public static VBOIndexData[] directionPasses;

	public static void init() {
		float[] blockData = new float[] {0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0,
											0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0,
											0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0,
											1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0,
											0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1};

		block = new VBOVertexData("Block", blockData, VBOInterleave.V3T2);

		inst = new InstanceData("Inst", InstanceInterleave.P1);

		noTint = new VBOVertexData("NoTint", new float[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
															1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
															1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
															1, 1, 1, 1, 1, 1, 1, 1}, VBOInterleave.C3);
		tint = new VBOVertexData("Tint", new float[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
														1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
														1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
														1, 1, 1}, VBOInterleave.C3);

		yPass = new VBOIndexData("ypass", new int[] {12, 13, 14, 15});
		nyPass = new VBOIndexData("nypass", new int[] {0, 1, 2, 3});
		xPass = new VBOIndexData("xpass", new int[] {20, 21, 22, 23});
		nxPass = new VBOIndexData("nxpass", new int[] {8, 9, 10, 11});
		zPass = new VBOIndexData("zpass", new int[] {16, 17, 18, 19});
		nzPass = new VBOIndexData("nzpass", new int[] {4, 5, 6, 7});

		directionPasses = new VBOIndexData[] {yPass, nyPass, nxPass, xPass, nzPass, zPass};
	}

	public static void renderChunk(Collection<Chunk> cs) {
		TextureManager.useTexture("Blocks");
		ShaderManager.instanceProgram.use();
		block.enableBuffer();
		inst.enableBuffer();
		tint.enableBuffer();
		for (int i = 0; i < directionPasses.length; i++) {
			directionPasses[i].enableBuffer();
			if (i == 1)
				noTint.enableBuffer();
			int instanceCount = 0;
			for (Chunk c : cs)
				for (int y = 0; y < 64; y++)
					for (int x = 0; x < 16; x++)
						for (int z = 0; z < 16; z++) {
							Block b = c.getBlock(x, y, z);
							if (b != null && b.visibility[i])
								instanceCount++;
						}
			FloatBuffer instanceData = BufferUtils.createFloatBuffer(instanceCount * 4);
			for (Chunk c : cs)
				for (int y = 0; y < 64; y++)
					for (int x = 0; x < 16; x++)
						for (int z = 0; z < 16; z++) {
							Block b = c.getBlock(x, y, z);
							if (b != null && b.visibility[i]) {
								instanceData.put(b.getPositionArray());
								instanceData.put(b.getTexID(i));
							}
						}
			instanceData.flip();
			inst.bufferData(instanceData);
			GL31.glDrawElementsInstanced(GL11.GL_QUADS, directionPasses[i].getSize(), GL11.GL_UNSIGNED_INT, 0, instanceCount);
		}
	}

	public static void renderTest() {
		TextureManager.useTexture("Blocks");
		block.enableBuffer();
		tint.enableBuffer();

		inst.enableBuffer();
		VBOIndexData index = new VBOIndexData(0, 23);
		index.enableBuffer();
		inst.bufferData(new float[] {0, 0, 0, 1, 0, 1, 0, 2, 0, 2, 0, 3});
		GL31.glDrawElementsInstanced(GL11.GL_QUADS, 24, GL11.GL_UNSIGNED_INT, 0, 3);
	}
}
