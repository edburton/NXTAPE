package org.opendrawer.dawinian.neurodynamics;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

public class Predictor extends DataStreamBundleList {
	BasicNetwork inputResponder;
	Backpropagation predictionTransform;

	DataStreamBundle predictionStreamBundle;
	DataStreamBundle errorStreamBundle;

	public Predictor(DataStreamBundle inputDataStreamBundle,
			DataStreamBundle outputDataStreamBundle) {
		super(inputDataStreamBundle, outputDataStreamBundle);
		predictionStreamBundle = new DataStreamBundle(
				inputDataStreamBundle.getDataWidth());
		addDataStreamBundle(predictionStreamBundle);
		errorStreamBundle = new DataStreamBundle(
				inputDataStreamBundle.getDataWidth());
		addDataStreamBundle(errorStreamBundle);
	}

	public void predict() {

	}
}
