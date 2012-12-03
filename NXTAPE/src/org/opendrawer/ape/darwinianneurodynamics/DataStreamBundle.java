package org.opendrawer.ape.darwinianneurodynamics;

import java.util.ArrayList;
import java.util.List;

public class DataStreamBundle {
	protected List<DataStream> dataStreams = new ArrayList<DataStream>();
	protected int dataWidth;

	public DataStreamBundle(int dataWidth) {
		this.dataWidth = dataWidth;
	}

	public void write(double[] values) {
		for (int i = 0; i < dataStreams.size() && i < values.length; i++)
			dataStreams.get(i).write(values[i]);
	}

	public double[] read(int pastPosition) {
		double[] values = new double[dataStreams.size()];
		for (int i = 0; i < dataStreams.size(); i++)
			values[i] = read(pastPosition, i);
		return values;
	}

	public double[][] read() {
		double[][] values = new double[2][dataStreams.size()];
		for (int d = 0; d < dataStreams.size(); d++)
			for (int t = 0; t < 2; t++)
				values[t] = dataStreams.get(d).read();
		return values;
	}

	public double read(int pastPosition, int channel) {
		return dataStreams.get(channel).read(pastPosition);
	}

	public int getDataWidth() {
		return dataWidth;
	}

	public List<DataStream> getDataStreams() {
		return dataStreams;
	}

	public void addDataStream(DataStream dataStream) {
		dataStreams.add(dataStream);
	}

	public void addDataProvidedStreams(DataProvider dataProvider) {
		for (int i = 0; i < dataProvider.getChannelCount(); i++)
			addDataStream(new DataStream(dataProvider, i, dataWidth));
	}

	public void addEmptyDataStreams(int n) {
		for (int i = 0; i < n; i++)
			addDataStream(new DataStream(dataWidth));
	}

	public void removeDataStream(DataStream dataStream) {
		dataStreams.remove(dataStream);
	}
}
