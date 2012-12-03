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
		return 1;
	}

	@Override
	public void step() {
		output = new double[] { Math.random() < 0.9 ? Math.random() + phase
				* (Math.sin(phase * System.currentTimeMillis() / 600.0d))
				: Double.NaN /*
							 * , Math.random() - phase (Math.sin(phase *
							 * System.currentTimeMillis() / 600.0d))
							 */};

	}

	@Override
	public void setInhihited(boolean inhibited) {
		// TODO Auto-generated method stub

	}

}
