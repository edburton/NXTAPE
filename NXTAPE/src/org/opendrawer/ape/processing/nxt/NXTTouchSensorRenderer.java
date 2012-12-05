package org.opendrawer.ape.processing.nxt;

import org.opendrawer.ape.darwinianneurodynamics.DataProvider;
import org.opendrawer.ape.processing.DataProviderRenderer;
import org.opendrawer.ape.processing.Renderer;

import processing.core.PGraphics;

public class NXTTouchSensorRenderer extends DataProviderRenderer {
	NXTTouchSensor nxtTouchSensor;

	public NXTTouchSensorRenderer(NXTTouchSensor nxtTouchSensor) {
		super();
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
	public DataProvider getDataProvider() {
		return nxtTouchSensor;
	}
}
