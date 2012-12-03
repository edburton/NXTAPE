package org.opendrawer.ape.darwinianneurodynamics;

public class DataStream {
	private int dataWidth;
	private double[] data;
	private int writeHead = 0;
	private int totalWriteHead = 0;
	private DataProvider dataProvider;
	private int dataProviderChannel;

	public DataStream(DataProvider dataProvider, int dataProviderChannel,
			int dataWidth) {
		super();
		setDataProvider(dataProvider, dataProviderChannel, dataWidth);
	}

	public DataStream() {
		super();
	}

	public DataStream(int dataWidth) {
		super();
		this.dataWidth = dataWidth;
		data = new double[dataWidth];
	}

	public void setDataProvider(DataProvider dataProvider,
			int dataProviderChannel, int dataWidth) {
		this.dataWidth = dataWidth;
		this.dataProvider = dataProvider;
		this.dataProviderChannel = dataProviderChannel;
		data = new double[dataWidth];
	}

	public void write(double value) {
		if (data != null) {
			data[writeHead] = value;
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
		if (data == null)
			return null;
		double[] result = new double[dataWidth];
		for (int i = 0; i < dataWidth; i++)
			result[i] = read(i);
		return result;
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
