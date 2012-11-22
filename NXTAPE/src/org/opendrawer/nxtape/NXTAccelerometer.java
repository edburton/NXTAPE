package org.opendrawer.nxtape;

import lejos.nxt.addon.AccelHTSensor;

public class NXTAccelerometer implements DataProvider {
	private final AccelHTSensor accelerometer;
	private final String name;
	private static String[] streamName = new String[] { "X", "Y", "Z" };
	private boolean inhibited = false;
	private int[] values;
	private float maxReasonableValue = 300;

	public NXTAccelerometer(AccelHTSensor accelerometer, String name) {
		this.accelerometer = accelerometer;
		this.name = name;
		values=new int[3];
	}

	@Override
	public float[] getNormalizedValues() {
		return new float[] { values[0] / maxReasonableValue,
				values[1] / maxReasonableValue, values[2] / maxReasonableValue };
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String[] getChannelNames() {
		return streamName;
	}

	@Override
	public void startStep() {
		if (accelerometer != null && !inhibited)
			accelerometer.getAllAccel(values, 0);
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
