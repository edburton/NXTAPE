package org.opendrawer.ape.darwinianneurodynamics;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

public class Predictor extends StateStreamBundleGroup {
	BasicNetwork inputResponder;
	ResilientPropagation predictor;
	BasicNetwork predictorNetwork;

	StateStreamBundle inputStateStreamBundle;
	StateStreamBundle outputStateStreamBundle;

	HomogeneousStateStreamBundle predictionStreamBundle;
	StateStreamBundle errorStreamBundle;

	Prediction prediction;

	private static final int streamLength = 20;

	public Predictor(StateStreamBundle inputStateStreamBundle,
			StateStreamBundle outputStateStreamBundle) {
		super(inputStateStreamBundle, outputStateStreamBundle);
		this.inputStateStreamBundle = inputStateStreamBundle;
		this.outputStateStreamBundle = outputStateStreamBundle;
		if (outputStateStreamBundle == null)
			return;
		prediction = new Prediction(outputStateStreamBundle.getStateStreams()
				.size());
		predictionStreamBundle = new HomogeneousStateStreamBundle(prediction,
				streamLength);
		addStateStreamBundle(predictionStreamBundle);
		errorStreamBundle = new StateStreamBundle(
				inputStateStreamBundle.getStreamLength());
		errorStreamBundle.addEmptyStateStreams(1);
		addStateStreamBundle(errorStreamBundle);
	}

	public Predictor() { // Constructor used for testing only
		super();
		addStateStreamBundle(inputStateStreamBundle = new HomogeneousStateStreamBundle(
				new TestingStatesProvider(1), 200));
		addStateStreamBundle(outputStateStreamBundle = new HomogeneousStateStreamBundle(
				new TestingStatesProvider(-1), 200));
		prediction = new Prediction(outputStateStreamBundle.getStateStreams()
				.size());
		predictionStreamBundle = new HomogeneousStateStreamBundle(prediction,
				streamLength);
		addStateStreamBundle(predictionStreamBundle);
		errorStreamBundle = new StateStreamBundle(
				inputStateStreamBundle.getStreamLength());
		errorStreamBundle.addEmptyStateStreams(1);
		addStateStreamBundle(errorStreamBundle);
	}

	public void predict_placeholder() {
		// double[] input = inputStateStreamBundle.read(0);

		double[] output = outputStateStreamBundle.read(0);
		predictionStreamBundle.write(output);
		errorStreamBundle.write(new double[] { Math.pow(Math.random(), 16) });
	}

	public void predict() {
		if (predictor == null)
			initiatePredictor();
		double[][] inputPeriod = inputStateStreamBundle.readPortion(0,
				streamLength);
		double[][] outputPeriod = outputStateStreamBundle.readPortion(0,
				streamLength);
		MLDataSet trainingSet = new BasicMLDataSet(inputPeriod, outputPeriod);
		MLDataPair currentPair = trainingSet.get(0);
		MLData prediction = predictorNetwork.compute(currentPair.getInput());
		predictionStreamBundle.write(prediction.getData());
		predictor.setTraining(trainingSet);
		predictor.iteration();
		double error = predictor.getError();
		// System.out.println(error);
		errorStreamBundle.getStateStreams().get(0).write(error);
	}

	private void initiatePredictor() {
		double[][] inputPrototype = new double[][] { inputStateStreamBundle
				.read(0) };
		double[][] outputPrototype = new double[][] { outputStateStreamBundle
				.read(0) };
		predictorNetwork = new BasicNetwork();
		predictorNetwork.addLayer(new BasicLayer(null, true,
				inputStateStreamBundle.getStateStreams().size()));
		predictorNetwork.addLayer(new BasicLayer(new ActivationSigmoid(), true,
				inputStateStreamBundle.getStateStreams().size()
						+ outputStateStreamBundle.getStateStreams().size()));
		predictorNetwork.addLayer(new BasicLayer(new ActivationSigmoid(),
				false, outputStateStreamBundle.getStateStreams().size()));
		predictorNetwork.getStructure().finalizeStructure();
		predictorNetwork.reset();

		MLDataSet prototypeTrainingSet = new BasicMLDataSet(inputPrototype,
				outputPrototype);

		predictor = new ResilientPropagation(predictorNetwork,
				prototypeTrainingSet);
	}
}
