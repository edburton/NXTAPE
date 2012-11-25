package org.opendrawer.nxtape;

import lejos.nxt.addon.AccelHTSensor;

import org.opendrawer.dawinian.neurodynamics.DataProvider;

public class NXTAccelerometer implements DataProvider {
	private final AccelHTSensor accelerometer;
	private final String name;
	private static final String[] channelNames = new String[] { "X", "Y", "Z" };
	private static final int[] channelTypes = new int[] { DataProvider.INPUT,
			DataProvider.INPUT, DataProvider.INPUT };
	private boolean inhibited = false;
	private double values[];
	private float maxExpectedValue = 256; // Arbitrary

	public NXTAccelerometer(AccelHTSensor accelerometer, String name) {
		this.accelerometer = accelerometer;
		this.name = name;
		values = new double[3];
	}

	@Override
	public double[] getNormalizedValues() {
		return values;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String[] getChannelNames() {
		return channelNames;
	}

	@Override
	public void step() {
		if (accelerometer != null && !inhibited) {
			int[] intValues = new int[3];
			accelerometer.getAllAccel(intValues, 0);
			for (int i = 0; i < 3; i++)
				values[i] = intValues[i] / maxExpectedValue;
		}
	}

	@Override
	public int getChannelCount() {
		return 3;
	}

	@Override
	public void setInhihited(boolean inhibited) {
		this.inhibited = inhibited;
	}

	@Override
	public int[] getChannelTypes() {
		return channelTypes;
	}
}
