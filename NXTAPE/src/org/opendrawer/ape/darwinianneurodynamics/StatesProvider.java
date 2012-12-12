package org.opendrawer.ape.darwinianneurodynamics;

import java.util.ArrayList;
import java.util.List;

public abstract class StatesProvider {
	public static int GENERIC = 0;
	public static int INPUT = 1;
	public static int OUTPUT = 2;

	List<StatesObserver> statesObservers = new ArrayList<StatesObserver>();

	public void addStreamObserver(StatesObserver statesObserver) {
		statesObservers.add(statesObserver);
	}

	public void removeStatesObserver(StatesObserver statesObserver) {
		statesObservers.remove(statesObserver);
	}

	public void notifyStatesObservers() {
		for (int i = 0; i < statesObservers.size(); i++) {
			StatesObserver statesObserver = statesObservers.get(i);
			statesObserver.statesUpdated(this);
		}
	}

	public abstract void updateStates();

	public abstract String getName();

	public abstract double[] getStates();

	public abstract String[] getStateNames();

	public abstract int[] getStateTypes();

	public abstract int getStatesLength();
}