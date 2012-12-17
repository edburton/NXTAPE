package org.opendrawer.ape.darwinianneurodynamics;

public class OutputStateStream extends StateStream {

	public OutputStateStream(OutputStatesProvider statesProvider,
			int statesProviderChannel, int streamLength) {
		super(statesProvider, statesProviderChannel, streamLength);
	}

	public OutputStateStream() {

	}

	public OutputStateStream(int streamLength) {
		super(streamLength);

	}

	public void setStatesProvider(OutputStatesProvider statesProvider,
			int statesProviderChannel, int streamLength) {
		super.setStatesProvider(statesProvider, statesProviderChannel,
				streamLength);
	}

}
