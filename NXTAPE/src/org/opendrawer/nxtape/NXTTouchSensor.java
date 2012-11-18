package org.opendrawer.nxtape;

import lejos.nxt.TouchSensor;

public class NXTTouchSensor implements DataProvider {
	private final TouchSensor touchSensor;
	private final String name;
	private static String[] streamName = new String[] { "On/Off" };
	private boolean on = false;

	public NXTTouchSensor(TouchSensor touchSensor, String name) {
		this.touchSensor = touchSensor;
		this.name = name;
	}

	@Override
	public float[] getNormalizedValues() {
		return new float[] { on ? 1 : 0 };
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
		if (touchSensor != null)
			on = touchSensor.isPressed();
	}

	@Override
	public int getChannelCount() {
		return 1;
	}

	@Override
	public void finishStep() {
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

}
