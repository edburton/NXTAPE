package org.opendrawer.nxtape;

public interface DataProvider {
	public abstract String getName();

	public abstract float[] getNormalizedValues();

	public abstract String[] getChannelNames();

	public abstract int getChannelCount();

	public abstract void startStep();

	public abstract void finishStep();
}