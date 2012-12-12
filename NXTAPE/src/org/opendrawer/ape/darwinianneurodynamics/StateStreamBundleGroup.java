package org.opendrawer.ape.darwinianneurodynamics;

import java.util.ArrayList;

public abstract class StateStreamBundleGroup {
	protected ArrayList<StateStreamBundle> stateStreamBundles = new ArrayList<StateStreamBundle>();

	public StateStreamBundleGroup(StateStreamBundle inputStatesStreamBundle,
			StateStreamBundle outputStatesStreamBundle) {
		if (inputStatesStreamBundle != null)
			stateStreamBundles.add(inputStatesStreamBundle);
		if (outputStatesStreamBundle != null)
			stateStreamBundles.add(outputStatesStreamBundle);
	}

	public StateStreamBundleGroup() {
	}

	public ArrayList<StateStreamBundle> getStatesStreamBundles() {
		return stateStreamBundles;
	}

	public void addStateStreamBundle(StateStreamBundle stateStreamBundle) {
		if (stateStreamBundle != null)
			stateStreamBundles.add(stateStreamBundle);
	}
}
