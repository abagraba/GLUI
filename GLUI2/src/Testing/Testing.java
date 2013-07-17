package Testing;

import static Util.GLCONST.SHADER_FRAG;
import static Util.GLCONST.SHADER_VERT;

import java.io.File;
import java.util.LinkedList;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import Managers.IndexData;
import Managers.Interleaving;
import Managers.ShaderManager;
import Managers.Texture;
import Managers.TextureManager;
import Managers.VertexData;
import Rendering.Instance;
import Rendering.InstantiableStaticEntity;
import Rendering.RenderQueue;
import Util.Quaternionf;
import Util.Vectorf3;

public class Testing {

	public static int d = 10;
	public static float sx = 1.5f;
	public static float sy = 1.5f;
	public static float wh = 1;

	public static void initDisplay() {
		try {
			Display.create();
			Display.setDisplayMode(new DisplayMode(1300, 800));
		}
		catch (LWJGLException e) {
			e.printStackTrace();
		}
		wh = (float) Display.getWidth() / Display.getHeight();
		GL11.glOrtho(-0.5f * sx, d * wh * sx + 0.5f, -0.5f * sy, d * sy + 0.5f, -1, 1);
		ShaderManager.createShader("instance", new File("src/Shaders/Instance.vert"), SHADER_VERT);
		ShaderManager.createShader("vcolor", new File("src/Shaders/ShaderColor.vert"), SHADER_VERT);
		ShaderManager.createShader("vtexture", new File("src/Shaders/ShaderTexture.vert"), SHADER_VERT);
		ShaderManager.createShader("fcolor", new File("src/Shaders/FragColor.frag"), SHADER_FRAG);
		ShaderManager.createShader("ftexture", new File("src/Shaders/FragTexture.frag"), SHADER_FRAG);
		ShaderManager.createShader("vtesting", new File("src/Shaders/Testing.vert"), SHADER_VERT);
		ShaderManager.createShader("ftesting", new File("src/Shaders/Testing.frag"), SHADER_FRAG);
		ShaderManager.createProgram("Test", "instance", "vcolor", "fcolor", "vtesting", "ftesting", "vtexture", "ftexture");
		ShaderManager.setInstancingProgram(ShaderManager.getProgram("Test"));

		Texture.createTexture("Test", new File("src/Testing/kitty1.png"), Texture.TEXTURE_2D, Texture.NEAREST);
	}

	public static void main(String[] args) {
		initDisplay();

		VertexData[] vd = new VertexData[] {new VertexData("Testing", Interleaving.V2T2),
											new VertexData("Testingc", Interleaving.C3)};
		vd[0].bufferData(new float[] {-0.5f, -0.5f, 0, 1, 0.5f, -0.5f, 1, 1, 0.5f, 0.5f, 1, 0, -0.5f, 0.5f, 0, 0, 0, 0,
										0.5f, 0.5f});
		vd[1].bufferData(getData(1, 0, 0));

		IndexData id = new IndexData("OddQuad");
		id.bufferData(new int[] {4, 0, 1, 2, 3, 0});

		InstantiableStaticEntity ie = new InstantiableStaticEntity(vd, id, GL11.GL_TRIANGLE_FAN);
		LinkedList<Instance> instances = new LinkedList<Instance>();
		for (int x = 0; x < d * wh; x++)
			for (int y = 0; y < d; y++)
				instances.add(ie.createInstance(new Vectorf3(x * sx, y * sy, 0), Quaternionf.fromAxisAngle(Vectorf3.zAxis(),
						(x + y) % 4 * (float) (Math.PI / 2)), new Vectorf3(1.5f, 1, 1)));
		TextureManager.useTexture("Test");
		RenderQueue def = new RenderQueue("Default");

		def.addToQueue(ie);
		int color = 0;
		while (!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			float[] c = toRGB(color++);
			vd[1].bufferData(getData(c[0], c[1], c[2]));
			if (color % 1 == 0)
				for (Instance instance : instances)
					instance.rotateBy(Quaternionf.fromAxisAngle(new Vectorf3(0, 0, 1), (float) (Math.PI * 0.01)));
			def.render();
			Display.update();
			Display.sync(60);
		}
	}

	public static float[] getData(float r, float g, float b) {
		// return new float[] {-0.5f, -0.5f, 0, 0, 0, 0.5f, -0.5f, r, g, b, 0.5f, 0.5f, 0, 0, 0, -0.5f, 0.5f, r, g, b,
		// 0, 0, 1,
		// 1, 1};
		return new float[] {r, g, b, 0, 0, 0, r, g, b, 0, 0, 0, 1, 1, 1};
	}

	// h = [0, 360]
	public static float[] toRGB(float h) {
		float x = 1 - Math.abs(h / 60 % 2 - 1);
		float r = 0, g = 0, b = 0;
		switch ((int) (h / 60) % 6) {
			case 0:
				r = 1;
				g = x;
				break;
			case 1:
				r = x;
				g = 1;
				break;
			case 2:
				g = 1;
				b = x;
				break;
			case 3:
				g = x;
				b = 1;
				break;
			case 4:
				r = x;
				b = 1;
				break;
			case 5:
				r = 1;
				b = x;
				break;
		}
		return new float[] {r, g, b};
	}
}
