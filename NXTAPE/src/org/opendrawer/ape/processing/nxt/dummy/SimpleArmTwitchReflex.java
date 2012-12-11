package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.OutputStatesProvider;
import org.opendrawer.ape.darwinianneurodynamics.Reflex;
import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundle;

public class SimpleArmTwitchReflex extends Reflex {
	int twitchTime = 0;
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
		if (counter > twitchTime && Math.random() > 0.95) {
			counter = 0;
			twitchTime = (int) ((Math.random() + 1) * 20);
			twitchAmount = (Math.random() - 0.5) * 2;
		}
		double output = counter++ > twitchTime ? 0 : twitchAmount;
		outputStatesProvider.setOutputState(output, outputChannel);
	}
}
