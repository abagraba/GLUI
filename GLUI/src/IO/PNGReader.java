package IO;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class PNGReader {

	private static final byte[] signature = new byte[] {-119, 80, 78, 71, 13, 10, 26, 10};

	public static LinkedList<PNGChunk> readPNG(String filename) {
		Reader r = new Reader(filename);
		try {
			readSignature(r.readBytes(8));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		LinkedList<PNGChunk> chunks = new LinkedList<PNGChunk>();
		while (readChunk(r, chunks))
			;
		return chunks;
	}

	public static LinkedList<PNGChunk> readPNG(File file) {
		Reader r = new Reader(file);
		try {
			readSignature(r.readBytes(8));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		LinkedList<PNGChunk> chunks = new LinkedList<PNGChunk>();
		while (readChunk(r, chunks))
			;
		return chunks;
	}

	private static void readSignature(byte[] b) {
		for (int i = 0; i < 8; i++)
			if (b[i] != signature[i])
				throw new IllegalArgumentException("Not actually a PNG file");
	}

	private static boolean readChunk(Reader r, LinkedList<PNGChunk> chunks) {
		try {
			int size = r.readInt();
			String id = r.readString(4);
			byte[] b = r.readBytes(size);
			@SuppressWarnings("unused")
			int crc = r.readInt();
			chunks.add(new PNGChunk(id, b));
			return !id.equals("IEND");
		}
		catch (IOException e) {
			Util.Debug.log("PNGReader IOException.");
			return false;
		}
	}

}
