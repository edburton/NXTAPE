package org.opendrawer.ape.darwinianneurodynamics;

import java.util.ArrayList;
import java.util.List;

public class StateStreamBundle {
	protected List<StateStream> stateStreams = new ArrayList<StateStream>();
	protected int streamLength;

	public StateStreamBundle(int streamLength) {
		this.streamLength = streamLength;
	}

	public void write(double[] states) {
		for (int i = 0; i < stateStreams.size() && i < states.length; i++)
			stateStreams.get(i).write(states[i]);
	}

	public double[] read(int pastPosition) {
		double[] states = new double[stateStreams.size()];
		for (int i = 0; i < stateStreams.size(); i++)
			states[i] = read(pastPosition, i);
		return states;
	}

	public double[][] read() {
		return readPortion(0, streamLength);
	}

	public double[][] readPortion(int from, int to) {
		double[][] stateStream = new double[to - from][stateStreams.size()];
		for (int d = 0; d < stateStreams.size(); d++)
			for (int t = 0; t < to - from; t++)
				stateStream[t][d] = read(from + t, d);
		return stateStream;
	}

	public double read(int pastPosition, int channel) {
		return stateStreams.get(channel).read(pastPosition);
	}

	public int getStreamLength() {
		return streamLength;
	}

	public List<StateStream> getStateStreams() {
		return stateStreams;
	}

	public void addStatesStream(StateStream stateStream) {
		stateStreams.add(stateStream);
	}

	public void addStatesProviderStreams(StatesProvider statesProvider) {
		for (int i = 0; i < statesProvider.getStatesLength(); i++)
			addStatesStream(new StateStream(statesProvider, i, streamLength));
	}

	public void addEmptyStateStreams(int n) {
		for (int i = 0; i < n; i++)
			addStatesStream(new StateStream(streamLength));
	}

	public void removeStatesStream(StateStream stateStream) {
		stateStreams.remove(stateStream);
	}
}
