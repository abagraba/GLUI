package Testing;

import static Util.GLCONST.SHADER_FRAG;
import static Util.GLCONST.SHADER_VERT;

import java.io.File;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import Managers.ShaderManager;
import Managers.Texture;
import Managers.TextureManager;
import Rendering.Instance;
import Rendering.InstantiableStaticEntity;
import Rendering.RenderQueue;
import Rendering.VBOIndexData;
import Rendering.VBOInterleave;
import Rendering.VBOVertexData;
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

		VBOVertexData vert = new VBOVertexData("Testing", new float[] {-0.5f, -0.5f, 0, 1, 0.5f, -0.5f, 1, 1, 0.5f, 0.5f, 1,
																		0, -0.5f, 0.5f, 0, 0, 0, 0, 0.5f, 0.5f},
				VBOInterleave.V2T2);
		VBOVertexData col = new VBOVertexData("Testingc", VBOInterleave.C3);

		col.bufferData(getData(new float[] {1, 0, 0}));
		VBOIndexData id = new VBOIndexData("OddQuadIndex", new int[] {4, 0, 1, 2, 3, 0});

		InstantiableStaticEntity cat = new InstantiableStaticEntity(new VBOVertexData[] {vert, col}, id,
				GL11.GL_TRIANGLE_FAN);
		for (int x = 0; x < d * wh; x++)
			for (int y = 0; y < d; y++)
				cat.createInstance(new Vectorf3(x * sx, y * sy, 0), Quaternionf.fromAxisAngle(Vectorf3.zAxis(), (x + y) % 4
						* (float) (Math.PI / 2)), new Vectorf3(1.5f, 1, 1));
		TextureManager.useTexture("Test");
		RenderQueue queue = new RenderQueue("Main");

		queue.addToQueue(cat);
		int color = 0;
		while (!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			if (color % 1 == 0)
				for (Instance instance : cat.active)
					instance.rotateBy(Quaternionf.fromAxisAngle(new Vectorf3(0, 0, 1), (float) (Math.PI * 0.01)));
			col.bufferData(getData(toRGB(color++)));
			queue.render();
			Display.update();
			Display.sync(60);
		}
	}

	public static float[] getData(float[] c) {
		return new float[] {c[0], c[1], c[2], 0, 0, 0, c[0], c[1], c[2], 0, 0, 0, 1, 1, 1};
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
