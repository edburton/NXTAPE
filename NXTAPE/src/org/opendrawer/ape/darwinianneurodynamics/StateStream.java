package org.opendrawer.ape.darwinianneurodynamics;

public class StateStream {
	private int streamLength;
	private double[] stateStream;
	private int writeHead = 0;
	private int totalWriteHead = 0;
	private StatesProvider statesProvider;
	private int statesProviderChannel;

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

	public void write(double value) {
		if (stateStream != null) {
			stateStream[writeHead] = value;
			writeHead++;
			totalWriteHead++;
			if (writeHead >= streamLength)
				writeHead = 0;
		}

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

	public String getName() {
		if (statesProvider != null)
			return statesProvider.getName() + ':'
					+ statesProvider.getStateNames()[statesProviderChannel];
		return "no StatesProvider";
	}

	public int getStateType() {
		if (statesProvider != null)
			return statesProvider.getStateTypes()[statesProviderChannel];
		return StatesProvider.NULL;
	}

	public int getStatesProviderChannel() {
		return statesProviderChannel;
	}
}
