package org.opendrawer.ape.processing.nxt;

import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.HomogeneousStateStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.Reflex;

public class LinearReflex extends Reflex {

	private double weight;

	public LinearReflex(StateStreamBundle inputStatesStreamBundle,
			HomogeneousStateStreamBundle outputStatesStreamBundle,
			int inputChannel, int outputChannel, double weight) {
		super(inputStatesStreamBundle, outputStatesStreamBundle, inputChannel,
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
