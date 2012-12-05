package org.opendrawer.ape.processing.nxt.dummy;

import org.opendrawer.ape.darwinianneurodynamics.DataProvider;
import org.opendrawer.ape.processing.DataProviderRenderer;
import org.opendrawer.ape.processing.Renderer;

import processing.core.PGraphics;

public class MuscleRenderer extends DataProviderRenderer {
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
		g.fill(240, 90, 90);
		g.ellipse(xc - mh, (y + height) - (height * value), xc + mh, y + height);

	}

	@Override
	public DataProvider getDataProvider() {
		return muscle;
	}
}
