package org.opendrawer.ape.nxt;

import org.opendrawer.dawinian.neurodynamics.DataProvider;

import processing.core.PGraphics;

public class NXTCompassRenderer extends NXTRenderer {
	NXTCompass nxtCompass;

	public NXTCompassRenderer(NXTCompass nxtCompass) {
		super();
		this.nxtCompass = nxtCompass;
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		g.strokeWeight(NXT_ArtificialPlasticityEcology.lineWidth);
		float radius = (Math.min(width, height) / 2);
		float xc = x + width / 2;
		float yc = y + height / 2;
		g.noStroke();
		g.fill(64, 64, 64);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		radius -= NXT_ArtificialPlasticityEcology.lineMarginWidth;
		g.fill(16);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		g.stroke(255, 255, 0);
		float a = (float) ((nxtCompass.getNormalizedValues()[0]) * (Math.PI * 2));
		g.line(xc, yc, (float) (xc + Math.sin(a) * radius),
				(float) (yc + Math.cos(a) * radius));
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
	public void mouseClicked(int mouseX, int mouseY) {

	}

	@Override
	public DataProvider getDataProvider() {
		return nxtCompass;
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
