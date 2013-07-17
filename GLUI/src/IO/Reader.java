package IO;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import Util.ByteUtil;

public class Reader {

	private FileInputStream inStream;

	public Reader(String filename) {
		File f = new File(filename);
		try {
			inStream = new FileInputStream(f);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Reader(File f) {
		try {
			inStream = new FileInputStream(f);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int readInt() throws IOException {
		return ByteUtil.bytesToInt(readBytes(4));
	}

	public String readString(int size) throws IOException {
		return ByteUtil.bytesToString(readBytes(size));
	}

	public byte readByte() throws IOException {
		return readBytes(1)[0];
	}

	public byte[] readBytes(int size) throws IOException {
		byte[] b = new byte[size];
		int bytesRead = 0;
		while (bytesRead < size) {
			int read = inStream.read(b, bytesRead, size - bytesRead);
			if (read == -1)
				throw new EOFException();
			bytesRead += read;
		}
		return b;
	}

	public void skipBytes(int numBytes) throws IOException {
		inStream.skip(numBytes);
	}
}
