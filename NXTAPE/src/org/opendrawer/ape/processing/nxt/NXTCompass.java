package org.opendrawer.ape.processing.nxt;

import lejos.nxt.addon.CompassHTSensor;

import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;

public class NXTCompass extends StatesProvider {
	private final CompassHTSensor compass;
	private static final int[] stateTypes = new int[] { INPUT };
	private double direction;

	public NXTCompass(CompassHTSensor compass) {
		this.compass = compass;
	}

	@Override
	public double[] getStates() {
		return new double[] { direction };
	}

	@Override
	public void updateStates() {
		if (compass != null) {
			float directionIn = compass.getDegrees();
			direction = directionIn / 360.0d;
		}

	}

	@Override
	public int getStatesLength() {
		return 1;
	}

	@Override
	public int[] getStateTypes() {
		return stateTypes;
	}
}
