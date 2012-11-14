package org.opendrawer.nxtape;

public interface DataProvider {
	public abstract String getName();

	public abstract float[] getNormalizedValues();

	public abstract String[] getChannelNames();

	public abstract int getChannels();

	public abstract void step();
}