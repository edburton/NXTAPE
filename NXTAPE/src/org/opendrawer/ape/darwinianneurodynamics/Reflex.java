package org.opendrawer.ape.darwinianneurodynamics;

public abstract class Reflex extends StateStreamBundleGroup {

	protected int inputChannel;
	protected int outputChannel;
	protected OutputStatesProvider outputStatesProvider;

	public Reflex(StateStreamBundle inputStateStreamBundle,
			OutputStatesProvider outputStatesProvider, int inputStateIndex,
			int outputIndex) {
		super();
		addStateStreamBundle(inputStateStreamBundle);
		this.outputStatesProvider = outputStatesProvider;
		this.inputChannel = inputStateIndex;
		this.outputChannel = outputIndex;
		StateStream outputStream = new StateStream(outputStatesProvider,
				outputIndex, inputStateStreamBundle.getStreamLength());
		addStateStreamBundle(new StateStreamBundle(outputStream));
	}

	public abstract void react();
}