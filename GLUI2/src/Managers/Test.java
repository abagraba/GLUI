package Managers;

import static Util.GLCONST.*;

import java.io.File;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Rendering.InstantiableStaticEntity;

public class Test {

	public static void main(String[] args) {
		try {
			Display.create();
		}
		catch (LWJGLException e) {
			e.printStackTrace();
		}

		GL11.glOrtho(0, 500, 0, 500, -1, 1);
		ShaderManager.createShader("VERT", new File("src/Managers/InstanceShader.vert"), SHADER_VERT);
		ShaderManager.createShader("FRAG", new File("src/Managers/InstanceShader.frag"), SHADER_FRAG);
		ShaderManager.createProgram("Instance", "VERT", "FRAG");
		ShaderManager.setInstancingProgram(ShaderManager.getProgram("Instance"));

		float[] data = new float[] {0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1};
		VertexData vd = new VertexData("TestVertices", Interleaving.V2C3);
		vd.bufferData(data);
		IndexData id = new IndexData(0, 3);

		InstantiableStaticEntity obj = new InstantiableStaticEntity(new VertexData[] {vd}, id, GL11.GL_QUADS);
		for (int x = 0; x < 500; x++)
			for (int y = 0; y < 500; y++)
				obj.createInstance(new Vector3f(x, y, 0), new Vector4f(0, 0, 0, 1));

		while (!Display.isCloseRequested()) {
			long t = System.nanoTime();
			InstantiableStaticEntity.drawAllInstances();
			System.out.println((System.nanoTime() - t) * .000001f);
			Display.update();
		}
	}

}
