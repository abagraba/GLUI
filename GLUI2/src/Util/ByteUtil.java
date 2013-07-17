package Util;

/**
 * A Utility class with useful Byte manipulation methods.
 */
public class ByteUtil {

	private ByteUtil() {

	}

	/**
	 * Converts a String into a byte[].
	 * @param s String to be converted.
	 * @return byte[] representing s.
	 */
	public static byte[] toBytes(String s) {
		return s.getBytes();
	}

	/**
	 * Converts an int into a byte[].
	 * @param i int to be converted.
	 * @return byte[] representing i. Big endian.
	 */
	public static byte[] toBytes(int i) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (i >> 24 & 0xFF);
		bytes[1] = (byte) (i >> 16 & 0xFF);
		bytes[2] = (byte) (i >> 8 & 0xFF);
		bytes[3] = (byte) (i & 0xFF);
		return bytes;
	}

	/**
	 * Converts a byte[] into an int.
	 * @param b bytes to be converted.
	 * @return int represented by b. Returns -1 if the length of b is not 4.
	 */
	public static int bytesToInt(byte[] b) {
		if (b.length != 4)
			return -1;
		int x = 0;
		for (int i = 0; i < 4; i++) {
			x = x << 8;
			x += b[i] & 0xFF;
		}
		return x;
	}

	/**
	 * Converts a byte[] into a String.
	 * @param b bytes to be converted.
	 * @return String represented by b.
	 */
	public static String bytesToString(byte[] b) {
		return new String(b);
	}

	public static byte getAudioByte(float f) {
		if (f > 1)
			f = 1;
		if (f < -1)
			f = -1;
		return (byte) (127.5 * (1 + f));
	}

	public static byte getAudioByte(double d) {
		if (d > 1)
			d = 1;
		if (d < -1)
			d = -1;
		return (byte) (127.5 * (1 + d));
	}

}
