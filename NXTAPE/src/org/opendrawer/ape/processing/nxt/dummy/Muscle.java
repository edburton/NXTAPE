package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.OutputStatesProvider;

public class Muscle extends OutputStatesProvider {
	private double requestedRestLength = 1;
	private double currentRestLength = requestedRestLength;
	private static final int[] stateTypes = new int[] { OUTPUT, INPUT };

	public Muscle() {
	}

	@Override
	public void updateStates() {
		currentRestLength = (requestedRestLength + currentRestLength * 3) / 4;
	}

	@Override
	public void setOutputState(double state, int stateIndex) {
		requestedRestLength = state;
	}

	@Override
	public double[] getStates() {
		return new double[] { requestedRestLength, currentRestLength };
	}

	@Override
	public int getStatesLength() {
		return 2;
	}

	public double getCurrentRestLength() {
		return currentRestLength;
	}

	@Override
	public int[] getStateTypes() {
		return stateTypes;
	}
}
