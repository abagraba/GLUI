package FFt;

public abstract class Component {

	private final ComponentChannel[] inputs;
	private final ComponentChannel[] outputs;
	private final ComponentChannel[] syncs;
	private float[][] outputBuffer;
	private final boolean bufferValid = false;
	private float[][] syncBuffer;
	private final boolean syncReady = false;

	public Component(int in, int out, int sync) {
		inputs = new ComponentChannel[in];
		outputs = new ComponentChannel[out];
		syncs = new ComponentChannel[sync];
		ComponentControl.addComponent(this);
		updateOutputBuffer();
	}

	protected void updateOutputBuffer() {
		outputBuffer = new float[outputs.length][ComponentControl.getBufferSize()];
		syncBuffer = new float[syncs.length][ComponentControl.getBufferSize()];

	}

	protected boolean parse() {
		for (ComponentChannel inputChannel : inputs) {
			if (inputChannel == null || inputChannel.component == null)
				continue;
			if (!inputChannel.component.bufferValid)
				inputChannel.component.parse();
		}
		filter();
		return true;
	}

	/*
	 * protected void requestSync() { syncReady = true; for (ComponentChannel syncChannel : syncs) { if (syncChannel ==
	 * null || syncChannel.component == null) continue; if (!syncChannel.component.syncReady)
	 * syncChannel.component.parse(); } }
	 */
	/**
	 * Takes valid input from channels and then populates outputBuffers.
	 */
	public abstract void filter();

	protected float[] getInput(int inputChannel) {
		ComponentChannel channel = inputs[inputChannel];
		if (channel == null || channel.component == null)
			return new float[ComponentControl.getBufferSize()];
		if (!channel.component.bufferValid)
			return new float[ComponentControl.getBufferSize()];
		return channel.component.outputBuffer[channel.channelID];
	}

	protected void setOutput(int outputChannel, float[] data) {
		outputBuffer[outputChannel] = data;
	}

	public void outputTo(int outputChannel, ComponentChannel destination) {
		outputs[outputChannel] = destination;
		if (destination != null && destination.component != null)
			destination.component.inputFrom(destination.channelID, this, outputChannel);
	}

	private void outputTo(int outputChannel, Component destination, int inputChannel) {
		outputs[outputChannel] = new ComponentChannel(destination, inputChannel);
	}

	public void inputFrom(int inputChannel, ComponentChannel origin) {
		inputs[inputChannel] = origin;
		if (origin != null && origin.component != null)
			origin.component.outputTo(origin.channelID, this, inputChannel);
	}

	private void inputFrom(int inputChannel, Component origin, int outputChannel) {
		inputs[inputChannel] = new ComponentChannel(origin, outputChannel);
	}

	public void syncWith(int syncChannel, ComponentChannel partner) {
		syncs[syncChannel] = partner;
		if (partner != null && partner.component != null)
			partner.component.syncWith(partner.channelID, this, syncChannel);
	}

	private void syncWith(int syncChannel, Component partner, int partnerChannel) {
		syncs[syncChannel] = new ComponentChannel(partner, partnerChannel);
	}

}
