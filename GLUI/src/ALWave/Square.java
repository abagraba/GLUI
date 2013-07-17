package ALWave;

import java.nio.ByteBuffer;

import ALdio.AudioData;
import Util.BufferUtil;
import Util.ByteUtil;

public class Square extends AudioData {

	public final int freq;

	public Square(int freq, int duration, int sampleRate) {
		super(freq, duration, sampleRate);
		this.freq = freq;
	}

	public Square(int freq, int duration) {
		super(freq, duration);
		this.freq = freq;
	}

	@Override
	public ByteBuffer getData() {
		byte[] b = new byte[(int) (sampleRate * duration)];
		for (int i = 0; i < b.length; i++) {
			float t = (float) i / sampleRate;
			b[i] = ByteUtil.getAudioByte(freq * t % 1 > 0.5f ? 1 : -1);
		}
		return BufferUtil.asDirectByteBuffer(b);
	}

}
