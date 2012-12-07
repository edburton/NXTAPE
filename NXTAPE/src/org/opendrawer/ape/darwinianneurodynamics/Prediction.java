package org.opendrawer.ape.darwinianneurodynamics;

public class Prediction extends StatesProvider {
	int states;
	private final int[] stateTypes;
	double[] state;

	public Prediction(int states) {
		this.states = states;
		stateTypes = new int[states];
		for (int i = 0; i < states; i++)
			stateTypes[i] = INTERNAL;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getStates() {
		return state;
	}

	@Override
	public String[] getStateNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getStateTypes() {
		return stateTypes;
	}

	@Override
	public int getStatesLength() {
		// TODO Auto-generated method stub
		return states;
	}

	@Override
	public void updateStates() {
		// TODO Auto-generated method stub

	}

	public void setState(double[] state) {
		this.state = state;
	}
}
