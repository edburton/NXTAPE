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
		float radius = (Math.min(width, height) / 2);
		float xc = x + width / 2;
		float yc = y + height / 2;
		g.noStroke();
		g.fill(0, 128, 0);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		radius -= Renderer.lineMarginWidth;
		g.fill(16);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);

		float value = (float) ((muscle.getData()[0]));

		for (int i = 8; i >= 0; i--) {
			float mh = ((1.2f - value) / 4) * height;
			float f = ((i + 0.5f) / 8.0f);
			mh *= f;
			mh = Math.min(mh, (width * value));
			g.stroke(255 * (1 - f), 128 * (1 - f), 128 * (1 - f));
			g.ellipse(x, yc - mh, x + (width * value), yc + mh);
		}
	}

	@Override
	public DataProvider getDataProvider() {
		return muscle;
	}
}
