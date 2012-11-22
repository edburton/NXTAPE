package org.opendrawer.nxtape;

public class DataStream {
	protected final int dataWidth;
	private float[] data;
	private int writeHead = 0;
	protected int totalWriteHead = 0;
	protected DataProvider dataProvider;

	public DataStream(int width, DataProvider dataProvider) {
		super();
		this.dataWidth = width;
		this.dataProvider = dataProvider;
		data = new float[width];
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
