package org.opendrawer.dawinian.neurodynamics;

public interface DataProvider {
	public abstract String getName();

	public abstract double[] getNormalizedValues();

	public abstract String[] getChannelNames();

	public abstract int getChannelCount();

	public abstract void startStep();

	public abstract void finishStep();

	public abstract void setInhihited(boolean inhibited);
}