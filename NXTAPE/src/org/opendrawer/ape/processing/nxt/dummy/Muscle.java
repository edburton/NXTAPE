package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.OutputDataProvider;

public class Muscle implements OutputDataProvider {
	private final String name;
	private double requestedRestLength = 1;
	private double currentRestLength = requestedRestLength;
	private static String[] channelNames = new String[] {
			"Requested rest length", "Current rest length" };
	private static final int[] channelTypes = new int[] { OUTPUT, INPUT };

	public Muscle(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void step() {
		currentRestLength = (requestedRestLength + currentRestLength) / 2;
	}

	@Override
	public void setOutputChannel(double data, int dataChannel) {
		requestedRestLength = data;
	}

	@Override
	public double[] getData() {
		return new double[] { requestedRestLength, currentRestLength };
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
	public int[] getChannelTypes() {
		return channelTypes;
	}

	public double getCurrentRestLength() {
		return currentRestLength;
	}
}
