package org.opendrawer.ape.darwinianneurodynamics;

public class TestingStatesProvider extends StatesProvider {

	private final double phase;
	private double[] states;
	private int c = 0;

	public TestingStatesProvider(double phase) {
		this.phase = phase;
	}

	@Override
	public double[] getStates() {
		return states;
	}

	@Override
	public int getStatesLength() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void updateStates() {
		states = new double[] {
				Math.random() / 10 + phase * (Math.cos(c / 20.0d)),
				Math.random() / 10 + phase * (Math.sin(c / 20.0d)) };
		c++;
	}

	@Override
	public int[] getStateTypes() {
		return null;
	}

	@Override
	public void setOutputState(double state, int stateChannel) {
		states[stateChannel] = state;
	}
}
