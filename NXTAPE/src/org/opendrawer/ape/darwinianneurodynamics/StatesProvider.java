package org.opendrawer.ape.darwinianneurodynamics;

import java.util.ArrayList;
import java.util.List;

public abstract class StatesProvider {
	public static final int NULL = 0;
	public static final int INPUT = 1;
	public static final int OUTPUT = 2;
	public static final int INTERNAL = 3;

	List<StatesObserver> statesObservers = new ArrayList<StatesObserver>();

	public void addStreamObserver(StatesObserver statesObserver) {
		statesObservers.add(statesObserver);
	}

	public void removeStatesObserver(StatesObserver statesObserver) {
		statesObservers.remove(statesObserver);
	}

	public void notifyStateObservers() {
		for (int i = 0; i < statesObservers.size(); i++)
			;

	}

	public abstract String getName();

	public abstract double[] getStates();

	public abstract String[] getStateNames();

	public abstract int[] getStateTypes();

	public abstract int getStatesLength();

	public abstract void updateStates();
}