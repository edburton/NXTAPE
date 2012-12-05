package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.DataStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.HomogeneousDataStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.Reflex;

public class TwitchReflex extends Reflex {
	int twitchTime = 0;
	double twitchLength = 0;
	int counter = 0;

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
		double input = 0;
		for (int i = 0; i < dataStreamBundles.get(0).getDataStreams().size(); i++) {
			double v = Math.abs(dataStreamBundles.get(0).getDataStreams()
					.get(i).read(0));
			input += v * v;
		}
		input = Math.sqrt(input);
		dataStreamBundles.get(0).getDataStreams().get(inputChannel).read(0);
		if (input < 0.0000001 && counter > twitchTime && Math.random() > 0.99) {
			counter = 0;
			twitchTime = 20;
			twitchLength = Math.random();
		}
		double output = counter++ > twitchTime ? 1 : twitchLength;
		outputDataProvider.setOutputChannel(output, outputChannel);
	}
}
