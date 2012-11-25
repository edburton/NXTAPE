package org.opendrawer.dawinian.neurodynamics;

public interface OutputProvider extends DataProvider {
	public abstract void setOutputChannel(double data, int dataChannel);
}
