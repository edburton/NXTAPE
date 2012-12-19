package org.opendrawer.ape.darwinianneurodynamics;

public class StatesStreamBundleMean extends StatesProvider {
	private double error = Double.NaN;
	private final StateStreamBundle inputStateStreamBundle;

	public StatesStreamBundleMean(StateStreamBundle inputStateStreamBundle) {
		this.inputStateStreamBundle = inputStateStreamBundle;
	}

	@Override
	public void updateStates() {
		error = 0;
		int c = 0;
		double[] input = inputStateStreamBundle.read(0);
		for (int i = 0; i < input.length; i++) {
			double e = input[i];
			if (!Double.isNaN(e)) {
				error += e;
				c++;
			}
		}
		if (c > 0)
			error /= c;
		else
			error = Double.NaN;
		notifyStatesObservers();
	}

	@Override
	public double[] getStates() {
		return new double[] { error };
	}

	@Override
	public int[] getStateTypes() {
		return new int[] { GENERIC };
	}

	@Override
	public int getStatesLength() {
		return 1;
	}

	@Override
	public void setOutputState(double state, int stateChannel) {

	}
}
