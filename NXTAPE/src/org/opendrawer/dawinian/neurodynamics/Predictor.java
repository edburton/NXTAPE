package org.opendrawer.dawinian.neurodynamics;

import org.encog.neural.networks.training.propagation.back.Backpropagation;

public class Predictor extends DataStreamBundleMap {
	Backpropagation network;

	public Predictor(DataStreamBundle inputDataStreamBundle,
			DataStreamBundle outputDataStreamBundle) {
		super(inputDataStreamBundle, outputDataStreamBundle);
	}
}
