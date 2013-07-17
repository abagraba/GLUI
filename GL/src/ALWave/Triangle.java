package ALWave;

import java.nio.ByteBuffer;

import ALdio.AudioData;
import Util.BufferUtil;
import Util.ByteUtil;

public class Triangle extends AudioData {

	public final int freq;

	public Triangle(int freq, int duration, int sampleRate) {
		super(freq, duration, sampleRate);
		this.freq = freq;
	}

	public Triangle(int freq, int duration) {
		super(freq, duration);
		this.freq = freq;
	}

	@Override
	public ByteBuffer getData() {
		byte[] b = new byte[(int) (sampleRate * duration)];
		for (int i = 0; i < b.length; i++) {
			float t = (float) i / sampleRate;
			float f = 4 * (freq * t % 1) - 1;
			b[i] = ByteUtil.getAudioByte(f > 1 ? 2 - f : f);
		}
		return BufferUtil.asDirectByteBuffer(b);
	}

}
