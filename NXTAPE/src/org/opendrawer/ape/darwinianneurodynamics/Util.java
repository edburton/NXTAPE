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

	public static int clamp(int v, int min, int max) {
		if (v <= min)
			return min;
		if (v >= max)
			return max;
		return v;
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

	public static double distribution(double input, double offset, double range) {
		double d = Math.abs(input - offset) / range;
		if (d < 1) {
			double v = (Math.cos(d * Math.PI) + 1) / 2;
			return v * input;
		}
		return 0;
	}

	public static double distribution(double input[], double timeOffset,
			double timeRange) {
		int length = input.length;
		int firstIndex = clamp(
				(int) Math.floor((timeOffset - timeRange) * length), 0,
				length - 1);
		int lastIndex = clamp(
				(int) Math.ceil((timeOffset + timeRange) * length), 0,
				length - 1);
		double result = 0;
		double t = 0;
		for (int i = firstIndex; i <= lastIndex; i++) {
			double x = (i / (length - 1.0)) - timeOffset;
			double v = distribution(x, timeOffset, timeRange);
			t += v;
			result += v * input[i];
		}
		if (t > 0)
			result /= t;
		return result;
	}

	public static double distribution(double input[], double offset,
			double range, double timeOffset, double timeRange) {
		return distribution(distribution(input, timeOffset, timeRange), offset,
				range);
	}
}
