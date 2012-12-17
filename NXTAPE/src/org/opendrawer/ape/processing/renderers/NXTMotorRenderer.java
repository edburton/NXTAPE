package org.opendrawer.ape.processing.renderers;

import org.opendrawer.ape.processing.nxt.NXTMotor;

import processing.core.PGraphics;

public class NXTMotorRenderer extends Renderer {
	NXTMotor nxtMotor;

	public NXTMotorRenderer(NXTMotor nxtMotor) {
		this.nxtMotor = nxtMotor;
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		g.strokeWeight(Renderer.lineWidth);
		float radius = (Math.min(width, height) / 2) - Renderer.lineMarginWidth;
		float xc = x + width / 2;
		float yc = y + height / 2;
		g.noStroke();
		g.fill(Math.round(keyColor.getRed()), Math.round(keyColor.getGreen()),
				Math.round(keyColor.getBlue()));
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		radius -= Renderer.lineMarginWidth;
		g.fill(16);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		g.stroke(255, 255, 0);
		float a = (float) ((nxtMotor.getStates()[1]) * (Math.PI));
		g.line(xc, yc, (float) (xc + Math.sin(a) * radius),
				(float) (yc + Math.cos(a) * radius));
	}
}
