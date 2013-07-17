package FFt;

import org.lwjgl.openal.AL10;

import Util.BufferUtil;
import Util.ByteUtil;

public class OutputComponent extends Component {

	final int source;
	private final int buffer;

	public OutputComponent() {
		super(1, 0, 0);
		source = AL10.alGenSources();
		buffer = AL10.alGenBuffers();
		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			System.out.println("OpenAL Error: Failure to generate source.");

	}

	@Override
	public void filter() {
		byte[] b = new byte[ComponentControl.getBufferSize()];
		float[] f = getInput(0);
		for (int i = 0; i < b.length; i++)
			b[i] = ByteUtil.getAudioByte(f[i]);
		AL10.alBufferData(buffer, AL10.AL_FORMAT_MONO8, BufferUtil.asDirectByteBuffer(b), 44100);
		AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
		AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source, AL10.AL_GAIN, 1.0f);
	}

}
