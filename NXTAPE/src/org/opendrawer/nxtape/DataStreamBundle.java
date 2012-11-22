package org.opendrawer.nxtape;

public class DataStreamBundle {
	private DataProvider dataProvider;
	private DataStream[] dataStreams;
	private int dataWidth;

	public DataStreamBundle(DataProvider dataProvider, int dataWidth) {
		this.dataProvider = dataProvider;
		this.dataWidth = dataWidth;
		int channels = dataProvider.getChannelCount();
		dataStreams = new DataStream[channels];
		for (int i = 0; i < channels; i++)
			dataStreams[i] = new DataStream(dataProvider, i, dataWidth);
	}

	public void write(float[] values) {
		for (int i = 0; i < dataStreams.length; i++)
			dataStreams[i].write(values[i]);
	}

	public float[] read(int pastPosition) {
		float[] values = new float[dataStreams.length];
		for (int i = 0; i < dataStreams.length; i++)
			values[i] = read(pastPosition, i);
		return values;
	}

	public float read(int pastPosition, int channel) {
		return dataStreams[channel].read(pastPosition);
	}

	public int getDataWidth() {
		return dataWidth;
	}

	public DataProvider getDataProvider() {
		return dataProvider;
	}
}
