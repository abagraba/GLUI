package ALdio;

import java.nio.ByteBuffer;

import org.lwjgl.openal.AL10;

public abstract class AudioData {

	public final int sampleRate;
	public final float duration;
	public final int format;

	public AudioData(float duration) {
		this(duration, 44100);
	}

	public AudioData(float duration, int sampleRate) {
		this(duration, sampleRate, AL10.AL_FORMAT_MONO8);
	}

	public AudioData(float duration, int sampleRate, int format) {
		this.duration = duration;
		this.sampleRate = sampleRate;
		this.format = format;
	}

	public int getFormat() {
		return format;
	}

	public abstract ByteBuffer getData();

	public int getSampleRate() {
		return sampleRate;
	}

}
