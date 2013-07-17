package ALWave;

import java.nio.ByteBuffer;

import ALdio.AudioData;
import Util.BufferUtil;
import Util.ByteUtil;

public class Kick extends AudioData {

	public final float speed;
	public int base = 10;
	public int power = 1;
	public int minFreq = 20;
	public int maxFreq = 60;
	public int initFreq = 20;

	public Kick(float speed, float duration, int sampleRate) {
		super(duration, sampleRate);
		this.speed = speed;
	}

	public Kick(float speed, float duration) {
		super(duration);
		this.speed = speed;
	}

	@Override
	public ByteBuffer getData() {
		byte[] b = new byte[(int) (sampleRate * duration)];
		float delta = 1.0f / sampleRate;
		double phase = 0;
		float d = duration * 0.333333f;
		for (int i = 0; i < b.length; i++) {
			float t = i * delta;
			double freq = t < d ? initFreq + (maxFreq - initFreq) * Math.sin(t * Math.PI * 0.5 / d) : minFreq
					+ (maxFreq - minFreq) * Math.cos((t / d - 1) * Math.PI * 0.25);
			phase += freq * delta;
			b[i] = ByteUtil.getAudioByte(Math.sin(2 * Math.PI * (freq * t + phase)));
		}
		return BufferUtil.asDirectByteBuffer(b);
	}
}
