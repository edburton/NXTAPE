package org.opendrawer.dawinian.neurodynamics;

public class DataStream {
	protected final int dataWidth;
	private float[] data;
	private int writeHead = 0;
	protected int totalWriteHead = 0;
	protected DataProvider dataProvider;
	protected int dataProviderChannel;

	public DataStream(DataProvider dataProvider, int dataProviderChannel,
			int dataWidth) {
		super();
		this.dataWidth = dataWidth;
		this.dataProvider = dataProvider;
		this.dataProviderChannel = dataProviderChannel;
		data = new float[dataWidth];
	}

	public void write(float value) {
		data[writeHead] = value;
		writeHead++;
		totalWriteHead++;
		if (writeHead >= dataWidth)
			writeHead = 0;
	}

	public float read(int pastPosition) {
		if (pastPosition > dataWidth || pastPosition > totalWriteHead)
			return Float.NaN;
		int index = writeHead - (1 + pastPosition);
		while (index < 0)
			index += dataWidth;
		return data[index];
	}

	public int getDataWidth() {
		return dataWidth;
	}

	public DataProvider getDataProvider() {
		return dataProvider;
	}
}
