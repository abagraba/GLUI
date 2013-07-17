package IO;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import GLcomponent.Texture;
import Util.FileUtil;

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
public final class TEXExtractor {

	private static final byte[] signature = new byte[] {-119, 80, 78, 71, 13, 10, 26, 10};

	private TEXExtractor() {

	}

	/**
	 * Returns a list of Texture definitions stored in the provided TEX file.
	 * @param file TEX file containing definitions.
	 * @return Texture definitions. returns null if file is invalid.
	 */
	public static LinkedList<Texture> readPTEX(File file) {
		if (!FileUtil.isType(file, "tex"))
			return null;
		Reader r = new Reader(file);
		try {
			checkSignature(r.readBytes(8));
		}
		catch (IOException e) {
			return null;
		}
		if (findPTEX(r))
			return parsePTEX(r, file.getPath());
		return null;
	}

	private static void checkSignature(byte[] b) {
		for (int i = 0; i < 8; i++)
			if (b[i] != signature[i])
				throw new IllegalArgumentException("Not actually a TEX file");
	}

	private static boolean findPTEX(Reader r) {
		String id = null;
		try {
			while (!"IEND".equals(id)) {
				int size;
				size = r.readInt();
				id = r.readString(4);
				if ("ptEx".equals(id))
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

	private static LinkedList<Texture> parsePTEX(Reader r, String filename) {
		LinkedList<Texture> textures = new LinkedList<Texture>();
		int w = 1;
		int h = 1;
		byte typeData = Texture.TEXTURE_2D | Texture.NEAREST;
		int size;
		try {
			while ((size = r.readInt()) != -1)
				if (size == -2) {
					w = r.readInt();
					h = r.readInt();
				}
				else if (size == -3)
					typeData = r.readByte();
				else {
					String name = r.readString(size);
					int offset = r.readInt();
					textures.add(new Texture(name, filename, w, h, offset, typeData));
				}
		}
		catch (IOException eofe) {
			return null;
		}
		return textures;
	}
}
