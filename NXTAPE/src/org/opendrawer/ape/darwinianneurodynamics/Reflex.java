package org.opendrawer.ape.darwinianneurodynamics;

public abstract class Reflex extends StateStreamBundleGroup {

	protected int inputChannel;
	protected int outputChannel;
	protected OutputStatesProvider outputStatesProvider;

	public Reflex(StateStreamBundle inputStateStreamBundle,
			HomogeneousStateStreamBundle outputStateStreamBundle,
			int inputStateIndex, int outputIndex) {
		super(inputStateStreamBundle, outputStateStreamBundle);
		this.inputChannel = inputStateIndex;
		this.outputChannel = outputIndex;
		if (outputStateStreamBundle != null)
			outputStatesProvider = (OutputStatesProvider) outputStateStreamBundle
					.getStateProvider();
	}

	public abstract void react();
}
