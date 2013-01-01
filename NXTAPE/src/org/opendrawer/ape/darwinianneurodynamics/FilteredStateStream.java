package org.opendrawer.ape.darwinianneurodynamics;

public class FilteredStateStream extends StateStream {
	private final StateStream sourceStream;

	public FilteredStateStream(StateStream sourceStream) {
		super(sourceStream.getStreamLength());
		this.sourceStream = sourceStream;
		sourceStream.addStreamObserver(this);
	}

	@Override
	public void statesUpdated(StatesProvider statesProvider) {
		double[] states = sourceStream.read();
	}

}
