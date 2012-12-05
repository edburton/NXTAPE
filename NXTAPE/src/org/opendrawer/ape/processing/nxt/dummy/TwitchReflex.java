package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.DataStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.HomogeneousDataStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.Reflex;

public class TwitchReflex extends Reflex {

	private int count = (int) (50 * Math.random());

	public TwitchReflex(DataStreamBundle inputDataStreamBundle,
			HomogeneousDataStreamBundle outputDataStreamBundle,
			int inputChannel, int outputChannel) {
		super(inputDataStreamBundle, outputDataStreamBundle, inputChannel,
				outputChannel);
	}

	@Override
	public void react() {
		if (outputDataProvider == null)
			return;
		double output = Math.random() < 0.9 ? 1 : Math.pow(Math.random(), 8);
		double[] outputs = outputDataProvider.getData();
		output = outputs[outputChannel] + output;
		outputDataProvider.setOutputChannel(output, outputChannel);
	}
}
