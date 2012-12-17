package org.opendrawer.ape.darwinianneurodynamics;

import java.util.ArrayList;
import java.util.List;

public abstract class StatesProvider {
	public static int GENERIC = 2;
	public static int INPUT = 4;
	public static int OUTPUT = 8;

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

	public abstract double[] getStates();

	public abstract int[] getStateTypes();

	public abstract int getStatesLength();
}