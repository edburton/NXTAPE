package org.opendrawer.dawinian.neurodynamics;

public interface InputProvider extends DataProvider {
	public abstract int getInputChannelCount();

	public abstract void setInputChannels(double[] data);
}
