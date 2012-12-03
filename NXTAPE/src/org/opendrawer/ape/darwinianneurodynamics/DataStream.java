package org.opendrawer.ape.darwinianneurodynamics;

public class DataStream {
	private int dataWidth;
	private double[] data;
	private int writeHead = 0;
	private int totalWriteHead = 0;
	private DataProvider dataProvider;
	private int dataProviderChannel;
	private double minValue = Double.NaN;
	private double maxValue = Double.NaN;

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

	public void write(double value) {
		if (Double.isNaN(minValue)) {
			minValue = value;
			maxValue = value;
		} else if (value < minValue)
			minValue = value;
		else if (value > maxValue)
			maxValue = value;
		if (data != null) {
			data[writeHead] = value;
			writeHead++;
			totalWriteHead++;
			if (writeHead >= dataWidth)
				writeHead = 0;
		}
	}

	public double readWithMinMaxScaledToZeroOne(int pastPosition) {
		if (Double.isNaN(minValue))
			return Double.NaN;
		double value = read(pastPosition);
		if (Double.isNaN(value))
			return Double.NaN;
		if (maxValue - minValue == 0)
			return 0;
		return (value - minValue) / (maxValue - minValue);
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
