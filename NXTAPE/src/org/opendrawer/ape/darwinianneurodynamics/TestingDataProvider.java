package org.opendrawer.ape.darwinianneurodynamics;

public class TestingDataProvider implements DataProvider {

	public TestingDataProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getNormalizedValues() {
		return new double[] {
				Math.random()
						* (Math.sin(System.currentTimeMillis() / 2000.0d)),
				Math.random()
						* (Math.cos(System.currentTimeMillis() / 2000.0d)) };
	}

	@Override
	public String[] getChannelNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getChannelTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getChannelCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInhihited(boolean inhibited) {
		// TODO Auto-generated method stub

	}

}
