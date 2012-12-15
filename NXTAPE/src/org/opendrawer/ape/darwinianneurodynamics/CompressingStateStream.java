package org.opendrawer.ape.darwinianneurodynamics;

public class CompressingStateStream extends StateStream {

	private int steps = 1;
	private double stepStates[] = new double[steps];
	private int step = 0;

	public CompressingStateStream(int streamLength) {
		super(streamLength);
	}

	public CompressingStateStream(StatesProvider statesProvider, int i,
			int streamLength) {
		super(statesProvider, i, streamLength);
	}

	@Override
	public double read(int pastPosition) {
		if (stateStream != null) {
			if (pastPosition > streamLength || pastPosition > totalWriteHead)
				return Double.NaN;
			int index = pastPosition;
			while (index < 0)
				index += streamLength;
			return (stateStream[index]);
		} else
			return Double.NaN;
	}

	@Override
	protected void write(double value) {
		if (stateStream != null) {
			stepStates[step++] = value;
			if (step >= steps) {
				step = 0;
				double v = Double.MIN_VALUE;
				for (int i = 0; i < steps; i++)
					v = Math.max(stepStates[i], v);
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
							stateStream[i] = Math.max(temp[i * 2],
									temp[i * 2 + 1]);
						} else
							stateStream[i] = Double.NaN;
				}
			} else if (step > 1) {
				double v = Double.MIN_VALUE;
				for (int i = 0; i < step - 1; i++)
					v = Math.max(stepStates[i], v);
				// v /= step - 1;
				stateStream[writeHead] = v;
			}
		}
	}

	@Override
	public int getMin() {
		return 0;
	}

	@Override
	public int getMax() {
		return 1;
	}
}
