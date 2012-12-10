package org.opendrawer.ape.darwinianneurodynamics;

import java.util.ArrayList;
import java.util.List;

public class Ecosystem {
	private final List<SensorimotorInput> inputs = new ArrayList<SensorimotorInput>();
	private final List<SensorimotorOutput> outputs = new ArrayList<SensorimotorOutput>();
	private final List<Reflex> reflexes = new ArrayList<Reflex>();
	private final List<Actor> actors = new ArrayList<Actor>();
	private final List<Predictor> predictors = new ArrayList<Predictor>();
	private final int statesStreamLength;

	public Ecosystem(int statesStreamLength) {
		this.statesStreamLength = statesStreamLength;
	}

	public SensorimotorInput makeInput(StatesProvider statesProvider) {
		SensorimotorInput input = new SensorimotorInput(statesProvider,
				statesStreamLength);
		inputs.add(input);
		return input;
	}

	public SensorimotorOutput makeOutput(OutputStatesProvider statesProvider) {
		SensorimotorOutput output = new SensorimotorOutput(statesProvider,
				statesStreamLength);
		outputs.add(output);
		return output;
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

	public void step() {
		stepInputs();
		stepOutputs();
	}

	private void stepInputs() {
		for (int i = 0; i < inputs.size(); i++) {
			inputs.get(i).getStateProvider().updateStates();
			inputs.get(i).getStateProvider().notifyStatesObservers();
		}
	}

	private void stepOutputs() {
		for (int i = 0; i < outputs.size(); i++) {
			int channels = outputs.get(i).getOutputStatesProvider()
					.getStatesLength();
			for (int n = 0; n < channels; n++)
				outputs.get(i).getOutputStatesProvider().setOutputState(0, n);
		}

		for (int i = 0; i < reflexes.size(); i++)
			reflexes.get(i).react();

		for (int i = 0; i < outputs.size(); i++) {
			outputs.get(i).getOutputStatesProvider().updateStates();
			outputs.get(i).getOutputStatesProvider().notifyStatesObservers();
		}

		for (int i = 0; i < predictors.size(); i++)
			predictors.get(i).predict();
	}

	public List<SensorimotorInput> getInputs() {
		return inputs;
	}

	public List<SensorimotorOutput> getOutputs() {
		return outputs;
	}

	public List<Actor> getActors() {
		return actors;
	}

	public List<Reflex> getReflexes() {
		return reflexes;
	}

	public List<Predictor> getPredictors() {
		return predictors;
	}

	public int getStatesStreamLength() {
		return statesStreamLength;
	}
}
