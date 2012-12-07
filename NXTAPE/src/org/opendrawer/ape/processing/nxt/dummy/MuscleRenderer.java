package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;
import org.opendrawer.ape.processing.StatesProviderRenderer;
import org.opendrawer.ape.processing.Renderer;

import processing.core.PGraphics;

public class MuscleRenderer extends StatesProviderRenderer {
	Muscle muscle;

	public MuscleRenderer(Muscle muscle) {
		super();
		this.muscle = muscle;
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		g.strokeWeight(Renderer.lineWidth);
		float xc = x + width / 2;
		float value = (float) muscle.getCurrentRestLength();

		float mh = ((1.2f - value) / 4) * width;
		g.stroke(255, 128, 128);
		g.fill(250, 100, 100);
		g.ellipse(xc - mh, (y + height) - (height * value), xc + mh, y + height);

	}

	@Override
	public StatesProvider getStatesProvider() {
		return muscle;
	}
}
