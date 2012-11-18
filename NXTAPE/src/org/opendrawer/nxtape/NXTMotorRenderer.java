package org.opendrawer.nxtape;

import processing.core.PGraphics;

public class NXTMotorRenderer extends Renderer {
	NXTMotor nxtMotor;

	public NXTMotorRenderer(NXTMotor nxtMotor) {
		super();
		this.nxtMotor = nxtMotor;
	}

	@Override
	public void draw(PGraphics g) {
		float radius = (Math.min(width, height) / 2) - 2.5f;
		float xc = x + width / 2;
		float yc = y + height / 2;
		g.fill(16);
		g.strokeWeight(5);
		g.stroke(64, 64, 64);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		g.stroke(255, 255, 0);
		float a = (float) ((nxtMotor.getActualAngle() / 360.0f) * (Math.PI * 2));
		g.line(xc, yc, (float) (xc + Math.sin(a) * radius),
				(float) (yc + Math.cos(a) * radius));
	}
}
