package org.opendrawer.dawinian.neurodynamics;

import java.util.ArrayList;
import java.util.List;

public class DataStreamBundle {
	private DataProvider dataProvider;
	private List<DataStream> dataStreams = new ArrayList<DataStream>();
	private int dataWidth;

	public DataStreamBundle(DataProvider dataProvider, int dataWidth) {
		this.dataProvider = dataProvider;
		this.dataWidth = dataWidth;
		int channels = dataProvider.getChannelCount();
		for (int i = 0; i < channels; i++)
			dataStreams.add(new DataStream(dataProvider, i, dataWidth));
	}

	public void write(double[] values) {
		for (int i = 0; i < dataStreams.size(); i++)
			dataStreams.get(i).write(values[i]);
	}

	public double[] read(int pastPosition) {
		double[] values = new double[dataStreams.size()];
		for (int i = 0; i < dataStreams.size(); i++)
			values[i] = read(pastPosition, i);
		return values;
	}

	public double read(int pastPosition, int channel) {
		return dataStreams.get(channel).read(pastPosition);
	}

	public int getDataWidth() {
		return dataWidth;
	}

	public DataProvider getDataProvider() {
		return dataProvider;
	}

	public List<DataStream> getDataStreams() {
		return dataStreams;
	}
}
