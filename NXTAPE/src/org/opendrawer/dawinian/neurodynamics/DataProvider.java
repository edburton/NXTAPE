package org.opendrawer.dawinian.neurodynamics;

public interface DataProvider {
	public static final int NULL = 0;
	public static final int INPUT = 1;
	public static final int OUTPUT = 2;

	public abstract String getName();

	public abstract double[] getNormalizedValues();

	public abstract String[] getChannelNames();

	public abstract int[] getChannelTypes();

	public abstract int getChannelCount();

	public abstract void step();

	public abstract void setInhihited(boolean inhibited);
}