package org.opendrawer.dawinian.neurodynamics;

import java.util.ArrayList;
import java.util.List;

public class DataStreamCore {
	private List<DataStreamBundle> dataStreamBundles = new ArrayList<DataStreamBundle>();
	private List<DataStream> allDataStreams = new ArrayList<DataStream>();
	private List<DataProvider> outputDataProviders = new ArrayList<DataProvider>();
	private List<DataProvider> inputOnlyDataProviders = new ArrayList<DataProvider>();
	private List<Reflex> reflexes = new ArrayList<Reflex>();

	public void prepareDataStreams() {
		for (int i = 0; i < dataStreamBundles.size(); i++)
			allDataStreams.addAll(dataStreamBundles.get(i).getDataStreams());

		for (int i = 0; i < allDataStreams.size(); i++) {
			DataStream dataStream = allDataStreams.get(i);
			int type = dataStream.getDataType();
			if (type == DataProvider.OUTPUT)
				if (!outputDataProviders.contains(dataStream.getDataProvider()))
					outputDataProviders.add(dataStream.getDataProvider());
		}
		for (int i = 0; i < allDataStreams.size(); i++) {
			DataStream dataStream = allDataStreams.get(i);
			if (!outputDataProviders.contains(dataStream.getDataProvider())) {
				if (!inputOnlyDataProviders.contains(dataStream
						.getDataProvider()))
					inputOnlyDataProviders.add(dataStream.getDataProvider());
			}
		}
	}

	public void addReflex(Reflex reflex) {
		reflexes.add(reflex);
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

	public void stepInputs() {
		for (int i = 0; i < inputOnlyDataProviders.size(); i++)
			inputOnlyDataProviders.get(i).step();

		for (int i = 0; i < allDataStreams.size(); i++) {
			DataStream dataStream = allDataStreams.get(i);
			if (inputOnlyDataProviders.contains(dataStream.getDataProvider()))
				dataStream.write(dataStream.getDataProvider()
						.getNormalizedValues()[dataStream
						.getDataProviderChannel()]);
		}
	}

	public void stepOutputs() {
		for (int i = 0; i < reflexes.size(); i++)
			reflexes.get(i).map();

		for (int i = 0; i < outputDataProviders.size(); i++)
			outputDataProviders.get(i).step();

		for (int i = 0; i < allDataStreams.size(); i++) {
			DataStream dataStream = allDataStreams.get(i);
			if (outputDataProviders.contains(dataStream.getDataProvider()))
				dataStream.write(dataStream.getDataProvider()
						.getNormalizedValues()[dataStream
						.getDataProviderChannel()]);
		}
	}
}
