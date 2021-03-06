package org.opendrawer.ape.darwinianneurodynamics;

public class Error extends StatesProvider {
	private int channels = 1;
	private double[] states = new double[channels];
	private static int[] stateTypes = new int[] { GENERIC };
	int count = 0;

	public Error() {
	}

	public Error(int channels) {
		this.channels = channels;
		states = new double[channels];
		stateTypes = new int[channels];
		for (int i = 0; i < channels; i++)
			stateTypes[i] = GENERIC;
	}

	@Override
	public double[] getStates() {
		return states;
	}

	@Override
	public int getStatesLength() {
		return channels;
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
		return stateTypes;
	}
}
