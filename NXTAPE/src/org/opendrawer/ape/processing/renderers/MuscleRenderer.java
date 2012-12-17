package org.opendrawer.ape.processing.renderers;

import org.opendrawer.ape.processing.nxt.dummy.Muscle;

import processing.core.PGraphics;

public class MuscleRenderer extends Renderer {
	Muscle muscle;

	public MuscleRenderer(Muscle muscle) {
		this.muscle = muscle;
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		g.strokeWeight(Renderer.lineWidth);
		float xc = x + width / 2;
		float value = (float) muscle.getCurrentRestLength();

		float mh = ((1.2f - value) / 4) * width;
		g.noStroke();
		g.fill(Math.round(keyColor.getRed()), Math.round(keyColor.getGreen()),
				Math.round(keyColor.getBlue()));
		g.ellipse(xc - mh, (y + height) - (height * value), xc + mh, y + height);
	}
}
