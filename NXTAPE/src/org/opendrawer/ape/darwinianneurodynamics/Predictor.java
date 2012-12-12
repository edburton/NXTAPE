package org.opendrawer.ape.darwinianneurodynamics;

public class Predictor extends Actor {

	StateStreamBundle inputStateStreamBundle;
	StateStreamBundle outputStateStreamBundle;

	HomogeneousStateStreamBundle predictionStreamBundle;
	HomogeneousStateStreamBundle errorStreamBundle;

	Prediction prediction;
	Error error;

	private int streamLength;

	int inputLength;
	int outputLength;
	double weightsMatrix[][];
	double learningRate = 0.001;

	private final int counter = 0;

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

		inputLength = inputStateStreamBundle.getStateStreams().size();
		outputLength = outputStateStreamBundle.getStateStreams().size();
		weightsMatrix = new double[inputLength + 1][outputLength];
		for (int i = 0; i < inputLength + 1; i++)
			for (int o = 0; o < outputLength; o++) {
				weightsMatrix[i][o] = (Math.random() * 2) - 1;
				// if (i - 1 == o)
				// weightsMatrix[i][o] = 1;
				// else
				// weightsMatrix[i][o] = 0;
			}

		System.out.println("");
		for (int i = 0; i < inputLength + 1; i++) {
			System.out.println("");
			for (int o = 0; o < outputLength; o++) {
				System.out.print("[" + weightsMatrix[i][o] + "]");
			}
		}
		System.out.println("");

		error = new Error();
		errorStreamBundle = new HomogeneousStateStreamBundle(error, 1000);
		addStateStreamBundle(errorStreamBundle);
	}

	public void predict() {

		double[][] inputPeriod = inputStateStreamBundle.readPortion(0, 1);
		double[][] outputPeriod = outputStateStreamBundle.readPortion(0, 1);

		double input[] = new double[inputLength + 1];
		input[0] = 1;
		for (int i = 0; i < inputLength; i++)
			if (!Double.isNaN(inputPeriod[0][i]))
				input[i + 1] = inputPeriod[0][i];

		double output[] = new double[outputLength];
		for (int i = 0; i < outputLength; i++)
			if (!Double.isNaN(outputPeriod[0][i]))
				output[i] = outputPeriod[0][i];

		double resultingPrediction[] = new double[outputLength];

		for (int i = 0; i < inputLength + 1; i++)
			for (int o = 0; o < outputLength; o++)
				resultingPrediction[o] += input[i] * weightsMatrix[i][o];

		for (int i = 0; i < outputLength; i++)
			prediction.setOutputState(resultingPrediction[i], i);

		double errorValue = 0;
		for (int i = 0; i < inputLength + 1; i++)
			for (int o = 0; o < outputLength; o++) {
				weightsMatrix[i][o] = weightsMatrix[i][o] - learningRate
						* ((resultingPrediction[o] - output[o]) * input[i]);
				errorValue += Math.pow(resultingPrediction[o] - output[o], 2);
			}
		errorValue = Math.sqrt(errorValue) / (inputLength + 1);

		error.setOutputState(errorValue, 0);
		prediction.notifyStatesObservers();
		error.notifyStatesObservers();

		// if (++counter % 100 == 0) {
		// System.out.println("");
		// for (int i = 0; i < inputLength + 1; i++) {
		// System.out.println("");
		// for (int o = 0; o < outputLength; o++) {
		// System.out.print("[" + weightsMatrix[i][o] + "]");
		// }
		// }
		// System.out.println("");
		// }
	}

	public Error getError() {
		return error;
	}
}
