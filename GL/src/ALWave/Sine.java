package ALWave;

import java.nio.ByteBuffer;

import ALdio.AudioData;
import Util.BufferUtil;
import Util.ByteUtil;

public class Sine extends AudioData {

	public final int freq;

	public Sine(int freq, int duration, int sampleRate) {
		super(duration, sampleRate);
		this.freq = freq;
	}

	public Sine(int freq, int duration) {
		super(duration);
		this.freq = freq;
	}

	@Override
	public ByteBuffer getData() {
		byte[] b = new byte[(int) (sampleRate * duration)];
		float w = (float) (2 * Math.PI * freq);
		for (int i = 0; i < b.length; i++) {
			float t = (float) i / sampleRate;
			// FIXME Optimize using sine table
			b[i] = ByteUtil.getAudioByte(Math.sin(w * t));
		}
		return BufferUtil.asDirectByteBuffer(b);
	}
}
