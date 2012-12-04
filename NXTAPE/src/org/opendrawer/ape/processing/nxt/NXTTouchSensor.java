package org.opendrawer.ape.processing.nxt;

import lejos.nxt.TouchSensor;

import org.opendrawer.ape.darwinianneurodynamics.DataProvider;

public class NXTTouchSensor implements DataProvider {
	private final TouchSensor touchSensor;
	private final String name;
	private static String[] channelNames = new String[] { "On/Off" };
	private static int[] channelTypes = new int[] { INPUT };
	private boolean on = false;
	private boolean inhibited = false;

	public NXTTouchSensor(TouchSensor touchSensor, String name) {
		this.touchSensor = touchSensor;
		this.name = name;
	}

	@Override
	public double[] getData() {
		return new double[] { on ? 1 : 0 };
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
	public int[] getChannelTypes() {
		return channelTypes;
	}

	@Override
	public void step() {
		if (touchSensor != null && !inhibited)
			on = touchSensor.isPressed();
	}

	@Override
	public int getChannelCount() {
		return 1;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	@Override
	public void setInhihited(boolean inhibited) {
		this.inhibited = inhibited;
	}
}
