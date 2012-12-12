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
}
