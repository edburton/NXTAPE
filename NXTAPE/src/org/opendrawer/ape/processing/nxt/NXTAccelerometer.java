package org.opendrawer.ape.processing.nxt;

import lejos.nxt.addon.AccelHTSensor;

import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;

public class NXTAccelerometer extends StatesProvider {
	private final AccelHTSensor accelerometer;
	private final String name;
	private static final String[] stateNames = new String[] { "X", "Y", "Z" };
	private static final int[] stateTypes = new int[] { INPUT, INPUT, INPUT };
	private final double states[];
	private final float maxExpectedValue = 256; // Arbitrary

	public NXTAccelerometer(AccelHTSensor accelerometer, String name) {
		this.accelerometer = accelerometer;
		this.name = name;
		states = new double[3];
	}

	@Override
	public double[] getStates() {
		return states;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String[] getStateNames() {
		return stateNames;
	}

	@Override
	public void updateStates() {
		if (accelerometer != null) {
			int[] intValues = new int[3];
			accelerometer.getAllAccel(intValues, 0);
			for (int i = 0; i < 3; i++)
				states[i] = intValues[i] / maxExpectedValue;
		}
	}

	@Override
	public int getStatesLength() {
		return 3;
	}

	@Override
	public int[] getStateTypes() {
		return stateTypes;
	}
}
