package org.opendrawer.ape.darwinianneurodynamics;

public class StateProviderStream extends StateStream {

	protected StatesProvider statesProvider;
	protected int statesProviderChannel;

	public StateProviderStream(StatesProvider statesProvider,
			int statesProviderChannel, int streamLength) {
		super(streamLength);
		setStatesProvider(statesProvider, statesProviderChannel, streamLength);
	}

	public void setStatesProvider(StatesProvider statesProvider,
			int statesProviderChannel, int streamLength) {
		statesProvider.addStreamObserver(this);
		this.streamLength = streamLength;
		this.statesProvider = statesProvider;
		this.statesProviderChannel = statesProviderChannel;
		stateStream = new double[streamLength];
		setToNaN();
	}

	public void setOutputState(double state) {
		statesProvider.setOutputState(state, statesProviderChannel);
	}

	@Override
	public void statesUpdated(StatesProvider statesProvider) {
		double[] states = statesProvider.getStates();
		if (states != null)
			write(states[statesProviderChannel]);
	}

	public StatesProvider getStatesProvider() {
		return statesProvider;
	}

	public int getStatesProviderChannel() {
		return statesProviderChannel;
	}

	@Override
	public void updateStates() {

	}

}
