package org.opendrawer.ape.darwinianneurodynamics;

import java.util.ArrayList;
import java.util.List;

public class Ecosytem {
	private final List<StateStreamBundle> stateStreamBundles = new ArrayList<StateStreamBundle>();
	private final List<StateStream> allStateStreams = new ArrayList<StateStream>();
	private final List<OutputStatesProvider> outputStatesProviders = new ArrayList<OutputStatesProvider>();
	private final List<StatesProvider> inputOnlyStatesProviders = new ArrayList<StatesProvider>();
	private final List<Reflex> reflexes = new ArrayList<Reflex>();
	private final List<Actor> actors = new ArrayList<Actor>();
	private final List<Predictor> predictors = new ArrayList<Predictor>();

	public void prepareStatesStreams() {
		for (int i = 0; i < stateStreamBundles.size(); i++)
			allStateStreams.addAll(stateStreamBundles.get(i).getStateStreams());

		for (int i = 0; i < allStateStreams.size(); i++) {
			StateStream stateStream = allStateStreams.get(i);
			if (stateStream.getStatesProvider() instanceof OutputStatesProvider)
				if (!outputStatesProviders.contains(stateStream
						.getStatesProvider()))
					outputStatesProviders
							.add((OutputStatesProvider) stateStream
									.getStatesProvider());
		}
		for (int i = 0; i < allStateStreams.size(); i++) {
			StateStream stateStream = allStateStreams.get(i);
			if (!outputStatesProviders
					.contains(stateStream.getStatesProvider())) {
				if (!inputOnlyStatesProviders.contains(stateStream
						.getStatesProvider()))
					inputOnlyStatesProviders.add(stateStream
							.getStatesProvider());
			}
		}
	}

	public void addReflex(Reflex reflex) {
		reflexes.add(reflex);
	}

	public void addActor(Actor actor) {
		actors.add(actor);
	}

	public void addPredictor(Predictor predictor) {
		predictors.add(predictor);
	}

	public void addStatesStreamBundle(StateStreamBundle stateStreamBundle) {
		stateStreamBundles.add(stateStreamBundle);
	}

	public StateStream getStatesStreamByName(String name) {
		for (int i = 0; i < allStateStreams.size(); i++) {
			StateStream stateStream = allStateStreams.get(i);
			if (allStateStreams.get(i).getName().equalsIgnoreCase(name))
				return stateStream;
		}
		return null;
	}

	public void step() {
		stepInputs();
		stepOutputs();
	}

	private void stepInputs() {
		for (int i = 0; i < inputOnlyStatesProviders.size(); i++) {
			inputOnlyStatesProviders.get(i).updateStates();
			inputOnlyStatesProviders.get(i).notifyStatesObservers();
		}
	}

	private void stepOutputs() {
		for (int i = 0; i < outputStatesProviders.size(); i++) {
			int channels = outputStatesProviders.get(i).getStatesLength();
			for (int n = 0; n < channels; n++)
				outputStatesProviders.get(i).setOutputState(0, n);
		}

		for (int i = 0; i < reflexes.size(); i++)
			reflexes.get(i).react();

		for (int i = 0; i < outputStatesProviders.size(); i++) {
			outputStatesProviders.get(i).updateStates();
			outputStatesProviders.get(i).notifyStatesObservers();
		}

		for (int i = 0; i < predictors.size(); i++)
			predictors.get(i).predict();
	}

	public int getStatesStreamBundleCount() {
		return allStateStreams.size();
	}
}
