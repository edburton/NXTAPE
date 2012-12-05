package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.OutputDataProvider;

public class Muscle implements OutputDataProvider {
	private final String name;
	private final double friction = 0.9;
	private double restLength = 0.5;
	private static String[] channelNames = new String[] { "Rest Length",
			"Actual Length" };
	private static final int[] channelTypes = new int[] { INPUT, OUTPUT };
	private boolean inhibited = false;

	public Muscle(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void step() {

	}

	@Override
	public void setOutputChannel(double data, int dataChannel) {
		restLength = data;
	}

	@Override
	public double[] getData() {
		return new double[] { restLength, 1 };
	}

	@Override
	public String[] getChannelNames() {
		return channelNames;
	}

	@Override
	public int getChannelCount() {
		return 2;
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
