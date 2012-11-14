package org.opendrawer.nxtape;

public class DataStream {
	protected final int width;
	private float[][] data;
	private int writeHead = 0;
	protected DataProvider dataProvider;

	public DataStream(int width, DataProvider dataProvider) {
		super();
		this.width = width;
		this.dataProvider = dataProvider;
		data = new float[width][dataProvider.getChannels()];
	}

	public void write(float[] values) {
		data[writeHead] = values;
		writeHead++;
		if (writeHead >= width)
			writeHead = 0;
	}

	public float[] read(int pastPosition) {
		int index = writeHead - (1 + pastPosition);
		while (index < 0)
			index += width;
		return data[index];
	}

	public float read(int pastPosition, int channel) {
		int index = writeHead - (1 + pastPosition);
		while (index < 0)
			index += width;
		return data[index][channel];
	}

	public int getWidth() {
		return width;
	}
}
