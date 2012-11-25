package org.opendrawer.dawinian.neurodynamics;

import java.util.ArrayList;
import java.util.List;

public class DataStreamCore {
	private List<DataStreamBundle> dataStreamBundles = new ArrayList<DataStreamBundle>();
	private List<DataStream> allDataStreams = new ArrayList<DataStream>();
	private List<DataStream> inputDataStreams = new ArrayList<DataStream>();
	private List<DataStream> outputDataStreams = new ArrayList<DataStream>();

	public void prepareDataStreams() {
		for (int i = 0; i < dataStreamBundles.size(); i++)
			allDataStreams.addAll(dataStreamBundles.get(i).getDataStreams());

		for (int i = 0; i < allDataStreams.size(); i++) {
			DataStream dataStream = allDataStreams.get(i);
			int type = dataStream.getDataType();
			if (type == DataProvider.INPUT)
				inputDataStreams.add(dataStream);
			if (type == DataProvider.OUTPUT)
				outputDataStreams.add(dataStream);
		}
	}

	public void addDataStreamBundle(DataStreamBundle dataStreamBundle) {
		dataStreamBundles.add(dataStreamBundle);
	}

	public DataStream getDataStreamByName(String name) {
		for (int i = 0; i < allDataStreams.size(); i++) {
			DataStream dataStream = allDataStreams.get(i);
			if (allDataStreams.get(i).getName().equalsIgnoreCase(name))
				return dataStream;
		}
		return null;
	}

	public void step() {
		for (int i = 0; i < dataStreamBundles.size(); i++)
			dataStreamBundles.get(i).getDataProvider().step();

		for (int i = 0; i < allDataStreams.size(); i++) {
			DataStream dataStream = allDataStreams.get(i);
			dataStream
					.write(dataStream.getDataProvider().getNormalizedValues()[dataStream
							.getDataProviderChannel()]);
		}
	}
}
