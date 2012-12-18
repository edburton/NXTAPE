package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;
import org.opendrawer.ape.darwinianneurodynamics.Util;

public class SimpleArm extends StatesProvider {
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
		states = new double[joints + 2];
		stateTypes = new int[states.length];
		double totalJointLength = 0;
		for (int i = 0; i < joints; i++) {
			double l = (joints + 1) - i;
			jointLength[i] = l;
			totalJointLength += l;
		}
		for (int i = 0; i < joints; i++)
			jointLength[i] /= totalJointLength;
	}

	@Override
	public double[] getStates() {
		return states;
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
			a += ((jointAngle[i]) + 1) * (Math.PI / 8);
			x += Math.cos(a) * jointLength[i];
			y += Math.sin(a) * jointLength[i];
			xs[i] = x;
			ys[i] = y;
			int c = 0;
			jointAngle[i] = Util.clampMinusOneToOne(jointAngle[i]);
			// states[(c++ * joints) + i] = jointMovement[i];
			states[(c++ * joints) + i] = jointAngle[i];
		}
		states[states.length - 2] = x;
		states[states.length - 1] = y;
	}

	@Override
	public void setOutputState(double state, int stateChannel) {
		if (stateChannel < joints)
			jointMovement[stateChannel] = Util.clampMinusOneToOne(state);
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
