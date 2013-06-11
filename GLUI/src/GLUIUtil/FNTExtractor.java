package GLUIUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import GLUIRenderer.Texture;
import GLUIRes.Font;
import GLUIRes.ResourceManager;

/**
 * A TEXExtractor extracts a LinkedList of Texture definitions embedded in a TEX file. A TEX file consists of a PNG file
 * with an embedded ptEx chunk. The ptEx chunk is specified as sequence of smaller chunks specified as:
 * 
 * <pre>
 * <code>
 * Size: 		4 bytes		>0	int	Specifies length of Texture name.
 * Name:		Size bytes		String 	Texture name.
 * Offset:	4 bytes			int	Offset of Texture tiles. 		
 * 
 * Size: 		4 bytes		-2	int			
 * Width:		4 bytes			int 	Number of tiles the Texture should be split into horizontally.
 * Height:	4 bytes			int	Number of tiles the Texture should be split into vertically.
 * 
 * Size: 		4 bytes		-3	int			
 * TypeData:	1 bytes			byte 	Contains information on the texture target and filter. Bits 3-5 contain filter information and bits 6-8 contain target data. Bits 1-2 should not be set.
 * 
 * Size:		4 bytes		-1	int 	Specifies end of Texture definitions.
 * </code>
 * </pre>
 */
public final class FNTExtractor {

	private static final byte[] signature = new byte[] {-119, 80, 78, 71, 13, 10, 26, 10};

	private FNTExtractor() {

	}

	/**
	 * Returns the Font definition stored in the provided FNT file.
	 * @param file FNT file containing definitions.
	 * @return Font definition. returns null if file is invalid.
	 */
	public static Font readFONT(File file) {
		if (!FileUtil.isType(file, "fnt"))
			return null;
		String name = file.getName().split("\\.")[0];
		Reader r = new Reader(file);
		try {
			checkSignature(r.readBytes(8));
		}
		catch (IOException e) {
			return null;
		}
		if (findFONT(r))
			return parseFONT(r, name, file.getPath());
		return null;
	}

	private static void checkSignature(byte[] b) {
		for (int i = 0; i < 8; i++)
			if (b[i] != signature[i])
				throw new IllegalArgumentException("Not actually a FNT file");
	}

	private static boolean findFONT(Reader r) {
		String id = null;
		try {
			while (!"IEND".equals(id)) {
				int size;
				size = r.readInt();
				id = r.readString(4);
				if ("foNt".equals(id))
					return true;
				// Skip chunk data
				r.skipBytes(size);
				// Skip crc
				r.skipBytes(4);
			}
		}
		catch (IOException e) {
			return false;
		}
		return false;
	}

	private static Font parseFONT(Reader r, String name, String filename) {
		HashMap<Byte, Integer> kerning = new HashMap<Byte, Integer>();
		int tw = 1;
		int th = 1;
		int w = 1;
		int h = 1;
		try {
			byte b;
			while ((b = r.readByte()) != -1) {
				int kern = r.readInt();
				kerning.put(b, kern);
			}
			tw = r.readInt();
			th = r.readInt();
			w = r.readInt();
			h = r.readInt();
		}
		catch (IOException eofe) {
			return null;
		}
		ResourceManager.addTexture(new Texture("FONT: " + name, filename, tw, th, 0,
				(byte) (Texture.TEXTURE_2D | Texture.NEAREST)));
		return new Font(name, ResourceManager.getTexture("FONT: " + name), w, h, kerning);
	}
}
