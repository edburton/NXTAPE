package org.opendrawer.ape.darwinianneurodynamics;

public class Prediction implements DataProvider {
	int channels;
	double[] data;

	public Prediction(int channels) {
		this.channels = channels;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getData() {
		return data;
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
		return channels;
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub

	}

	public void setData(double[] data) {
		this.data = data;
	}

	@Override
	public void setInhihited(boolean inhibited) {
		// TODO Auto-generated method stub

	}

}
