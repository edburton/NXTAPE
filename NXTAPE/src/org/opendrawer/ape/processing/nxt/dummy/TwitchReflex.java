package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.DataProvider;
import org.opendrawer.ape.darwinianneurodynamics.DataStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.DataStreamBundleList;
import org.opendrawer.ape.darwinianneurodynamics.HomogeneousDataStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.OutputDataProvider;

public class TwitchReflex extends DataStreamBundleList {
	private int outputChannel;
	@SuppressWarnings("unused")
	private int inputChannel;
	@SuppressWarnings("unused")
	private DataProvider inputDataProvider;
	private OutputDataProvider outputDataProvider;
	private int count=(int)(50*Math.random());

	public TwitchReflex(DataStreamBundle inputDataStreamBundle,
			HomogeneousDataStreamBundle outputDataStreamBundle,
			int inputChannel, int outputChannel) {
		super(inputDataStreamBundle, outputDataStreamBundle);
		this.inputChannel = inputChannel;
		this.outputChannel = outputChannel;
		outputDataProvider = (OutputDataProvider) outputDataStreamBundle
				.getDataProvider();
	}

	public void react() {
		double output = Math.random() < 0.9 ? 1 : Math.pow(Math.random(), 8);
		double[] outputs = outputDataProvider.getData();
		output = outputs[outputChannel] + output;
		outputDataProvider.setOutputChannel(output, outputChannel);
	}
}
