package org.opendrawer.dawinian.neurodynamics;

import java.util.ArrayList;
import java.util.List;

public class NeurodynamicStreamCore {
	private List<DataStreamBundle> dataStreamBundles = new ArrayList<DataStreamBundle>();
	private List<DataStream> allDataStreams = new ArrayList<DataStream>();
	private List<OutputDataProvider> outputDataProviders = new ArrayList<OutputDataProvider>();
	private List<DataProvider> inputOnlyDataProviders = new ArrayList<DataProvider>();
	private List<Reflex> reflexes = new ArrayList<Reflex>();
	private List<Actor> actors = new ArrayList<Actor>();
	private List<Predictor> predictors = new ArrayList<Predictor>();

	public void prepareDataStreams() {
		for (int i = 0; i < dataStreamBundles.size(); i++)
			allDataStreams.addAll(dataStreamBundles.get(i).getDataStreams());

		for (int i = 0; i < allDataStreams.size(); i++) {
			DataStream dataStream = allDataStreams.get(i);
			int type = dataStream.getDataType();
			if (type == DataProvider.OUTPUT)
				if (!outputDataProviders.contains(dataStream.getDataProvider()))
					outputDataProviders.add((OutputDataProvider) dataStream
							.getDataProvider());
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

	public void addActor(Actor actor) {
		actors.add(actor);
	}

	public void addPredictor(Predictor predictor) {
		predictors.add(predictor);
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
		stepInputs();
		stepOutputs();
	}

	private void stepInputs() {
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

	private void stepOutputs() {
		for (int i = 0; i < outputDataProviders.size(); i++) {
			int channels = outputDataProviders.get(i).getChannelCount();
			for (int n = 0; n < channels; n++)
				outputDataProviders.get(i).setOutputChannel(0, n);
		}

		for (int i = 0; i < reflexes.size(); i++)
			reflexes.get(i).react();

		for (int i = 0; i < outputDataProviders.size(); i++)
			outputDataProviders.get(i).step();

		for (int i = 0; i < allDataStreams.size(); i++) {
			DataStream dataStream = allDataStreams.get(i);
			if (outputDataProviders.contains(dataStream.getDataProvider()))
				dataStream.write(dataStream.getDataProvider()
						.getNormalizedValues()[dataStream
						.getDataProviderChannel()]);
		}

		for (int i = 0; i < predictors.size(); i++)
			predictors.get(i).predict();
	}
}
