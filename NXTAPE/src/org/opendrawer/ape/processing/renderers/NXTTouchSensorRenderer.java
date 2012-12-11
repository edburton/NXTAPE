package org.opendrawer.ape.processing.renderers;

import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;
import org.opendrawer.ape.processing.nxt.NXTTouchSensor;

import processing.core.PGraphics;

public class NXTTouchSensorRenderer extends StatesProviderRenderer {
	NXTTouchSensor nxtTouchSensor;

	public NXTTouchSensorRenderer(NXTTouchSensor nxtTouchSensor) {
		this.nxtTouchSensor = nxtTouchSensor;
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		float radius = (Math.min(width, height) / 2);
		float xc = x + width / 2;
		float yc = y + height / 2;
		g.noStroke();
		g.fill(64, 64, 64);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		radius -= Renderer.lineMarginWidth;

		if (nxtTouchSensor.isOn())
			g.fill(255, 255, 0);
		else
			g.fill(16);

		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
	}

	@Override
	public StatesProvider getStatesProvider() {
		return nxtTouchSensor;
	}
}
