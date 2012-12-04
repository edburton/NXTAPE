package org.opendrawer.ape.darwinianneurodynamics;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

public class Predictor extends DataStreamBundleList {
	BasicNetwork inputResponder;
	ResilientPropagation predictor;
	BasicNetwork predictorNetwork;

	DataStreamBundle inputDataStreamBundle;
	DataStreamBundle outputDataStreamBundle;

	DataStreamBundle predictionStreamBundle;
	DataStreamBundle errorStreamBundle;

	private static final int dataWidth = 20;

	public Predictor(DataStreamBundle inputDataStreamBundle,
			DataStreamBundle outputDataStreamBundle) {
		super(inputDataStreamBundle, outputDataStreamBundle);
		this.inputDataStreamBundle = inputDataStreamBundle;
		this.outputDataStreamBundle = outputDataStreamBundle;
		predictionStreamBundle = new DataStreamBundle(dataWidth);
		predictionStreamBundle.addEmptyDataStreams(outputDataStreamBundle
				.getDataStreams().size());
		addDataStreamBundle(predictionStreamBundle);
		errorStreamBundle = new DataStreamBundle(
				inputDataStreamBundle.getDataWidth());
		errorStreamBundle.addEmptyDataStreams(1);
		addDataStreamBundle(errorStreamBundle);
	}

	public Predictor() { // Constructor used for testing only
		super();
		addDataStreamBundle(inputDataStreamBundle = new HomogeneousDataStreamBundle(
				new TestingDataProvider(1), 100));
		addDataStreamBundle(outputDataStreamBundle = new HomogeneousDataStreamBundle(
				new TestingDataProvider(-1), 100));
		predictionStreamBundle = new DataStreamBundle(
				inputDataStreamBundle.getDataWidth());
		predictionStreamBundle.addEmptyDataStreams(outputDataStreamBundle
				.getDataStreams().size());
		addDataStreamBundle(predictionStreamBundle);
		errorStreamBundle = new DataStreamBundle(
				inputDataStreamBundle.getDataWidth());
		errorStreamBundle.addEmptyDataStreams(1);
		addDataStreamBundle(errorStreamBundle);
	}

	public void predict() {
		// double[] input = inputDataStreamBundle.read(0);
		double[] output = outputDataStreamBundle.read(0);
		predictionStreamBundle.write(output);
		errorStreamBundle.write(new double[] { Math.cos(System
				.currentTimeMillis() / 600.0d) });
	}

	public void predict_backup() {
		if (predictor == null)
			initiatePredictor();
		double[][] inputPeriod = inputDataStreamBundle
				.readPortion(0, dataWidth);
		double[][] outputPeriod = outputDataStreamBundle.readPortion(0,
				dataWidth);
		MLDataSet trainingSet = new BasicMLDataSet(inputPeriod, outputPeriod);
		MLDataPair currentPair = trainingSet.get(0);
		MLData prediction = predictorNetwork.compute(currentPair.getInput());
		predictionStreamBundle.write(prediction.getData());
		predictor.setTraining(trainingSet);
		predictor.iteration();
		double error = predictor.getError();
		// System.out.println(error);
		errorStreamBundle.getDataStreams().get(0).write(error);
	}

	private void initiatePredictor() {
		double[][] inputPrototype = new double[][] { inputDataStreamBundle
				.read(0) };
		double[][] outputPrototype = new double[][] { outputDataStreamBundle
				.read(0) };
		predictorNetwork = new BasicNetwork();
		predictorNetwork.addLayer(new BasicLayer(null, true,
				inputDataStreamBundle.getDataStreams().size()));
		predictorNetwork.addLayer(new BasicLayer(new ActivationSigmoid(), true,
				inputDataStreamBundle.getDataStreams().size()
						+ outputDataStreamBundle.getDataStreams().size()));
		predictorNetwork.addLayer(new BasicLayer(new ActivationSigmoid(),
				false, outputDataStreamBundle.getDataStreams().size()));
		predictorNetwork.getStructure().finalizeStructure();
		predictorNetwork.reset();

		MLDataSet prototypeTrainingSet = new BasicMLDataSet(inputPrototype,
				outputPrototype);

		predictor = new ResilientPropagation(predictorNetwork,
				prototypeTrainingSet);
	}
}
