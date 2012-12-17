package org.opendrawer.ape.processing.nxt;

import lejos.nxt.TouchSensor;

import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;

public class NXTTouchSensor extends StatesProvider {
	private final TouchSensor touchSensor;
	private static final int[] stateTypes = new int[] { INPUT };
	private boolean on = false;

	public NXTTouchSensor(TouchSensor touchSensor) {
		this.touchSensor = touchSensor;
	}

	@Override
	public double[] getStates() {
		return new double[] { on ? 1 : 0 };
	}

	@Override
	public void updateStates() {
		if (touchSensor != null)
			on = touchSensor.isPressed();
	}

	@Override
	public int getStatesLength() {
		return 1;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	@Override
	public int[] getStateTypes() {
		return stateTypes;
	}
}
