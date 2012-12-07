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
	HomogeneousStateStreamBundle errorStreamBundle;

	Prediction prediction;
	Error error;

	private int streamLength = 20;

	public Predictor(StateStreamBundle inputStateStreamBundle,
			StateStreamBundle outputStateStreamBundle) {
		super(inputStateStreamBundle, outputStateStreamBundle);
		this.inputStateStreamBundle = inputStateStreamBundle;
		this.outputStateStreamBundle = outputStateStreamBundle;
		streamLength = inputStateStreamBundle.getStreamLength();
		if (outputStateStreamBundle == null)
			return;
		prediction = new Prediction(outputStateStreamBundle.getStateStreams()
				.size());
		predictionStreamBundle = new HomogeneousStateStreamBundle(prediction,
				streamLength);
		addStateStreamBundle(predictionStreamBundle);

		error = new Error();
		errorStreamBundle = new HomogeneousStateStreamBundle(error,
				streamLength);
		addStateStreamBundle(errorStreamBundle);
	}

	public void predict() {
		if (predictor == null)
			initiatePredictor();
		double[][] inputPeriod = inputStateStreamBundle.readPortion(0, 1);
		double[][] outputPeriod = outputStateStreamBundle.readPortion(0, 1);
		MLDataSet trainingSet = new BasicMLDataSet(inputPeriod, outputPeriod);
		MLDataPair currentPair = trainingSet.get(0);
		MLData predictionResult = predictorNetwork.compute(currentPair
				.getInput());
		for (int i = 0; i < prediction.getStatesLength(); i++)
			prediction.setOutputState(predictionResult.getData()[i], i);
		predictor.setTraining(trainingSet);
		predictor.iteration();
		double errorValue = predictor.getError();
		error.setOutputState(errorValue, 0);
		prediction.notifyStatesObservers();
		error.notifyStatesObservers();
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
