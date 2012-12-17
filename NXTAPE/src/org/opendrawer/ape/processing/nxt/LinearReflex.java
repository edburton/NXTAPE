package org.opendrawer.ape.processing.nxt;

import org.opendrawer.ape.darwinianneurodynamics.Reflex;
import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;

public class LinearReflex extends Reflex {

	private final double weight;

	public LinearReflex(StateStreamBundle inputStatesStreamBundle,
			StatesProvider outputStatesProvider, int inputChannel,
			int outputChannel, double weight) {
		super(inputStatesStreamBundle, outputStatesProvider, inputChannel,
				outputChannel);
		this.weight = weight;
	}

	@Override
	public void react() {
		double input = stateStreamBundles.get(0).getStateStreams()
				.get(inputChannel).read(0);
		double output = input * weight;
		double[] states = outputStatesProvider.getStates();
		output = states[outputChannel] + output;
		outputStatesProvider.setOutputState(output, outputChannel);
	}
}
