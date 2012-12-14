package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.OutputStatesProvider;
import org.opendrawer.ape.darwinianneurodynamics.Reflex;
import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundle;

public class SimpleArmTwitchReflex extends Reflex {
	int twitchTime = 1;
	double twitchAmount = 0;
	int counter = 0;

	public SimpleArmTwitchReflex(StateStreamBundle inputStatesStreamBundle,
			OutputStatesProvider outputStatesProvider, int inputIndex,
			int outputIndex) {
		super(inputStatesStreamBundle, outputStatesProvider, inputIndex,
				outputIndex);
	}

	@Override
	public void react() {
		if (outputStatesProvider == null)
			return;
		if (counter > twitchTime && Math.random() > 0.9) {
			counter = 0;
			twitchTime = 1 + (int) ((Math.random()) * 50);
			twitchAmount = (Math.random() - 0.5) * 2;
		}
		double output = counter > twitchTime ? 0 : (twitchAmount / 2)
				* (1 - Math
						.cos((counter / (double) twitchTime) * (Math.PI * 2)));
		counter++;
		outputStatesProvider.setOutputState(output, outputChannel);
	}
}
