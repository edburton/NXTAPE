package org.opendrawer.ape.darwinianneurodynamics;

import java.util.ArrayList;

public class StateStreamBundleGroupList {
	protected ArrayList<StateStreamBundleGroup> stateStreamBundleGroups = new ArrayList<StateStreamBundleGroup>();

	public StateStreamBundleGroupList(
			StateStreamBundleGroup inputStatesStreamBundle,
			StateStreamBundleGroup outputStatesStreamBundle) {
		if (inputStatesStreamBundle != null)
			stateStreamBundleGroups.add(inputStatesStreamBundle);
		if (outputStatesStreamBundle != null)
			stateStreamBundleGroups.add(outputStatesStreamBundle);
	}

	public StateStreamBundleGroupList() {
	}

	public ArrayList<StateStreamBundleGroup> getStatesStreamBundles() {
		return stateStreamBundleGroups;
	}

	public void addStateStreamBundle(StateStreamBundleGroup stateStreamBundle) {
		if (stateStreamBundle != null)
			stateStreamBundleGroups.add(stateStreamBundle);
	}
}
