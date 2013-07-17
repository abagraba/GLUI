package FFt;

public final class FFT {

	private static final double log2 = Math.log(2);

	public static final int NONE = -1;
	public static final int WINDOW_HANNING = 0;
	public static final int WINDOW_FLATTOP = 1;
	public static final int WINDOW_RESPONSE = 2;
	public static final int WINDOW_FORCE = 3;

	public static float[] transform(FloatStream fs, int sampleSize, int window, float[] windowArgs) {
		if (sampleSize > fs.size())
			sampleSize = fs.size();
		int twoPow = (int) (Math.log(sampleSize) / log2);
		sampleSize = (int) Math.pow(2, twoPow);
		float[] sample = fs.readBuffer(sampleSize);
	}
}
