package org.opendrawer.ape.processing.nxt;

import lejos.nxt.addon.CompassHTSensor;

import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;

public class NXTCompass extends StatesProvider {
	private final CompassHTSensor compass;
	private final String name;
	private static final String[] stateNames = new String[] { "Heading South" };
	private double direction;

	public NXTCompass(CompassHTSensor compass, String name) {
		this.compass = compass;
		this.name = name;
	}

	@Override
	public double[] getStates() {
		return new double[] { direction };
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
		if (compass != null) {
			float directionIn = compass.getDegrees();
			direction = directionIn / 360.0d;
		}

	}

	@Override
	public int getStatesLength() {
		return 1;
	}
}
