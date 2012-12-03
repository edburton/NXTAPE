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

	public Predictor(DataStreamBundle inputDataStreamBundle,
			DataStreamBundle outputDataStreamBundle) {
		super(inputDataStreamBundle, outputDataStreamBundle);
		this.inputDataStreamBundle = inputDataStreamBundle;
		this.outputDataStreamBundle = outputDataStreamBundle;
		predictionStreamBundle = new DataStreamBundle(
				inputDataStreamBundle.getDataWidth());
		predictionStreamBundle.AddDataStream(new DataStream(
				inputDataStreamBundle.getDataWidth()));
		addDataStreamBundle(predictionStreamBundle);
		errorStreamBundle = new DataStreamBundle(
				inputDataStreamBundle.getDataWidth());
		errorStreamBundle.AddDataStream(new DataStream(inputDataStreamBundle
				.getDataWidth()));
		addDataStreamBundle(errorStreamBundle);
	}

	public Predictor() {
		super();
		addDataStreamBundle(inputDataStreamBundle = new DataStreamBundle(512));
		addDataStreamBundle(outputDataStreamBundle = new DataStreamBundle(512));
		inputDataStreamBundle.AddDataStream(new DataStream(
				new TestingDataProvider(), 0, 512));
		inputDataStreamBundle.AddDataStream(new DataStream(
				new TestingDataProvider(), 1, 512));
		outputDataStreamBundle.AddDataStream(new DataStream(
				new TestingDataProvider(), 0, 512));
		outputDataStreamBundle.AddDataStream(new DataStream(
				new TestingDataProvider(), 1, 512));
		predictionStreamBundle = new DataStreamBundle(
				inputDataStreamBundle.getDataWidth());
		predictionStreamBundle.AddDataStream(new DataStream(
				inputDataStreamBundle.getDataWidth()));
		addDataStreamBundle(predictionStreamBundle);
		errorStreamBundle = new DataStreamBundle(
				inputDataStreamBundle.getDataWidth());
		errorStreamBundle.AddDataStream(new DataStream(inputDataStreamBundle
				.getDataWidth()));
		addDataStreamBundle(errorStreamBundle);
	}

	public void predict() {
		if (predictor == null)
			initiatePredictor();

		inputDataStreamBundle.write(inputDataStreamBundle.getDataStreams()
				.get(0).getDataProvider().getNormalizedValues());
		outputDataStreamBundle.write(outputDataStreamBundle.getDataStreams()
				.get(0).getDataProvider().getNormalizedValues());

		double[][] inputPeriod = { inputDataStreamBundle.read(0) };
		double[][] outputPeriod = { outputDataStreamBundle.read(0) };
		MLDataSet trainingSet = new BasicMLDataSet(inputPeriod, outputPeriod);
		MLDataPair currentPair = trainingSet.get(0);
		MLData prediction = predictorNetwork.compute(currentPair.getInput());
		predictionStreamBundle.write(prediction.getData());
		predictor.setTraining(trainingSet);
		predictor.iteration();
		double error = predictor.getError();
		errorStreamBundle.getDataStreams().get(0).write(error);
	}

	private void initiatePredictor() {
		double[][] inputPrototype = { inputDataStreamBundle.read(0) };
		double[][] outputPrototype = { outputDataStreamBundle.read(0) };

		predictorNetwork = new BasicNetwork();
		predictorNetwork.addLayer(new BasicLayer(null, true,
				inputPrototype.length));
		predictorNetwork.addLayer(new BasicLayer(new ActivationSigmoid(), true,
				inputPrototype.length + outputPrototype.length));
		predictorNetwork.addLayer(new BasicLayer(new ActivationSigmoid(),
				false, outputPrototype.length));
		predictorNetwork.getStructure().finalizeStructure();
		predictorNetwork.reset();

		MLDataSet prototypeTrainingSet = new BasicMLDataSet(inputPrototype,
				outputPrototype);

		predictor = new ResilientPropagation(predictorNetwork,
				prototypeTrainingSet);
	}
}
