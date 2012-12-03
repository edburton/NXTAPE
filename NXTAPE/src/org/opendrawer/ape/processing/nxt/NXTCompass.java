package org.opendrawer.ape.processing.nxt;

import lejos.nxt.addon.CompassHTSensor;

import org.opendrawer.ape.darwinianneurodynamics.DataProvider;

public class NXTCompass implements DataProvider {
	private final CompassHTSensor compass;
	private final String name;
	private static final String[] channelNames = new String[] { "Heading South" };
	private static final int[] channelTypes = new int[] { DataProvider.INPUT };
	private boolean inhibited = false;
	private double direction;

	public NXTCompass(CompassHTSensor compass, String name) {
		this.compass = compass;
		this.name = name;
	}

	@Override
	public double[] getData() {
		return new double[] { direction };
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
		if (compass != null && !inhibited) {
			float directionIn = compass.getDegrees();
			direction = directionIn / 360.0d;
			// System.out.println(directionIn);
		}

	}

	@Override
	public int getChannelCount() {
		return 1;
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
