package org.opendrawer.dawinian.neurodynamics;

public class DataStream {
	protected final int dataWidth;
	private double[] data;
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
		data = new double[dataWidth];
	}

	public void write(double values) {
		data[writeHead] = values;
		writeHead++;
		totalWriteHead++;
		if (writeHead >= dataWidth)
			writeHead = 0;
	}

	public double read(int pastPosition) {
		if (pastPosition > dataWidth || pastPosition > totalWriteHead)
			return Double.NaN;
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

	public String getName() {
		return dataProvider.getName() + ':'
				+ dataProvider.getChannelNames()[dataProviderChannel];
	}

	public int getDataType() {
		return dataProvider.getChannelTypes()[dataProviderChannel];
	}

	public int getDataProviderChannel() {
		return dataProviderChannel;
	}
}
