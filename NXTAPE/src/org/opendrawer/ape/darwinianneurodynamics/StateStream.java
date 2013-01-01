package org.opendrawer.ape.darwinianneurodynamics;

public abstract class StateStream extends StatesProvider implements
		StatesObserver {
	protected int streamLength;
	protected double[] stateStream;
	protected int writeHead = 0;
	protected int totalWriteHead = 0;
	protected double min = -1;
	protected double max = 1;

	public StateStream(int streamLength) {
		super();
		this.streamLength = streamLength;
		stateStream = new double[streamLength];
		setToNaN();
	}

	protected void setToNaN() {
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

	@Override
	public double[] getStates() {
		return new double[] { read(0) };
	}

	@Override
	public int[] getStateTypes() {
		return new int[] { GENERIC };
	}

	@Override
	public int getStatesLength() {
		return 1;
	}

	@Override
	public void setOutputState(double state, int stateChannel) {
		write(state);
	}

	@Override
	public void updateStates() {
		notifyStatesObservers();
	}
}
