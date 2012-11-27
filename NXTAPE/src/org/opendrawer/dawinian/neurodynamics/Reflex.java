package org.opendrawer.dawinian.neurodynamics;

public class Reflex extends DataStreamBundleList {
	private int inputChannel;
	private int outputChannel;
	private double weight;
	private OutputDataProvider outputDataProvider;

	public Reflex(DataStreamBundle inputDataStreamBundle,
			HomogeneousDataStreamBundle outputDataStreamBundle,
			int inputChannel, int outputChannel, double weight) {
		super(inputDataStreamBundle, outputDataStreamBundle);
		this.inputChannel = inputChannel;
		this.outputChannel = outputChannel;
		this.weight = weight;
		outputDataProvider = (OutputDataProvider) outputDataStreamBundle
				.getDataProvider();
	}

	public void activate() {
		double input = dataStreamBundles.get(0).getDataStreams()
				.get(inputChannel).read(0);
		double output = input * weight;
		double[] outputs = outputDataProvider.getNormalizedValues();
		output = outputs[outputChannel] + output;
		outputDataProvider.setOutputChannel(output, outputChannel);
	}
}
