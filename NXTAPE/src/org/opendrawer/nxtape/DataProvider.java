package org.opendrawer.nxtape;

public interface DataProvider {
	public abstract String getName();
	public abstract float[] getNormalizedValues();
	public abstract String [] getValueNames();
	public abstract void step();
}