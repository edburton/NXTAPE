package org.opendrawer.dawinian.neurodynamics;

public interface OutputDataProvider extends DataProvider {
	public abstract void setOutputChannel(double data, int dataChannel);
}
