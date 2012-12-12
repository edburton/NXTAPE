package org.opendrawer.ape.darwinianneurodynamics;

public class Error extends OutputStatesProvider {
	double error;
	private static final int[] stateTypes = new int[] { GENERIC };

	public Error() {
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getStates() {
		return new double[] { error };
	}

	@Override
	public String[] getStateNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStatesLength() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void updateStates() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOutputState(double state, int stateChannel) {
		error = state;
	}

	@Override
	public int[] getStateTypes() {
		return stateTypes;
	}
}
