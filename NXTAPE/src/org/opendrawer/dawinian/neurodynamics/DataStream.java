package org.opendrawer.dawinian.neurodynamics;

public class DataStream {
	protected int dataWidth;
	private double[] data;
	private int writeHead = 0;
	protected int totalWriteHead = 0;
	protected DataProvider dataProvider;
	protected int dataProviderChannel;

	public DataStream(DataProvider dataProvider, int dataProviderChannel,
			int dataWidth) {
		super();
		setDateProvider(dataProvider, dataProviderChannel, dataWidth);
	}

	public DataStream() {
		super();
	}

	public DataStream(int dataWidth) {
		super();
		this.dataWidth = dataWidth;
		data = new double[dataWidth];
	}

	public void setDateProvider(DataProvider dataProvider,
			int dataProviderChannel, int dataWidth) {
		this.dataWidth = dataWidth;
		this.dataProvider = dataProvider;
		this.dataProviderChannel = dataProviderChannel;
		data = new double[dataWidth];
	}

	public void write(double values) {
		if (data != null) {
			data[writeHead] = values;
			writeHead++;
			totalWriteHead++;
			if (writeHead >= dataWidth)
				writeHead = 0;
		}
	}

	public double read(int pastPosition) {
		if (data != null) {
			if (pastPosition > dataWidth || pastPosition > totalWriteHead)
				return Double.NaN;
			int index = writeHead - (1 + pastPosition);
			while (index < 0)
				index += dataWidth;
			return data[index];
		} else
			return Double.NaN;
	}

	public double[] read() {
		return data;
	}

	public int getDataWidth() {
		return dataWidth;
	}

	public DataProvider getDataProvider() {
		return dataProvider;
	}

	public String getName() {
		if (dataProvider != null)
			return dataProvider.getName() + ':'
					+ dataProvider.getChannelNames()[dataProviderChannel];
		return "no DataProvider";
	}

	public int getDataType() {
		if (dataProvider != null)
			return dataProvider.getChannelTypes()[dataProviderChannel];
		return DataProvider.NULL;
	}

	public int getDataProviderChannel() {
		return dataProviderChannel;
	}
}
