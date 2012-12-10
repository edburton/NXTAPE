package org.opendrawer.ape.darwinianneurodynamics;

public class SensorimotorOutput extends HomogeneousStateStreamBundle {

	private final OutputStatesProvider outputStatesProvider;

	public SensorimotorOutput(OutputStatesProvider outputStatesProvider,
			int streamLength) {
		super(outputStatesProvider, streamLength);
		this.outputStatesProvider = outputStatesProvider;
	}

	public OutputStatesProvider getOutputStatesProvider() {
		return outputStatesProvider;
	}

}
