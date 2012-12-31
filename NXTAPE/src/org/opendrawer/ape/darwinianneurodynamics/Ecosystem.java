package org.opendrawer.ape.darwinianneurodynamics;

import java.util.ArrayList;
import java.util.List;

public class Ecosystem {
	private final List<SensorimotorBundle> sensorimotorBundles = new ArrayList<SensorimotorBundle>();
	private final List<SensorimotorBundle> inputs = new ArrayList<SensorimotorBundle>();
	private final List<SensorimotorBundle> outputs = new ArrayList<SensorimotorBundle>();
	private final List<Reflex> reflexes = new ArrayList<Reflex>();
	private final List<CuriosityLoop> curiosityLoops = new ArrayList<CuriosityLoop>();
	private final List<Actor> actors = new ArrayList<Actor>();
	private final List<Predictor> predictors = new ArrayList<Predictor>();
	private final StateStreamBundle allErrorStreams;
	private final StatesStreamBundleMean totalErrors;
	private final StateStreamBundle totalErrorStreams;
	private final int statesStreamLength;
	private CuriosityLoop activeCuriosityLoop = null;

	public Ecosystem(int statesStreamLength) {
		this.statesStreamLength = statesStreamLength;
		allErrorStreams = new StateStreamBundle(statesStreamLength * 4);
		totalErrors = new StatesStreamBundleMean(allErrorStreams);
		totalErrorStreams = new StateStreamBundle(statesStreamLength * 4);
		totalErrorStreams.addCompressingStatesProviderStreams(totalErrors);
	}

	public SensorimotorBundle makeInput(StatesProvider statesProvider) {
		SensorimotorBundle input = new SensorimotorBundle(statesProvider,
				statesStreamLength);
		inputs.add(input);
		sensorimotorBundles.add(input);
		return input;
	}

	public SensorimotorBundle makeOutput(StatesProvider statesProvider) {
		SensorimotorBundle output = new SensorimotorBundle(statesProvider,
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
		allErrorStreams.addCompressingStatesProviderStreams(predictor
				.getError());
	}

	public void addCuriosityLoop(CuriosityLoop curiosityLoop) {
		curiosityLoops.add(curiosityLoop);
		allErrorStreams.addCompressingStatesProviderStreams(curiosityLoop
				.getPredictor().getError());
	}

	public void step() {
		stepInputs();
		stepOutputs();
	}

	private void stepInputs() {
		for (int i = 0; i < inputs.size(); i++) {
			inputs.get(i).getStatesProvider().updateStates();
			inputs.get(i).getStatesProvider().notifyStatesObservers();
		}
	}

	private void stepOutputs() {
		// for (int i = 0; i < outputs.size(); i++) {
		// int channels = outputs.get(i).getStateStreams().size();
		// for (int n = 0; n < channels; n++)
		// outputs.get(i).getStateStreams().get(n).setOutputState(0);
		// }

		if (activeCuriosityLoop == null) {
			int r = Util.RandomInt(0, curiosityLoops.size() - 1);
			activeCuriosityLoop = curiosityLoops.get(r);
			activeCuriosityLoop.activate();
		} else if (!activeCuriosityLoop.isActive())
			activeCuriosityLoop = null;

		for (int i = 0; i < reflexes.size(); i++)
			reflexes.get(i).react();

		for (int i = 0; i < outputs.size(); i++) {
			outputs.get(i).getStatesProvider().updateStates();
			outputs.get(i).getStatesProvider().notifyStatesObservers();
		}

		// for (int i = 0; i < predictors.size(); i++)
		// predictors.get(i).step();

		for (int i = 0; i < curiosityLoops.size(); i++)
			if (curiosityLoops.get(i).isActive())
				curiosityLoops.get(i).step();

		totalErrors.updateStates();
	}

	private List<StateStreamBundle> getRandomUniqueStateStreamBundles(
			List<StateStream> stateStreams, int n, int min, int max) {
		List<StateStreamBundle> result = new ArrayList<StateStreamBundle>();
		int[] streamIndexes = new int[max * 2];
		for (int s = 0; s < streamIndexes.length; s++)
			streamIndexes[s] = Integer.MIN_VALUE;
		int streamCount = 0;
		int resultLengths[] = new int[n];
		int total;
		if (min * n >= stateStreams.size())
			min = (int) Math.floor((double) stateStreams.size() / n);
		do {
			total = 0;
			for (int i = 0; i < n; i++) {
				resultLengths[i] = Util.RandomInt(min, max);
				total += resultLengths[i];
			}
		} while (total > stateStreams.size());
		for (int i = 0; i < n; i++) {
			for (int s = 0; s < resultLengths[i]; s++) {
				boolean original = false;
				int p = -1;
				while (!original) {
					original = true;
					p = Util.RandomInt(0, stateStreams.size() - 1);
					for (int ss = 0; ss < streamIndexes.length; ss++)
						if (p == streamIndexes[ss])
							original = false;
				}
				streamIndexes[streamCount++] = p;
			}
			StateStreamBundle stateStreamBundle = new StateStreamBundle(
					statesStreamLength);
			for (int c = 0; c < resultLengths[i]; c++) {
				StateStream stream = stateStreams
						.get(streamIndexes[(streamCount - 1) - c]);
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
				StateProviderStream stateStream = (StateProviderStream) sensorimotorBundle
						.getStateStreams().get(c);
				for (int cc = 0; cc < stateStream.getStatesProvider()
						.getStateTypes().length; cc++) {
					if ((types | stateStream.getStatesProvider()
							.getStateTypes()[cc]) != 0) {
						boolean original = true;
						for (int n1 = 0; n1 < stateStreams.size(); n1++)
							if (stateStream == stateStreams.get(n1))
								original = false;
						if (original)
							stateStreams.add(stateStream);
					}
				}
			}
		}
		return getRandomUniqueStateStreamBundles(stateStreams, n, min, max);
	}

	public List<SensorimotorBundle> getInputs() {
		return inputs;
	}

	public List<SensorimotorBundle> getOutputs() {
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

	public StateStreamBundle getAllErrorsStreams() {
		return allErrorStreams;
	}

	public List<CuriosityLoop> getCuriosityLoops() {
		return curiosityLoops;
	}

	public List<SensorimotorBundle> getSensorimotorBundles() {
		return sensorimotorBundles;
	}

	public StateStreamBundle getTotalErrorStreams() {
		return totalErrorStreams;
	}
}
