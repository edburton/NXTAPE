package org.opendrawer.nxtape;

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
		float radius = (Math.min(width, height) / 2)
				- NXT_ArtificialPlasticityEcology.lineWidth / 2;
		float xc = x + width / 2;
		float yc = y + height / 2;
		g.fill(16);
		g.strokeWeight(NXT_ArtificialPlasticityEcology.lineWidth);
		g.stroke(64, 64, 64);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		double[] values = nxtAccelerometer.getNormalizedValues();
		for (int i = 0; i < 3; i++) {
			g.stroke(Color.HSBtoRGB(i / 3.0f, 1.0f, 1.0f));
			float a = ((float) Math.PI * (2 / 3.0f)) * i;
			float rdx = (float) ((Math.sin(a) * radius) * values[i]);
			float rdy = (float) ((Math.cos(a) * radius) * values[i]);
			g.line(xc, yc, xc + rdx, yc + rdy);
			float r = NXT_ArtificialPlasticityEcology.lineWidth / 2;
			g.ellipse(xc + (rdx - r), yc + (rdy - r), xc + (rdx + r), yc
					+ (rdy + r));
		}
	}

	@Override
	public boolean contains(float x1, float y1) {
		float radius = (Math.min(width, height) / 2)
				- NXT_ArtificialPlasticityEcology.lineWidth / 2;
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
