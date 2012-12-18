package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;
import org.opendrawer.ape.darwinianneurodynamics.Util;

public class Muscle extends StatesProvider {
	private double requestedRestLength = 1;
	private double currentRestLength = requestedRestLength;
	private static final int[] stateTypes = new int[] { OUTPUT, INPUT };

	public Muscle() {
	}

	@Override
	public void updateStates() {
		currentRestLength = Util
				.clampMinusOneToOne((requestedRestLength + currentRestLength * 3) / 4);
	}

	@Override
	public void setOutputState(double state, int stateIndex) {
		requestedRestLength = Util.clampMinusOneToOne(state);
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
		return (currentRestLength + 1) / 2;
	}

	@Override
	public int[] getStateTypes() {
		return stateTypes;
	}
}
