package Test;

import java.io.File;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import Managers.Program;
import Managers.ShaderManager;
import Managers.Texture;
import Managers.TextureManager;
import Util.GLCONST;
import Util.Vectorf3;

public class Test {

	public static Program mainshader;
	public static Camera camera;
	public static float moveSpeed = 0.1f;
	public static float rotSpeed = 0.03f;
	public static Vectorf3 initPos = new Vectorf3(0, 0, 0);
	public static boolean up = false, down = false, left = false, right = false, tleft = false, tright = false, tup = false,
			tdown = false;

	public static void init() {
		try {
			Display.create();
			Display.setDisplayMode(new DisplayMode(1300, 800));
		}
		catch (LWJGLException e) {
			System.exit(-1);
		}
		ShaderManager.createShader("Vert", new File("src/Test/Shader.vert"), GLCONST.SHADER_VERT);
		ShaderManager.createShader("Frag", new File("src/Test/Shader.frag"), GLCONST.SHADER_FRAG);
		ShaderManager.instanceProgram = ShaderManager.createProgram("Prog", "Vert", "Frag");
		Texture.createTexture("tex", new File("src/Test/terrain.png"), Texture.TEXTURE_2D, Texture.NEAREST);
		ShaderManager.useProgram("Prog");
		TextureManager.useTexture("tex");
		camera = new Camera(initPos, 0.01f, 100, 80);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void main(String[] args) {
		init();
		GL11.glClearColor(0, 0, 0.1f, 1.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		Mouse.getDX();
		Mouse.getDY();
		Mouse.getDWheel();
		Random r = new Random();

		Map m = new Map();
		Tile[][] tiles = new Tile[30][30];
		for (int x = 0; x < tiles.length; x++)
			for (int y = 0; y < tiles.length; y++)
				tiles[x][y] = new Tile(m, x, y, 0, r.nextBoolean());
		m.tiles = tiles;
		m.init();
		for (Tile[] tile : tiles)
			for (int x = 0; x < tiles.length; x++) {
				tile[x].updateCache();
				tile[x].see();
			}

		long t;
		while (!Display.isCloseRequested()) {
			t = System.nanoTime();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			camera();
			ShaderManager.useProgram("Prog");
			TextureManager.useTexture("tex");
			if (camera.phi > 0.8f)
				m.renderWall();
			else
				m.renderCut();
			Display.update();
			Display.sync(60);
			// System.out.println((System.nanoTime() - t) * 0.000001f + " ms");
			// System.out.println(1000000000f / (System.nanoTime() - t) + " fps");
		}

	}

	public static void camera() {

		while (Keyboard.next())
			switch (Keyboard.getEventKey()) {
				case Keyboard.KEY_LEFT:
				case Keyboard.KEY_A:
					left = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_RIGHT:
				case Keyboard.KEY_D:
					right = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_UP:
				case Keyboard.KEY_W:
					up = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_DOWN:
				case Keyboard.KEY_S:
					down = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_Q:
					tright = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_E:
					tleft = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_LCONTROL:
					tup = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_SPACE:
					tdown = Keyboard.getEventKeyState();
					break;
			}
		boolean x = left || right;
		boolean y = up || down;

		float mult = x && y ? 0.707f : 1;

		float speed = moveSpeed * mult;

		if (left)
			camera.moveRight(-speed);
		if (right)
			camera.moveRight(speed);
		if (up)
			camera.moveForward(speed);
		if (down)
			camera.moveForward(-speed);
		if (tleft)
			camera.orbitRight(-rotSpeed);
		if (tright)
			camera.orbitRight(rotSpeed);
		if (tup)
			camera.orbitUp(rotSpeed);
		if (tdown)
			camera.orbitUp(-rotSpeed);
		camera.zoom(-Mouse.getDWheel() / 240.0f);
		camera.updateCamera();
	}
}
