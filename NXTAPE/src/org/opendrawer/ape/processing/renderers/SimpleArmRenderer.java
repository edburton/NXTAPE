package org.opendrawer.ape.processing.renderers;

import org.opendrawer.ape.processing.nxt.dummy.SimpleArm;

import processing.core.PGraphics;

public class SimpleArmRenderer extends Renderer {
	SimpleArm simpleArm;

	public SimpleArmRenderer(SimpleArm simpleArm) {
		this.simpleArm = simpleArm;
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		float radius = (Math.min(width, height) / 2) - Renderer.lineMarginWidth;
		float xc = x + width / 2;
		float yc = y + height / 2;
		g.noStroke();
		g.fill(0, 128, 0);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		radius -= Renderer.lineMarginWidth;
		g.fill(16);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		g.stroke(255, 255, 0);
		double[] xs = simpleArm.getXs();
		double[] ys = simpleArm.getYs();
		float xp = 0;
		float yp = 0;
		for (int i = 0; i < xs.length; i++) {
			g.strokeWeight(Renderer.lineWidth
					* (float) Math.sqrt((xs.length) - i));
			float xn = (float) xs[i];
			float yn = (float) ys[i];
			g.line(xc + xp * radius, yc + yp * radius, xc + xn * radius, yc
					+ yn * radius);
			xp = xn;
			yp = yn;
		}
	}
}
