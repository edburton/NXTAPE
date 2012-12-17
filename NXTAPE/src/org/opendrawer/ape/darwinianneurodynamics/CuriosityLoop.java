package org.opendrawer.ape.darwinianneurodynamics;

public class CuriosityLoop extends StateStreamBundleGroupList {

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
		getPredictor().step();
		getActor().step();
	}
}
