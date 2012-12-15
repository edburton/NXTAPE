package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.OutputStatesProvider;

public class SimpleArm extends OutputStatesProvider {

	private final String[] stateNames;
	private final int joints;
	private final double[] jointMovement;
	private final double[] jointAngle;
	private final double[] xs;
	private final double[] ys;
	private final double[] jointLength;
	private final double[] states;
	private final int[] stateTypes;

	public SimpleArm(int joints) {
		this.joints = joints;
		jointAngle = new double[joints];
		jointMovement = new double[joints];
		xs = new double[joints];
		ys = new double[joints];
		jointLength = new double[joints];
		states = new double[joints * 2 + 2];
		stateNames = new String[states.length];
		stateTypes = new int[states.length];
		double totalJointLength = 0;
		for (int i = 0; i < joints; i++) {
			int c = 0;
			stateNames[(c++ * joints) + i] = "Joint Movement " + (i + 1);
			stateNames[(c++ * joints) + i] = "Joint Angle " + (i + 1);
			double l = (joints + 1) - i;
			jointLength[i] = l;
			totalJointLength += l;
		}
		stateNames[states.length - 2] = "X extremity";
		stateNames[states.length - 1] = "Y extremity";
		for (int i = 0; i < joints; i++)
			jointLength[i] /= totalJointLength;
	}

	@Override
	public String getName() {
		return "Simple Arm";
	}

	@Override
	public double[] getStates() {
		return states;
	}

	@Override
	public String[] getStateNames() {
		return stateNames;
	}

	@Override
	public int getStatesLength() {
		return states.length;
	}

	@Override
	public void updateStates() {
		double x = 0;
		double y = 0;
		double a = 0;
		for (int i = 0; i < joints; i++) {
			jointAngle[i] += jointMovement[i] / 10;
			if (jointAngle[i] < -1) {
				jointAngle[i] = -1;
			} else if (jointAngle[i] > 1)
				jointAngle[i] = 1;
			a += ((jointAngle[i]) + 1) * (Math.PI / 4);
			x += Math.cos(a) * jointLength[i];
			y += Math.sin(a) * jointLength[i];
			xs[i] = x;
			ys[i] = y;
			int c = 0;
			states[(c++ * joints) + i] = jointMovement[i];
			states[(c++ * joints) + i] = jointAngle[i];
		}
		states[states.length - 2] = x;
		states[states.length - 1] = y;
	}

	@Override
	public void setOutputState(double state, int stateChannel) {
		if (stateChannel < joints)
			jointMovement[stateChannel] = state;
	}

	public double[] getXs() {
		return xs;
	}

	public double[] getYs() {
		return ys;
	}

	@Override
	public int[] getStateTypes() {
		return stateTypes;
	}
}
