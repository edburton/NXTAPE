package org.opendrawer.ape.darwinianneurodynamics;

public class StateStream implements StatesObserver {
	protected int streamLength;
	protected double[] stateStream;
	protected int writeHead = 0;
	protected int totalWriteHead = 0;
	protected StatesProvider statesProvider;
	protected int statesProviderChannel;
	protected double min = -1;
	protected double max = 1;

	public StateStream(StatesProvider statesProvider,
			int statesProviderChannel, int streamLength) {
		super();
		setStatesProvider(statesProvider, statesProviderChannel, streamLength);
	}

	public StateStream() {
		super();
	}

	public StateStream(int streamLength) {
		super();
		this.streamLength = streamLength;
		stateStream = new double[streamLength];
		setToNaN();
	}

	public void setStatesProvider(StatesProvider statesProvider,
			int statesProviderChannel, int streamLength) {
		statesProvider.addStreamObserver(this);
		this.streamLength = streamLength;
		this.statesProvider = statesProvider;
		this.statesProviderChannel = statesProviderChannel;
		stateStream = new double[streamLength];
		setToNaN();
	}

	private void setToNaN() {
		for (int i = 0; i < streamLength; i++)
			stateStream[i] = Double.NaN;
	}

	public double read(int pastPosition) {
		if (stateStream != null) {
			if (pastPosition > streamLength || pastPosition > totalWriteHead)
				return Double.NaN;
			int index = writeHead - (1 + pastPosition);
			while (index < 0)
				index += streamLength;
			return stateStream[index];
		} else
			return Double.NaN;
	}

	public double[] read() {
		if (stateStream == null)
			return null;
		double[] result = new double[streamLength];
		for (int i = 0; i < streamLength; i++) {
			result[i] = read(i);
		}
		return result;
	}

	public int getStreamLength() {
		return streamLength;
	}

	public StatesProvider getStatesProvider() {
		return statesProvider;
	}

	public int getStatesProviderChannel() {
		return statesProviderChannel;
	}

	public void setOutputState(double state) {
		statesProvider.setOutputState(state, statesProviderChannel);
	}

	@Override
	public void statesUpdated(StatesProvider statesProvider) {
		double[] states = statesProvider.getStates();
		if (states != null)
			write(states[statesProviderChannel]);
	}

	protected void write(double value) {
		if (stateStream != null) {
			stateStream[writeHead] = value;
			writeHead++;
			totalWriteHead++;
			if (writeHead >= streamLength)
				writeHead = 0;
		}
	}

	public int getMin() {
		return -1;
	}

	public int getMax() {
		return 1;
	}

	public float getSubStep() {
		return 0;
	}
}
