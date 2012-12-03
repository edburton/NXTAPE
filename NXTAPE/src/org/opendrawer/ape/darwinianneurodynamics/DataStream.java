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
	private int minValueIndex = 0;
	private int maxValueIndex = 0;

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
		minValueIndex = maxValueIndex = dataWidth + 1;
	}

	public void setDateProvider(DataProvider dataProvider,
			int dataProviderChannel, int dataWidth) {
		this.dataWidth = dataWidth;
		this.dataProvider = dataProvider;
		this.dataProviderChannel = dataProviderChannel;
		data = new double[dataWidth];
		minValueIndex = maxValueIndex = dataWidth + 1;
	}

	private void resetMin() {
		minValue = Double.NaN;
		for (int i = 0; i < dataWidth; i++) {
			double value = read(i);
			if (!Double.isNaN(value)) {
				if (Double.isNaN(minValue)) {
					minValue = value;
					minValueIndex = i - dataWidth;
				} else if (value < minValue) {
					minValue = value;
					minValueIndex = i - dataWidth;
				}
			}
		}
	}

	private void resetMax() {
		maxValue = Double.NaN;
		for (int i = 0; i < dataWidth; i++) {
			double value = read(i);
			if (!Double.isNaN(value)) {
				if (Double.isNaN(maxValue)) {
					maxValue = value;
					maxValueIndex = i - dataWidth;
				} else if (value > maxValue) {
					maxValue = value;
					maxValueIndex = i - dataWidth;
				}
			}
		}
	}

	public void write(double value) {
		if (data != null) {
			data[writeHead] = value;
			writeHead++;
			totalWriteHead++;
			if (writeHead >= dataWidth)
				writeHead = 0;
		}
		if (++minValueIndex > dataWidth)
			resetMin();
		if (++maxValueIndex > dataWidth)
			resetMax();
	}

	public double readWithMinMaxScaledToZeroOne(int pastPosition) {
		if (Double.isNaN(minValue) || Double.isNaN(maxValue))
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
