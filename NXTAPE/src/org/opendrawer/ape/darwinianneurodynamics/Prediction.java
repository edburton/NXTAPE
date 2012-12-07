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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getStates() {
		return states;
	}

	@Override
	public String[] getStateNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStatesLength() {
		// TODO Auto-generated method stub
		return states.length;
	}

	@Override
	public void updateStates() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOutputState(double state, int stateChannel) {
		states[stateChannel] = state;
	}
}
