package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.OutputDataProvider;

public class EyeBall implements OutputDataProvider {

	private static String[] channelNames = new String[] { "temp", "temp" };
	private static final int[] channelTypes = new int[] { INPUT, OUTPUT };

	public EyeBall() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "Eye Ball";
	}

	@Override
	public double[] getData() {
		return new double[] { Math.random(), Math.random() };
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
	public int getChannelCount() {
		return 2;
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOutputChannel(double data, int dataChannel) {
		// TODO Auto-generated method stub

	}

}
