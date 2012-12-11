package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.OutputStatesProvider;

public class SimpleArm extends OutputStatesProvider {

	private static String[] stateNames = new String[] { "X", "Y", "Speed" };
	private final int joints;
	private final double[] jointAngles;

	public SimpleArm(int joints) {
		this.joints = joints;
		jointAngles = new double[joints];
	}

	@Override
	public String getName() {
		return "Simple Arm";
	}

	@Override
	public double[] getStates() {
		return new double[] {};
	}

	@Override
	public String[] getStateNames() {
		return stateNames;
	}

	@Override
	public int getStatesLength() {
		return 0;
	}

	@Override
	public void updateStates() {

	}

	@Override
	public void setOutputState(double state, int stateChannel) {

	}
}
