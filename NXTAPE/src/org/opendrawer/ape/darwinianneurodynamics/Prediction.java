package org.opendrawer.ape.darwinianneurodynamics;

public class Prediction extends OutputStatesProvider {
	int length;
	double[] states;

	public Prediction(int length) {
		this.length = length;
		states = new double[length];
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public double[] getStates() {
		return states;
	}

	@Override
	public String[] getStateNames() {
		return null;
	}

	@Override
	public int getStatesLength() {
		return states.length;
	}

	@Override
	public void updateStates() {

	}

	@Override
	public void setOutputState(double state, int stateChannel) {
		states[stateChannel] = state;
	}

	@Override
	public int[] getStateTypes() {
		return null;
	}
}
