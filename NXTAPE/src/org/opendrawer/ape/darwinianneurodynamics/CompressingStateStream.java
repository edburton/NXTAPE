package org.opendrawer.ape.darwinianneurodynamics;

public class CompressingStateStream extends StateProviderStream {

	private int steps = 1;
	private double stepStates[] = new double[steps];
	private int step = 0;
	private float subStep = 0;
	protected StatesProvider statesProvider;
	protected int statesProviderChannel;

	public CompressingStateStream(StatesProvider statesProvider, int i,
			int streamLength) {
		super(statesProvider, i, streamLength);
	}

	@Override
	protected void write(double value) {
		if (stateStream != null) {
			stepStates[step++] = value;
			if (step >= steps) {
				step = 0;
				double v = 0;
				for (int i = 0; i < steps; i++)
					v += stepStates[i];
				v /= steps;
				stateStream[writeHead] = v;
				writeHead++;
				totalWriteHead++;
				if (writeHead >= streamLength) {
					steps *= 2;
					step = 0;
					stepStates = new double[steps];
					writeHead = streamLength / 2;
					double[] temp = new double[streamLength];
					for (int i = 0; i < streamLength; i++)
						temp[i] = stateStream[i];
					for (int i = 0; i < streamLength; i++)
						if (i < streamLength / 2) {
							stateStream[i] = (temp[i * 2] + temp[i * 2 + 1]) / 2;
						} else
							stateStream[i] = Double.NaN;
				}
			} else if (step > 1) {
				double v = 0;
				for (int i = 0; i < step - 1; i++)
					v += stepStates[i];
				v /= step - 1;
				stateStream[writeHead - 1] = v;
			}
			subStep = step / (float) steps;
		}
	}

	@Override
	public float getSubStep() {
		return subStep;
	}

	@Override
	public int getMin() {
		return 0;
	}

	@Override
	public int getMax() {
		return 1;
	}

	@Override
	public void setStatesProvider(StatesProvider statesProvider,
			int statesProviderChannel, int streamLength) {
		statesProvider.addStreamObserver(this);
		this.streamLength = streamLength;
		this.statesProvider = statesProvider;
		this.statesProviderChannel = statesProviderChannel;
		stateStream = new double[streamLength];
		setToNaN();
	}

	@Override
	public StatesProvider getStatesProvider() {
		return statesProvider;
	}

	@Override
	public int getStatesProviderChannel() {
		return statesProviderChannel;
	}
}
