package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.Reflex;
import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;
import org.opendrawer.ape.darwinianneurodynamics.Util;

public class WobbleReflex extends Reflex {
	double counter = 0;
	double s = 0.1;
	double ss = s;
	int c = 0;
	int n = 10;

	public WobbleReflex(StateStreamBundle inputStatesStreamBundle,
			StatesProvider outputStatesProvider, int inputIndex, int outputIndex) {
		super(inputStatesStreamBundle, outputStatesProvider, inputIndex,
				outputIndex);
	}

	@Override
	public void react() {
		if (outputStatesProvider == null)
			return;
		if (c % n == 0) {
			n = Util.randomInt(2, 100);
			s = Math.pow(Math.random() / 2, 2);
		}
		double output = Math.cos(counter);
		ss = (s + ss * 19) / 20;
		counter += ss;
		c++;
		outputStatesProvider.setOutputState(output, outputChannel);
	}
}
