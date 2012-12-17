package org.opendrawer.ape.darwinianneurodynamics;

public class Predictor extends Actor {

	HomogeneousStateStreamBundle predictionStreamBundle;
	StateStreamBundle errorStreamBundle;

	Error error;

	double learningRate = 0.001;

	public Predictor(StateStreamBundle inputStateStreamBundle,
			StateStreamBundle outputStateStreamBundle) {
		super(inputStateStreamBundle, outputStateStreamBundle);

		if (outputStateStreamBundle == null)
			return;
		statesStore = new StatesStore(outputStateStreamBundle.getStateStreams()
				.size());
		predictionStreamBundle = new HomogeneousStateStreamBundle(statesStore,
				streamLength);
		addStateStreamBundle(predictionStreamBundle);

		// System.out.println("");
		// for (int i = 0; i < inputLength + 1; i++) {
		// System.out.println("");
		// for (int o = 0; o < outputLength; o++) {
		// System.out.print("[" + inputToOutputWeightsMatrix[i][o] + "]");
		// }
		// }
		// System.out.println("");

		error = new Error();
		errorStreamBundle = new StateStreamBundle(
				inputStateStreamBundle.getStreamLength() * 2);
		errorStreamBundle.addCompressingStatesProviderStreams(error);
		addStateStreamBundle(errorStreamBundle);
	}

	@Override
	public void step() {

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
				resultingPrediction[o] += input[i]
						* inputToOutputWeightsMatrix[i][o];

		for (int i = 0; i < outputLength; i++)
			statesStore.setOutputState(resultingPrediction[i], i);

		double errorValue = 0;
		for (int i = 0; i < inputLength + 1; i++)
			for (int o = 0; o < outputLength; o++) {
				inputToOutputWeightsMatrix[i][o] = inputToOutputWeightsMatrix[i][o]
						- learningRate
						* ((resultingPrediction[o] - output[o]) * input[i]);
				errorValue += Math.pow(resultingPrediction[o] - output[o], 2);
			}
		errorValue = (Math.sqrt(errorValue) / (inputLength + 1)) * 0.70710678118655;

		error.setOutputState(errorValue, 0);
		statesStore.notifyStatesObservers();
		error.notifyStatesObservers();
	}

	public Error getError() {
		return error;
	}
}
