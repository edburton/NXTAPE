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
		if (counter > twitchTime && Math.random() > 0.995) {
			counter = 0;
			twitchTime = (int) ((1 + Math.random()) * 10);
			twitchLength = Math.pow(Math.random(), 2);
		}
		double output = counter++ > twitchTime ? 1 : twitchLength;
		outputDataProvider.setOutputChannel(output, outputChannel);
	}
}
