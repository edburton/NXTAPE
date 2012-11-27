package org.opendrawer.dawinian.neurodynamics;

import org.encog.neural.networks.training.propagation.back.Backpropagation;

public class Predictor extends DataStreamBundleList {
	Backpropagation network;

	DataStreamBundle errorStreamBundle;

	public Predictor(DataStreamBundle inputDataStreamBundle,
			DataStreamBundle outputDataStreamBundle) {
		super(inputDataStreamBundle, outputDataStreamBundle);
		errorStreamBundle = new DataStreamBundle(
				inputDataStreamBundle.getDataWidth());
		addDataStreamBundle(errorStreamBundle);
	}
}
