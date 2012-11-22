package org.opendrawer.nxtape;

import lejos.nxt.addon.AccelHTSensor;

public class NXTAccelerometer implements DataProvider {
	private final AccelHTSensor accelerometer;
	private final String name;
	private static String[] streamNames = new String[] { "X", "Y", "Z" };
	private boolean inhibited = false;
	private float values[];
	private float maxExpectedValue = 256; // Arbitrary

	public NXTAccelerometer(AccelHTSensor accelerometer, String name) {
		this.accelerometer = accelerometer;
		this.name = name;
		values = new float[3];
	}

	@Override
	public float[] getNormalizedValues() {
		return values;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String[] getChannelNames() {
		return streamNames;
	}

	@Override
	public void startStep() {
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
	public void finishStep() {
	}

	@Override
	public void setInhihited(boolean inhibited) {
		this.inhibited = inhibited;
	}
}
