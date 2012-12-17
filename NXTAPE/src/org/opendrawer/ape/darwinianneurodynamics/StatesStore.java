package org.opendrawer.ape.darwinianneurodynamics;

public class StatesStore extends StatesProvider {
	int length;
	double[] states;

	public StatesStore(int length) {
		this.length = length;
		states = new double[length];
	}

	@Override
	public double[] getStates() {
		return states;
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
