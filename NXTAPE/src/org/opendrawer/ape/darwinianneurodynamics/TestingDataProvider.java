package org.opendrawer.ape.darwinianneurodynamics;

public class TestingDataProvider implements DataProvider {

	private double phase;
	private double[] output;

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
		return null;
	}

	@Override
	public int getChannelCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void step() {
		output = new double[] {
				phase
						* (1 + Math.random())
						* (Math.sin(1.11 * phase * System.currentTimeMillis()
								/ 1600.0d)),
				phase
						* 1.7
						* (1 + Math.random())
						* (-Math.sin(phase * System.currentTimeMillis()
								/ 1600.0d)) };

	}

	@Override
	public void setInhihited(boolean inhibited) {
		// TODO Auto-generated method stub

	}

}
