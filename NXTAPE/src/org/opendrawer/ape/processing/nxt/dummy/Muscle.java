package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.OutputStatesProvider;

public class Muscle extends OutputStatesProvider {
	private final String name;
	private double requestedRestLength = 1;
	private double currentRestLength = requestedRestLength;
	private static String[] stateNames = new String[] {
			"Requested rest length", "Current rest length" };

	public Muscle(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void updateStates() {
		currentRestLength = (requestedRestLength + currentRestLength) / 2;
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
	public String[] getStateNames() {
		return stateNames;
	}

	@Override
	public int getStatesLength() {
		return 2;
	}

	public double getCurrentRestLength() {
		return currentRestLength;
	}
}
