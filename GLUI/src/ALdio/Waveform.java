package ALdio;

import org.lwjgl.openal.AL10;

import GLui.Debug;

public class Waveform {

	public final int waveform;

	public Waveform(AudioData ad) {
		waveform = AL10.alGenBuffers();
		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			Debug.log("OpenAL Error: Failure to generate waveform.");
		AL10.alBufferData(waveform, ad.getFormat(), ad.getData(), ad.getSampleRate());
	}

}
