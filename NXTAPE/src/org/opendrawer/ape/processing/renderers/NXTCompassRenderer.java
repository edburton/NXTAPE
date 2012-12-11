package org.opendrawer.ape.processing.renderers;

import org.opendrawer.ape.processing.nxt.NXTCompass;

import processing.core.PGraphics;

public class NXTCompassRenderer extends Renderer {
	NXTCompass nxtCompass;

	public NXTCompassRenderer(NXTCompass nxtCompass) {
		this.nxtCompass = nxtCompass;
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		g.strokeWeight(Renderer.lineWidth);
		float radius = (Math.min(width, height) / 2);
		float xc = x + width / 2;
		float yc = y + height / 2;
		g.noStroke();
		g.fill(64, 64, 64);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		radius -= Renderer.lineMarginWidth;
		g.fill(16);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		g.stroke(255, 255, 0);
		float a = (float) ((nxtCompass.getStates()[0]) * (Math.PI * 2));
		g.line(xc, yc, (float) (xc + Math.sin(a) * radius),
				(float) (yc + Math.cos(a) * radius));
	}
}
