package org.opendrawer.ape.processing.nxt;

import org.opendrawer.ape.darwinianneurodynamics.DataStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.HomogeneousDataStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.Reflex;

public class LinearReflex extends Reflex {

	private double weight;

	public LinearReflex(DataStreamBundle inputDataStreamBundle,
			HomogeneousDataStreamBundle outputDataStreamBundle,
			int inputChannel, int outputChannel, double weight) {
		super(inputDataStreamBundle, outputDataStreamBundle, inputChannel,
				outputChannel);
		this.weight=weight;
	}
	
	@Override
	public void react() {
		double input = dataStreamBundles.get(0).getDataStreams()
				.get(inputChannel).read(0);
		double output = input * weight;
		double[] outputs = outputDataProvider.getData();
		output = outputs[outputChannel] + output;
		outputDataProvider.setOutputChannel(output, outputChannel);
	}
}
