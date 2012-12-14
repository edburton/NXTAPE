package org.opendrawer.ape.darwinianneurodynamics;

import java.util.ArrayList;
import java.util.List;

public class Ecosystem {
	private final List<SensorimotorBundle> sensorimotorBundles = new ArrayList<SensorimotorBundle>();
	private final List<SensorimotorInput> inputs = new ArrayList<SensorimotorInput>();
	private final List<SensorimotorOutput> outputs = new ArrayList<SensorimotorOutput>();
	private final List<Reflex> reflexes = new ArrayList<Reflex>();
	private final List<CuriosityLoop> curiosityLoops = new ArrayList<CuriosityLoop>();
	private final List<Actor> actors = new ArrayList<Actor>();
	private final List<Predictor> predictors = new ArrayList<Predictor>();
	private Error allErros;
	private final HomogeneousStateStreamBundle allErrors;
	private final int statesStreamLength;

	public Ecosystem(int statesStreamLength) {
		this.statesStreamLength = statesStreamLength;
		allErrors = new HomogeneousStateStreamBundle(allErros,
				statesStreamLength);
	}

	public SensorimotorInput makeInput(StatesProvider statesProvider) {
		SensorimotorInput input = new SensorimotorInput(statesProvider,
				statesStreamLength);
		inputs.add(input);
		sensorimotorBundles.add(input);
		return input;
	}

	public SensorimotorOutput makeOutput(OutputStatesProvider statesProvider) {
		SensorimotorOutput output = new SensorimotorOutput(statesProvider,
				statesStreamLength);
		outputs.add(output);
		sensorimotorBundles.add(output);
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
		allErrors.addCompressingStatesProviderStreams(predictor.getError());
	}

	public void addCuriosityLoop(CuriosityLoop curiosityLoop) {
		curiosityLoops.add(curiosityLoop);
		allErrors.addCompressingStatesProviderStreams(curiosityLoop
				.getPredictor().getError());
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

		for (int i = 0; i < curiosityLoops.size(); i++)
			curiosityLoops.get(i).step();
	}

	private List<StateStreamBundle> getRandomUniqueStateStreamBundles(
			List<StateStream> stateStreams, int n, int min, int max) {
		List<StateStreamBundle> result = new ArrayList<StateStreamBundle>();

		for (int i = 0; i < n; i++) {
			int streams = Util.RandomInt(min, max);
			int[] streamIndexes = new int[streams];
			for (int s = 0; s < streams; s++)
				streamIndexes[s] = -1;

			for (int s = 0; s < streams; s++) {
				boolean original = false;
				int p = -1;
				while (!original) {
					original = true;
					p = Util.RandomInt(0, stateStreams.size() - 1);
					for (int ss = 0; ss < streams; ss++)
						if (p == streamIndexes[ss])
							original = false;
				}
				streamIndexes[s] = p;
			}

			StateStreamBundle stateStreamBundle = new StateStreamBundle(
					statesStreamLength);

			for (int c = 0; c < streams; c++) {
				StateStream stream = stateStreams.get(streamIndexes[c]);
				stateStreamBundle.addStateStream(stream);
			}
			result.add(stateStreamBundle);
		}
		return result;
	}

	public List<StateStreamBundle> getRandomUniqueSensorimotorStateStreamBundles(
			int types, int n, int min, int max) {
		List<StateStream> stateStreams = new ArrayList<StateStream>();
		for (int i = 0; i < sensorimotorBundles.size(); i++) {
			HomogeneousStateStreamBundle sensorimotorBundle = sensorimotorBundles
					.get(i);
			for (int c = 0; c < sensorimotorBundle.getStateStreams().size(); c++) {
				StateStream stateStream = sensorimotorBundle.getStateStreams()
						.get(c);
				for (int cc = 0; cc < stateStream.getStatesProvider()
						.getStateTypes().length; cc++) {
					if ((types | stateStream.getStatesProvider()
							.getStateTypes()[cc]) != 0)
						stateStreams.add(stateStream);
				}
			}
		}
		return getRandomUniqueStateStreamBundles(stateStreams, n, min, max);
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

	public StateStreamBundle getAllErrors() {
		return allErrors;
	}

	public List<CuriosityLoop> getCuriosityLoops() {
		return curiosityLoops;
	}

	public List<SensorimotorBundle> getSensorimotorBundles() {
		return sensorimotorBundles;
	}
}
