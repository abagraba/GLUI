package IO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PNGWriter {

	public static final int[] crcTable = new int[256];
	static {
		long c;
		for (int i = 0; i < 256; i++) {
			c = i;
			for (int k = 0; k < 8; k++)
				if ((c & 1) != 0)
					c = 0xedb88320 ^ c >> 1;
				else
					c = c >> 1;
			crcTable[i] = (int) c;
		}
	}
	FileOutputStream fos;
	private static final byte[] signature = new byte[] {-119, 80, 78, 71, 13, 10, 26, 10};

	public PNGWriter(String filename) {
		try {
			fos = new FileOutputStream(filename);
			fos.write(signature);
			fos.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PNGWriter(File file) {
		try {
			fos = new FileOutputStream(file);
			fos.write(signature);
			fos.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeChunk(PNGChunk chunk) {
		long crc = -1;
		try {
			fos.write(intToBytes(chunk.data.length));
			fos.write(chunk.name.getBytes());
			fos.write(chunk.data);
			crc = updateCrc(crc, intToBytes(chunk.data.length));
			crc = updateCrc(crc, chunk.name.getBytes());
			crc = updateCrc(crc, chunk.data);
			fos.write(intToBytes((int) crc));
			fos.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static long updateCrc(long crc, byte[] data) {
		for (byte element : data)
			crc = crcTable[(int) ((crc ^ element) & 0xff)] ^ crc >> 8;
		return crc;
	}

	private static byte[] intToBytes(int i) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (i >> 24 & 0xFF);
		bytes[1] = (byte) (i >> 16 & 0xFF);
		bytes[2] = (byte) (i >> 8 & 0xFF);
		bytes[3] = (byte) (i & 0xFF);
		return bytes;
	}
}
