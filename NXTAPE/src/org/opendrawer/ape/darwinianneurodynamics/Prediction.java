package org.opendrawer.ape.darwinianneurodynamics;

public class Prediction implements DataProvider {
	int channels;
	private int[] channelTypes;
	double[] data;

	public Prediction(int channels) {
		this.channels = channels;
		channelTypes = new int[channels];
		for (int i = 0; i < channels; i++)
			channelTypes[i] = INTERNAL;
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
		return channelTypes;
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
}
