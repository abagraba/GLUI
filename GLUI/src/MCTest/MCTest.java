package MCTest;

import java.io.File;
import java.util.LinkedList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import Managers.Program;
import Managers.ShaderManager;
import Managers.Texture;
import Util.Camera;
import Util.GLCONST;
import Util.Vectorf3;

public class MCTest {

	public static Program blockshader;
	public static Camera camera;
	public static float moveSpeed = 0.5f;
	public static float rotSpeed = 0.01f;
	public static Vectorf3 initPos = new Vectorf3(8, 0, -8);
	public static boolean up = false, down = false, left = false, right = false, rise = false, fall = false;

	public static void init() {
		try {
			Display.create();
			Display.setDisplayMode(new DisplayMode(1300, 800));
		}
		catch (LWJGLException e) {
			System.exit(-1);
		}
		ShaderManager.createShader("MCVert", new File("src/MCTest/MCShader.vert"), GLCONST.SHADER_VERT);
		ShaderManager.createShader("MCFrag", new File("src/MCTest/MCShader.frag"), GLCONST.SHADER_FRAG);
		blockshader = ShaderManager.createProgram("MCProg", "MCVert", "MCFrag");
		ShaderManager.instanceProgram = blockshader;
		Texture.createTexture("Blocks", new File("src/MCTest/terrain.png"), Texture.TEXTURE_2D, Texture.NEAREST);
		ChunkRenderer.init();
		camera = new Camera(initPos, 0.01f, 100, 80);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public static void main(String[] args) {
		init();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glOrtho(0, 16, 0, 64, -1, 16);
		World w = new World();
		Mouse.getDX();
		Mouse.getDY();
		LinkedList<Chunk> chunks = new LinkedList<Chunk>();
		for (int x = 0; x < 3; x++)
			for (int z = 0; z < 3; z++) {
				Chunk c = new Chunk(x, z, w);
				w.addChunk(c);
				chunks.add(c);
				c.generateRandomTerrain();
			}
		while (!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			camera();
			ChunkRenderer.renderChunk(chunks);
			// ChunkRenderer.renderTest();
			Display.update();
			Display.sync(60);
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
				case Keyboard.KEY_LCONTROL:
				case Keyboard.KEY_Q:
					fall = Keyboard.getEventKeyState();
					break;
				case Keyboard.KEY_SPACE:
				case Keyboard.KEY_E:
					rise = Keyboard.getEventKeyState();
					break;
			}
		boolean x = left || right;
		boolean y = rise || fall;
		boolean z = up || down;

		float mult = x && y && z ? 0.577f : x && (y || z) || y && z ? 0.707f : 1;

		float speed = moveSpeed * mult;

		if (left)
			camera.moveRight(-speed);
		if (right)
			camera.moveRight(speed);
		if (up)
			camera.moveForward(speed);
		if (down)
			camera.moveForward(-speed);
		if (rise)
			camera.moveUp(speed);
		if (fall)
			camera.moveUp(-speed);
		camera.turnRight(Mouse.getDX() * rotSpeed);
		camera.turnUp(Mouse.getDY() * rotSpeed);
		camera.updateMatrix();
	}
}
