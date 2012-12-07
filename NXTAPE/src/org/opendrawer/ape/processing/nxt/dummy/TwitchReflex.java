package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.OutputStatesProvider;
import org.opendrawer.ape.darwinianneurodynamics.Reflex;
import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundle;

public class TwitchReflex extends Reflex {
	int twitchTime = 0;
	double twitchLength = 0;
	int counter = 0;

	public TwitchReflex(StateStreamBundle inputStatesStreamBundle,
			OutputStatesProvider outputStatesProvider, int inputIndex,
			int outputIndex) {
		super(inputStatesStreamBundle, outputStatesProvider, inputIndex,
				outputIndex);
	}

	@Override
	public void react() {
		if (outputStatesProvider == null)
			return;
		double input = 0;
		for (int i = 0; i < stateStreamBundles.get(0).getStateStreams().size(); i++) {
			double v = Math.abs(stateStreamBundles.get(0).getStateStreams()
					.get(i).read(0));
			input += v * v;
		}
		input = Math.sqrt(input);
		stateStreamBundles.get(0).getStateStreams().get(inputChannel).read(0);
		if (input < 0.1 && counter > twitchTime && Math.random() > 0.99) {
			counter = 0;
			twitchTime = 20;
			twitchLength = Math.random();
		}
		double output = counter++ > twitchTime ? 1 : twitchLength;
		outputStatesProvider.setOutputState(output, outputChannel);
	}
}
