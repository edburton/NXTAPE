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

	public SimpleArm(int joints) {
		this.joints = joints;
		jointAngle = new double[joints];
		jointMovement = new double[joints];
		xs = new double[joints];
		ys = new double[joints];
		jointLength = new double[joints];
		states = new double[joints * 4];
		stateNames = new String[joints * 4];
		double totalJointLength = 0;
		for (int i = 0; i < joints; i++) {
			int c = 0;
			stateNames[(c++ * joints) + i] = "Joint Movement " + i;
			stateNames[(c++ * joints) + i] = "Joint Angle " + i;
			stateNames[(c++ * joints) + i] = "Joint X " + i;
			stateNames[(c++ * joints) + i] = "Joint Y " + i;
			double l = (joints + 1) - i;
			jointLength[i] = l;
			totalJointLength += l;
		}
		for (int i = 0; i < joints; i++)
			jointLength[i] /= totalJointLength;
		System.out.println("");
		for (int i = 0; i < stateNames.length; i++)
			System.out.print("[" + stateNames[i] + "]");
		System.out.println("");
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
		return joints * 4;
	}

	@Override
	public void updateStates() {
		double x = 0;
		double y = 0;
		double a = 0;
		for (int i = 0; i < joints; i++) {
			jointAngle[i] += jointMovement[i];
			a += jointAngle[i];
			x += Math.cos(a) * jointLength[i];
			y += Math.sin(a) * jointLength[i];
			xs[i] = x;
			ys[i] = y;
			int c = 0;
			states[(c++ * joints) + i] = jointMovement[i];
			states[(c++ * joints) + i] = jointAngle[i];
			states[(c++ * joints) + i] = xs[i];
			states[(c++ * joints) + i] = ys[i];
		}
	}

	@Override
	public void setOutputState(double state, int stateChannel) {
		if (stateChannel < joints)
			jointMovement[stateChannel] = state + 0.01;
	}

	public double[] getXs() {
		return xs;
	}

	public double[] getYs() {
		return ys;
	}
}
