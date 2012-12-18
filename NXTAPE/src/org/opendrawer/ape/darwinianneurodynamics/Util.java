package org.opendrawer.ape.darwinianneurodynamics;

import java.util.Random;

public class Util {
	private static Random random = new Random();

	public static int RandomInt(int min, int max) {
		checkRandom();
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	private static void checkRandom() {
		if (random == null)
			random = new Random();
	}

	public static double clamp(double v, double min, double max) {
		if (v <= min)
			return min;
		if (v >= max)
			return max;
		return v;
	}

	public static double clampMinusOneToOne(double v) {
		return clamp(v, -1, 1);
	}

	public static double distance(double x, double y) {
		return Math.sqrt((x * x) + (y * y));
	}

	public static double distance(double x1, double y1, double x2, double y2) {
		return distance(x1 - x2, y1 - y2);
	}
}
