package org.opendrawer.ape.processing.nxt.dummy;

import java.util.ArrayList;
import java.util.List;

import org.opendrawer.ape.darwinianneurodynamics.DataProvider;

public class EyeBall implements DataProvider {

	private static String[] channelNames = new String[] { "X", "Y", "Speed" };
	private static final int[] channelTypes = new int[] { INPUT, INPUT, INPUT };
	private final List<Muscle> muscles = new ArrayList<Muscle>();
	private double x = 0;
	private double y = 0;
	private double xv = 0;
	private double yv = 0;
	private double speed = 0;
	private static final double k = 0.05;
	private static final double f = 0.8;

	public EyeBall() {
	}

	@Override
	public String getName() {
		return "Eye Ball";
	}

	@Override
	public double[] getData() {
		return new double[] { x, y, speed };
	}

	@Override
	public String[] getChannelNames() {
		return channelNames;
	}

	@Override
	public int[] getChannelTypes() {
		return channelTypes;
	}

	@Override
	public int getChannelCount() {
		return 3;
	}

	@Override
	public void step() {
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
		speed = Math.sqrt(xv * xv + yv * yv);
		x += xv;
		y += yv;
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
		return (float) Math.sin(a);
	}

	public float getMuscleY(int i) {
		double a = (Math.PI * 2) * (i / (double) muscles.size());
		return (float) Math.cos(a);
	}
}
