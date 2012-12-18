package org.opendrawer.ape.darwinianneurodynamics;

public class CuriosityLoop extends StateStreamBundleGroupList {

	private int activeCount = 0;
	private static final int activationDuration = 100;

	public CuriosityLoop(Actor predictor, Actor actor) {
		super(predictor, actor);

	}

	public CuriosityLoop() {

	}

	public Predictor getPredictor() {
		return (Predictor) stateStreamBundleGroups.get(0);
	}

	public Actor getActor() {
		return (Actor) stateStreamBundleGroups.get(1);
	}

	public void step() {
		if (isActive()) {
			getPredictor().step();
			getActor().step();
		}
		if (activeCount > 0 && ++activeCount > activationDuration)
			activeCount = 0;
	}

	public boolean isActive() {
		return activeCount > 0;
	}

	public void activate() {
		activeCount = 1;
	}
}
