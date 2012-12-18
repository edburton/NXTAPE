package org.opendrawer.ape.darwinianneurodynamics;

public class Actor extends StateStreamBundleGroup {

	protected StateStreamBundle inputStateStreamBundle;
	protected StateStreamBundle outputStateStreamBundle;
	protected int streamLength;
	protected int inputLength;
	protected int outputLength;
	protected double inputToOutputWeightsMatrix[][];
	protected StatesStore statesStore;

	public Actor(StateStreamBundle inputStateStreamBundle,
			StateStreamBundle outputStateStreamBundle) {
		super(inputStateStreamBundle, outputStateStreamBundle);
		this.inputStateStreamBundle = inputStateStreamBundle;
		this.outputStateStreamBundle = outputStateStreamBundle;
		streamLength = inputStateStreamBundle.getStreamLength();

		inputLength = inputStateStreamBundle.getStateStreams().size();
		outputLength = outputStateStreamBundle.getStateStreams().size();
		statesStore = new StatesStore(outputLength);
		inputToOutputWeightsMatrix = new double[inputLength + 1][outputLength];
		for (int i = 0; i < inputLength + 1; i++)
			for (int o = 0; o < outputLength; o++)
				inputToOutputWeightsMatrix[i][o] = (Math.random() * 2) - 1;
	}

	public void step() {
		processWeightsMatrix();
	}

	public void processWeightsMatrix() {

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

		outputStateStreamBundle.setStates(statesStore.getStates());

		statesStore.notifyStatesObservers();
	}
}
