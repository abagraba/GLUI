package FFt;

public class FloatStream {

	private final float[] buffer;
	private int pos;

	/**
	 * Creates a FloatStream with a buffer of size bufferSize. A FloatStream of size 16 will hold up to the last 16
	 * floats it has passed.
	 * @param bufferSize size of the FloatStream buffer.
	 */
	public FloatStream(int bufferSize) {
		buffer = new float[bufferSize];
	}

	/**
	 * Writes the float array to the stream. If the data exceeds the buffer, only the last floats written will be
	 * remembered.
	 * @param data floats to be written to the stream.
	 */
	public void writeToStream(float[] data) {
		int i = 0;
		if (data.length > buffer.length)
			i = data.length - buffer.length;
		for (i = 0; i < data.length; i++)
			buffer[(pos + i) % data.length] = data[i];
		pos = (pos + data.length) % buffer.length;
	}

	/**
	 * Attempts to read size floats from the stream.
	 * @param size number of floats to read.
	 * @return float[] containing size floats. If size is greater than stream bufferSize, returns float[] containing
	 *         bufferSize floats.
	 */
	public float[] readBuffer(int size) {
		if (size > buffer.length)
			size = buffer.length;
		float[] data = new float[size];
		for (int i = 0; i < data.length; i++)
			data[i] = buffer[(pos + i) % buffer.length];
		return data;
	}

	public int size() {
		return buffer.length;
	}

}
