package org.opendrawer.dawinian.neurodynamics;

public abstract class DataStreamBundleMap {
	protected DataStreamBundle inputDataStreamBundle;
	protected DataStreamBundle outputDataStreamBundle;

	public DataStreamBundleMap(DataStreamBundle inputDataStreamBundle,
			DataStreamBundle outputDataStreamBundle) {
		this.inputDataStreamBundle = inputDataStreamBundle;
		this.outputDataStreamBundle = outputDataStreamBundle;
	}

	public void map() {

	}
}
