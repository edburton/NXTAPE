package org.opendrawer.ape.darwinianneurodynamics;

public class HomogeneousStateStreamBundle extends StateStreamBundle {

	private StatesProvider statesProvider;

	public HomogeneousStateStreamBundle(StatesProvider statesProvider,
			int streamLength) {
		super(streamLength);
		this.statesProvider = statesProvider;
		if (statesProvider == null)
			return;
		int channels = statesProvider.getStatesLength();
		for (int i = 0; i < channels; i++)
			stateStreams.add(new StateStream(statesProvider, i, streamLength));
	}

	public StatesProvider getStateProvider() {
		return statesProvider;
	}

}
