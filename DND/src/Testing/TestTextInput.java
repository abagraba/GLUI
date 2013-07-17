package Testing;

import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import GLcomponent.Batcher;
import GLres.Font;
import GLres.ResourceManager;
import GLui.InterleavedVBO;
import GLui.KeyEvent;
import GLui.Renderable;

public class TestTextInput extends Renderable {

	LinkedList<Byte> baos = new LinkedList<Byte>();

	Font f1 = ResourceManager.getFont("Modern");
	Font f2 = ResourceManager.getFont("Glou");
	boolean kern = true;
	boolean shift = false;
	byte[] toRender;

	@Override
	public void render() {
		Font f = kern ? f1 : f2;
		Batcher b = new Batcher("Input", GL11.GL_QUADS, InterleavedVBO.V2T2);
		b.batch(f.getString(toRender, 0, (25 - (kern ? 16 : 12)) / 2, kern ? 16 : 12));
		b.renderBatch();
	}

	@Override
	public void validateContents() {

	}

	@Override
	public void keyPressed(KeyEvent ke) {
		super.keyPressed(ke);
		char c = 0x7F;
		switch (ke.key) {
			case Keyboard.KEY_F1:
				kern = !kern;
				break;
			case Keyboard.KEY_BACK:
				if (!baos.isEmpty())
					baos.removeLast();
				break;
			case Keyboard.KEY_LSHIFT:
			case Keyboard.KEY_RSHIFT:
				shift = true;
				return;
			case Keyboard.KEY_SPACE:
				c = ' ';
				break;
			case Keyboard.KEY_0:
				c = shift ? ')' : '0';
				break;
			case Keyboard.KEY_1:
				c = shift ? '!' : '1';
				break;
			case Keyboard.KEY_2:
				c = shift ? '@' : '2';
				break;
			case Keyboard.KEY_3:
				c = shift ? '#' : '3';
				break;
			case Keyboard.KEY_4:
				c = shift ? '$' : '4';
				break;
			case Keyboard.KEY_5:
				c = shift ? '%' : '5';
				break;
			case Keyboard.KEY_6:
				c = shift ? '^' : '6';
				break;
			case Keyboard.KEY_7:
				c = shift ? '&' : '7';
				break;
			case Keyboard.KEY_8:
				c = shift ? '*' : '8';
				break;
			case Keyboard.KEY_9:
				c = shift ? '(' : '9';
				break;
			case Keyboard.KEY_Q:
				c = shift ? 'Q' : 'q';
				break;
			case Keyboard.KEY_W:
				c = shift ? 'W' : 'w';
				break;
			case Keyboard.KEY_E:
				c = shift ? 'E' : 'e';
				break;
			case Keyboard.KEY_R:
				c = shift ? 'R' : 'r';
				break;
			case Keyboard.KEY_T:
				c = shift ? 'T' : 't';
				break;
			case Keyboard.KEY_Y:
				c = shift ? 'Y' : 'y';
				break;
			case Keyboard.KEY_U:
				c = shift ? 'U' : 'u';
				break;
			case Keyboard.KEY_I:
				c = shift ? 'I' : 'i';
				break;
			case Keyboard.KEY_O:
				c = shift ? 'O' : 'o';
				break;
			case Keyboard.KEY_P:
				c = shift ? 'P' : 'p';
				break;
			case Keyboard.KEY_A:
				c = shift ? 'A' : 'a';
				break;
			case Keyboard.KEY_S:
				c = shift ? 'S' : 's';
				break;
			case Keyboard.KEY_D:
				c = shift ? 'D' : 'd';
				break;
			case Keyboard.KEY_F:
				c = shift ? 'F' : 'f';
				break;
			case Keyboard.KEY_G:
				c = shift ? 'G' : 'g';
				break;
			case Keyboard.KEY_H:
				c = shift ? 'H' : 'h';
				break;
			case Keyboard.KEY_J:
				c = shift ? 'J' : 'j';
				break;
			case Keyboard.KEY_K:
				c = shift ? 'K' : 'k';
				break;
			case Keyboard.KEY_L:
				c = shift ? 'L' : 'l';
				break;
			case Keyboard.KEY_Z:
				c = shift ? 'Z' : 'z';
				break;
			case Keyboard.KEY_X:
				c = shift ? 'X' : 'x';
				break;
			case Keyboard.KEY_C:
				c = shift ? 'C' : 'c';
				break;
			case Keyboard.KEY_V:
				c = shift ? 'V' : 'v';
				break;
			case Keyboard.KEY_B:
				c = shift ? 'B' : 'b';
				break;
			case Keyboard.KEY_N:
				c = shift ? 'N' : 'n';
				break;
			case Keyboard.KEY_M:
				c = shift ? 'M' : 'm';
				break;
			case Keyboard.KEY_SUBTRACT:
				c = shift ? '_' : '-';
				break;
			case Keyboard.KEY_EQUALS:
				c = shift ? '+' : '=';
				break;
			case Keyboard.KEY_LBRACKET:
				c = shift ? '{' : '[';
				break;
			case Keyboard.KEY_RBRACKET:
				c = shift ? '}' : ']';
				break;
			case Keyboard.KEY_SEMICOLON:
				c = shift ? ':' : ';';
				break;
			case Keyboard.KEY_APOSTROPHE:
				c = shift ? '"' : '\'';
				break;
			case Keyboard.KEY_COMMA:
				c = shift ? '<' : ',';
				break;
			case Keyboard.KEY_PERIOD:
				c = shift ? '>' : '.';
				break;
			case Keyboard.KEY_SLASH:
				c = shift ? '?' : '/';
				break;
			case Keyboard.KEY_BACKSLASH:
				c = shift ? '|' : '\\';
				break;
			case Keyboard.KEY_GRAVE:
				c = shift ? '~' : '`';
				break;
			default:
				return;
		}
		if (c != 0x7F)
			baos.add((byte) c);
		byte[] b = new byte[baos.size()];
		for (int i = 0; i < baos.size(); i++)
			b[i] = baos.get(i);
		toRender = b;
	}
}
