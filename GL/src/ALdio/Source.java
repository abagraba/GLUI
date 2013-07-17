package ALdio;

import org.lwjgl.openal.AL10;

import GLui.Debug;

public class Source {

	public final int source;

	public Source(Waveform wave) {
		source = AL10.alGenSources();
		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			Debug.log("OpenAL Error: Failure to generate source.");
		AL10.alSourcei(source, AL10.AL_BUFFER, wave.waveform);
		AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source, AL10.AL_GAIN, 1.0f);
	}

}
