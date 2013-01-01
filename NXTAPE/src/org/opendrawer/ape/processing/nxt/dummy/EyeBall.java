package org.opendrawer.ape.processing.nxt.dummy;

import java.util.ArrayList;
import java.util.List;

import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;
import org.opendrawer.ape.darwinianneurodynamics.Util;

public class EyeBall extends StatesProvider {
	private static final int[] stateTypes = new int[] { INPUT, INPUT, INPUT };
	private final List<Muscle> muscles = new ArrayList<Muscle>();
	private double x = 0;
	private double y = 0;
	private double xv = 0;
	private double yv = 0;
	private double speed = 0;
	private static final double k = 0.25;
	private static final double f = 0.95;
	private int count = 0;

	public EyeBall() {
	}

	@Override
	public double[] getStates() {
		return new double[] { count++ % 20 < 5 ? 1 : 0 /* x, y, xv, yv, speed */};
	}

	@Override
	public int getStatesLength() {
		return 1;// 5;
	}

	@Override
	public void updateStates() {
		for (int i = 0; i < muscles.size(); i++) {
			Muscle muscle = muscles.get(i);
			double mx = getMuscleX(i);
			double my = getMuscleY(i);
			double dx = x + mx;
			double dy = y + my;
			double actualLength = Math.sqrt(dx * dx + dy * dy);
			double restLength = muscle.getCurrentRestLength();
			double fx = (dx / actualLength) * (actualLength - restLength) * k;
			double fy = (dy / actualLength) * (actualLength - restLength) * k;
			xv -= fx;
			yv -= fy;
		}
		xv *= f;
		yv *= f;
		x += xv / 60;
		y += yv / 60;
		double d = Util.distance(x, y);
		if (d > 1) {
			x /= d;
			y /= d;
		}
		d = Util.distance(xv, yv);
		if (d > 1) {
			xv /= d;
			yv /= d;
		}
		speed = Math.sqrt(xv * xv + yv * yv);
	}

	public void addMuscle(Muscle muscle) {
		muscles.add(muscle);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getMuscleCount() {
		return muscles.size();
	}

	public float getMuscleX(int i) {
		double a = (Math.PI * 2) * (i / (double) muscles.size());
		return (float) Math.sin(a) * 2;
	}

	public float getMuscleY(int i) {
		double a = (Math.PI * 2) * (i / (double) muscles.size());
		return (float) Math.cos(a) * 2;
	}

	@Override
	public int[] getStateTypes() {
		return stateTypes;
	}

	@Override
	public void setOutputState(double state, int stateChannel) {
		switch (stateChannel) {
		case 0:
			// x = state;
			break;
		case 1:
			// y = state;
			break;
		case 2:
			xv = state;
			break;
		case 3:
			yv = state;
			break;
		case 4:
			speed = state;
			break;
		}
	}
}
