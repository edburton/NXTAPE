package org.opendrawer.ape.darwinianneurodynamics;

public class HomogeneousStateStreamBundle extends StateStreamBundle {

	private StatesProvider statesProvider;

	public HomogeneousStateStreamBundle(StatesProvider statesProvider,
			int streamLength) {
		super(streamLength);
		this.statesProvider = statesProvider;
		if (statesProvider == null)
			return;
		makeStatesStreamsFromProvider();
	}

	public StatesProvider getStatesProvider() {
		return statesProvider;
	}

	private void makeStatesStreamsFromProvider() {
		stateStreams.clear();
		int channels = statesProvider.getStatesLength();
		for (int i = 0; i < channels; i++)
			addStateStream(new StateStream(statesProvider, i, streamLength));
	}
}
