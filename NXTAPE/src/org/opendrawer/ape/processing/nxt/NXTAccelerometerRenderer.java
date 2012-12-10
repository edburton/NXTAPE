package org.opendrawer.ape.processing.nxt;

import java.awt.Color;

import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;
import org.opendrawer.ape.processing.Renderer;
import org.opendrawer.ape.processing.StatesProviderRenderer;

import processing.core.PGraphics;

public class NXTAccelerometerRenderer extends StatesProviderRenderer {
	NXTAccelerometer nxtAccelerometer;

	public NXTAccelerometerRenderer(NXTAccelerometer nxtAccelerometer) {
		super(nxtAccelerometer);
		this.nxtAccelerometer = nxtAccelerometer;
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
		g.fill(16);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		double[] states = nxtAccelerometer.getStates();
		g.strokeWeight(Renderer.lineWidth);
		float r = Renderer.lineMarginWidth * 2;
		for (int i = 0; i < 3; i++) {
			int c = Color.HSBtoRGB(i / 3.0f, 1.0f, 1.0f);
			g.stroke(c);
			g.fill(c);
			float a = ((float) Math.PI * (2 / 3.0f)) * i;
			float rdx = (float) ((Math.sin(a) * radius) * states[i]);
			float rdy = (float) ((Math.cos(a) * radius) * states[i]);
			g.line(xc, yc, xc + rdx, yc + rdy);
			g.noStroke();
			g.ellipse(xc + (rdx - r), yc + (rdy - r), xc + (rdx + r), yc
					+ (rdy + r));
		}
	}

	@Override
	public StatesProvider getStatesProvider() {
		return nxtAccelerometer;
	}
}
