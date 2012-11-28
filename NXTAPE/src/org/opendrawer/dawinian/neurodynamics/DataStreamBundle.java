package org.opendrawer.dawinian.neurodynamics;

import java.util.ArrayList;
import java.util.List;

public class DataStreamBundle {
	protected List<DataStream> dataStreams = new ArrayList<DataStream>();
	protected int dataWidth;

	public DataStreamBundle(int dataWidth) {
		this.dataWidth = dataWidth;
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

	public double[][] read() {
		double[][] values = new double[dataWidth][dataStreams.size()];
		for (int d = 0; d < dataStreams.size(); d++)
			for (int t = 0; t < dataWidth; t++)
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

	public void AddDataStream(DataStream dataStream) {
		dataStreams.add(dataStream);
	}

	public void RemoveDataStream(DataStream dataStream) {
		dataStreams.remove(dataStream);
	}
}
