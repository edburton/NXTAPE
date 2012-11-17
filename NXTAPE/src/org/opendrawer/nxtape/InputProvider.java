package org.opendrawer.nxtape;

public interface InputProvider extends DataProvider {
	public abstract int getInputChannelCount();

	public abstract void setInputChannels(float[] data);
}
