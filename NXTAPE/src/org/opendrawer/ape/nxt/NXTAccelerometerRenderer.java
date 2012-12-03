package org.opendrawer.ape.nxt;

import java.awt.Color;

import org.opendrawer.dawinian.neurodynamics.DataProvider;

import processing.core.PGraphics;

public class NXTAccelerometerRenderer extends NXTRenderer {
	NXTAccelerometer nxtAccelerometer;

	public NXTAccelerometerRenderer(NXTAccelerometer nxtAccelerometer) {
		super();
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
		radius -= NXT_ArtificialPlasticityEcology.lineMarginWidth;
		g.fill(16);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		double[] values = nxtAccelerometer.getNormalizedValues();
		g.strokeWeight(NXT_ArtificialPlasticityEcology.lineWidth);
		float r = NXT_ArtificialPlasticityEcology.lineMarginWidth * 2;
		for (int i = 0; i < 3; i++) {
			int c = Color.HSBtoRGB(i / 3.0f, 1.0f, 1.0f);
			g.stroke(c);
			g.fill(c);
			float a = ((float) Math.PI * (2 / 3.0f)) * i;
			float rdx = (float) ((Math.sin(a) * radius) * values[i]);
			float rdy = (float) ((Math.cos(a) * radius) * values[i]);
			g.line(xc, yc, xc + rdx, yc + rdy);
			g.noStroke();
			g.ellipse(xc + (rdx - r), yc + (rdy - r), xc + (rdx + r), yc
					+ (rdy + r));
		}
	}

	@Override
	public boolean contains(float x1, float y1) {
		float radius = (Math.min(width, height) / 2)
				- NXT_ArtificialPlasticityEcology.lineMarginWidth / 2;
		float xc = x + width / 2;
		float yc = y + height / 2;
		float dx = x1 - xc;
		float dy = y1 - yc;
		return Math.sqrt(dx * dx + dy * dy) <= radius;
	}

	@Override
	public DataProvider getDataProvider() {
		return nxtAccelerometer;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY) {

	}

	@Override
	public void mousePressed(int mouseX, int mouseY) {

	}

	@Override
	public void mouseDragged(int mouseX, int mouseY) {

	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {

	}
}
