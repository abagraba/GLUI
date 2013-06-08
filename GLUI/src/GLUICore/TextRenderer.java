package GLUICore;

import org.lwjgl.opengl.GL11;

import GLUIRenderer.URect;

public class TextRenderer {

	String font = "src/Testing/GlouNum.png";
	int letterWidth = 8;
	int letterHeight = 12;

	// FIXME add resource manager
	public static void renderNumber(int x, int y, int num) {
		URect[] digitTex = new URect[(int) Math.log10(num) + 1];
		for (int i = 0; i < digitTex.length; i++) {
			int digit = num / (int) Math.pow(10, digitTex.length - 1 - i) % 10;
			digitTex[i] = getDigitTex(digit);
		}
		float[] f = new float[digitTex.length * 16];
		for (int i = 0; i < digitTex.length; i++) {
			URect vert = URect.rect().translate(x + i * 0.875f, y).scale(8, 12);
			fillBuffer(vert, f, i * 16);
			fillBuffer(digitTex[i], f, i * 16 + 2);
		}
		VBOManager.createDynamicVBO("Number: " + num, GL11.GL_QUADS, VBOManager.V2T2);
		VBOManager.dynamicDraw("Number: " + num, f);
	}

	private static void fillBuffer(URect rect, float[] buffer, int offset) {
		buffer[offset] = rect.x1;
		buffer[offset + 1] = rect.y1;
		buffer[offset + 4] = rect.x1;
		buffer[offset + 5] = rect.y2;
		buffer[offset + 8] = rect.x2;
		buffer[offset + 9] = rect.y2;
		buffer[offset + 12] = rect.x2;
		buffer[offset + 13] = rect.y1;
	}

	private static URect getDigitTex(int digit) {
		return URect.rect().translate(digit, 0).scale(0.1f, 1);
	}

}
