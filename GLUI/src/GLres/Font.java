package GLres;

import java.util.HashMap;

import GLcomponent.Drawable;
import GLcomponent.StaticDrawable;
import GLcomponent.Texture;
import GLcomponent.URect;

public class Font {

	public final String name;
	protected final int letterWidth;
	protected final int letterHeight;
	private final HashMap<Byte, Integer> kerning;
	protected final Texture texture;

	// TODO add TextFormat support.
	public Font(String name, Texture t, int w, int h, HashMap<Byte, Integer> kerning) {
		this.name = name;
		texture = t;
		letterWidth = w;
		letterHeight = h;
		this.kerning = kerning;
	}

	public Drawable getString(String s, int x, int y, int size) {
		return getString(s.getBytes(), x, y, size);
	}

	public Drawable getString(byte[] bytes, int x, int y, int size) {
		if (bytes == null)
			return null;
		float[] data = new float[16 * bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			URect vert = URect.vrect().translate(i, 0).scale(letterWidth * size / letterHeight, size).translate(x, y);
			URect tex = texture.getTile(getID(bytes[i]));
			vert.fillArray(data, 4, 16 * i);
			tex.fillArray(data, 4, 16 * i + 2);
			if (kerning.containsKey(bytes[i]))
				x += kerning.get(bytes[i]);
		}
		return new StaticDrawable(texture, data);
	}

	public Drawable getString(char[] c, int x, int y, int size) {
		if (c == null)
			return null;
		float[] data = new float[16 * c.length];
		for (int i = 0; i < c.length; i++) {
			URect vert = URect.vrect().translate(i, 0).scale(letterWidth * size / letterHeight, size).translate(x, y);
			URect tex = texture.getTile(getID((byte) c[i]));
			vert.fillArray(data, 4, 16 * i);
			tex.fillArray(data, 4, 16 * i + 2);
			if (kerning.containsKey((byte) c[i]))
				x += kerning.get((byte) c[i]);
		}
		return new StaticDrawable(texture, data);
	}

	public Drawable getWrappedString(String s, int x, int y, int size, int maxWidth, int spacing) {
		return getWrappedString(s.getBytes(), x, y, size, maxWidth, spacing);
	}

	public Drawable getWrappedString(byte[] bytes, int x, int y, int size, int maxWidth, int spacing) {
		if (bytes == null)
			return null;
		float[] data = new float[16 * bytes.length];
		int xOff = 0;
		int yOff = 0;
		int kern = 0;
		for (int i = 0; i < bytes.length; i++) {
			URect vert = URect.vrect().translate(xOff, 0).scale(letterWidth * size / letterHeight, size)
					.translate(x + kern, y + yOff);
			URect tex = texture.getTile(getID(bytes[i]));
			vert.fillArray(data, 4, 16 * i);
			tex.fillArray(data, 4, 16 * i + 2);
			if (kerning.containsKey(bytes[i]))
				kern += kerning.get(bytes[i]);
			xOff++;
			if ((xOff + 1) * letterWidth - kern >= maxWidth) {
				xOff = kern = 0;
				yOff += letterHeight + spacing;
			}

		}
		return new StaticDrawable(texture, data);
	}

	public Drawable getWrappedString(char[] c, int x, int y, int size, int maxWidth, int spacing) {
		if (c == null)
			return null;
		float[] data = new float[16 * c.length];
		int xOff = 0;
		int yOff = 0;
		int kern = 0;
		for (int i = 0; i < c.length; i++) {
			URect vert = URect.vrect().translate(xOff, 0).scale(letterWidth * size / letterHeight, size)
					.translate(x + kern, y + yOff);
			URect tex = texture.getTile(getID((byte) c[i]));
			vert.fillArray(data, 4, 16 * i);
			tex.fillArray(data, 4, 16 * i + 2);
			if (kerning.containsKey((byte) c[i]))
				kern += kerning.get((byte) c[i]);
			xOff++;
			if ((xOff + 1) * letterWidth - kern >= maxWidth) {
				xOff = kern = 0;
				yOff += letterHeight + spacing;
			}

		}
		return new StaticDrawable(texture, data);
	}

	public int getWidth(String s, int size) {
		int w = s.length() * letterWidth * size / letterHeight;
		byte[] bytes = s.getBytes();
		for (byte b : bytes)
			if (kerning.containsKey(b))
				w += kerning.get(b);
		return w;
	}

	protected static byte getID(byte c) {
		byte b = (byte) (c - ' ');
		if (b < 0)
			return 0x5F;
		if (b > 0x5F)
			return 0x5F;
		return b;
	}

	protected int getSpaceWidth() {
		return letterWidth + getKern(' ');
	}

	protected int getKern(byte b) {
		if (!kerning.containsKey(b))
			return 0;
		return kerning.get(b);
	}

	protected int getKern(char c) {
		return getKern((byte) c);
	}

}
