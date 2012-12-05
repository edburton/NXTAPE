package org.opendrawer.ape.darwinianneurodynamics;

public abstract class Reflex extends DataStreamBundleList {

	protected int inputChannel;
	protected int outputChannel;
	protected OutputDataProvider outputDataProvider;

	public Reflex(DataStreamBundle inputDataStreamBundle,
			HomogeneousDataStreamBundle outputDataStreamBundle,
			int inputChannel, int outputChannel) {
		super(inputDataStreamBundle, outputDataStreamBundle);
		this.inputChannel = inputChannel;
		this.outputChannel = outputChannel;
		outputDataProvider = (OutputDataProvider) outputDataStreamBundle
				.getDataProvider();
	}

	public abstract void react();
}
