package org.opendrawer.ape.darwinianneurodynamics;

public class TestingDataProvider implements DataProvider {

	private double phase;
	private double[] output;
	private static final int[] channelTypes = new int[] { INTERNAL, INTERNAL };
	private int c = 0;

	public TestingDataProvider(double phase) {
		this.phase = phase;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getData() {
		return output;
	}

	@Override
	public String[] getChannelNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getChannelTypes() {
		// TODO Auto-generated method stub
		return channelTypes;
	}

	@Override
	public int getChannelCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void step() {
		output = new double[] {
				Math.random() / 10 + phase * (Math.cos(c / 20.0d)),
				Math.random() / 10 + phase * (Math.sin(c / 20.0d)) };
		c++;
	}
}
