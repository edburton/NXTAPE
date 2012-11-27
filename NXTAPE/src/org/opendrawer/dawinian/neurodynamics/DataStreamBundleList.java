package org.opendrawer.dawinian.neurodynamics;

import java.util.ArrayList;

public abstract class DataStreamBundleList {
	protected ArrayList<DataStreamBundle> dataStreamBundles = new ArrayList<DataStreamBundle>();

	public DataStreamBundleList(DataStreamBundle inputDataStreamBundle,
			DataStreamBundle outputDataStreamBundle) {
		dataStreamBundles.add(inputDataStreamBundle);
		dataStreamBundles.add(outputDataStreamBundle);
	}

	public DataStreamBundleList() {
	}

	public ArrayList<DataStreamBundle> getDataStreamBundles() {
		return dataStreamBundles;
	}

	public void addDataStreamBundle(DataStreamBundle dataStreamBundle) {
		dataStreamBundles.add(dataStreamBundle);
	}
}
