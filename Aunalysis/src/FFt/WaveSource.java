package FFt;

public class WaveSource extends Component {

	int samplesRead = 0;
	int freq = 400;

	public WaveSource() {
		super(0, 1, 0);
	}

	@Override
	public void filter() {
		float[] data = new float[ComponentControl.getBufferSize()];
		for (int i = 0; i < data.length; i++)
			data[i] = (float) Math.sin(i * 2 * Math.PI * freq / 44100);
		setOutput(0, data);
	}
}
