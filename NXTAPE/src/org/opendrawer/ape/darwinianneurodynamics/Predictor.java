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
		predictionStreamBundle.addDataStream(new DataStream(
				inputDataStreamBundle.getDataWidth()));
		addDataStreamBundle(predictionStreamBundle);
		errorStreamBundle = new DataStreamBundle(
				inputDataStreamBundle.getDataWidth());
		errorStreamBundle.addDataStream(new DataStream(inputDataStreamBundle
				.getDataWidth()));
		addDataStreamBundle(errorStreamBundle);
	}

	public Predictor() {
		super();
		addDataStreamBundle(inputDataStreamBundle = new DataStreamBundle(100));
		addDataStreamBundle(outputDataStreamBundle = new DataStreamBundle(100));
		inputDataStreamBundle
				.addDataProvidedStreams(new TestingDataProvider(1));
		outputDataStreamBundle
				.addDataProvidedStreams(new TestingDataProvider(1));

		predictionStreamBundle = new DataStreamBundle(
				inputDataStreamBundle.getDataWidth());
		predictionStreamBundle.addEmptyDataStreams(outputDataStreamBundle
				.getDataStreams().size());
		addDataStreamBundle(predictionStreamBundle);
		errorStreamBundle = new DataStreamBundle(
				inputDataStreamBundle.getDataWidth());
		errorStreamBundle.addDataStream(new DataStream(inputDataStreamBundle
				.getDataWidth()));
		addDataStreamBundle(errorStreamBundle);
	}

	public void predict() {
		if (predictor == null)
			initiatePredictor();

		for (int i = 0; i < inputDataStreamBundle.getDataStreams().size(); i++) {
			inputDataStreamBundle.getDataStreams().get(i).getDataProvider()
					.step();
			inputDataStreamBundle.write(inputDataStreamBundle.getDataStreams()
					.get(i).getDataProvider().getData());
		}
		for (int i = 0; i < outputDataStreamBundle.getDataStreams().size(); i++) {
			outputDataStreamBundle.getDataStreams().get(i).getDataProvider()
					.step();
			outputDataStreamBundle.write(outputDataStreamBundle
					.getDataStreams().get(i).getDataProvider().getData());
		}
		double[][] inputPeriod = inputDataStreamBundle.read();
		double[][] outputPeriod = outputDataStreamBundle.read();
		MLDataSet trainingSet = new BasicMLDataSet(inputPeriod, outputPeriod);
		MLDataPair currentPair = trainingSet.get(0);
		MLData prediction = predictorNetwork.compute(currentPair.getInput());
		predictionStreamBundle.write(prediction.getData());
		predictor.setTraining(trainingSet);
		predictor.iteration();
		double error = predictor.getError();
		System.out.println(error);
		errorStreamBundle.getDataStreams().get(0).write(error);
	}

	private void initiatePredictor() {
		double[][] inputPrototype = inputDataStreamBundle.read();
		double[][] outputPrototype = outputDataStreamBundle.read();

		predictorNetwork = new BasicNetwork();
		predictorNetwork.addLayer(new BasicLayer(null, true,
				inputPrototype[0].length));
		predictorNetwork.addLayer(new BasicLayer(new ActivationSigmoid(), true,
				inputPrototype.length + outputPrototype.length));
		predictorNetwork.addLayer(new BasicLayer(new ActivationSigmoid(),
				false, inputPrototype[0].length));
		predictorNetwork.getStructure().finalizeStructure();
		predictorNetwork.reset();

		MLDataSet prototypeTrainingSet = new BasicMLDataSet(inputPrototype,
				outputPrototype);

		predictor = new ResilientPropagation(predictorNetwork,
				prototypeTrainingSet);
	}
}
