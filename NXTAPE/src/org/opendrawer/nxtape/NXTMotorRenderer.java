package org.opendrawer.nxtape;

import org.opendrawer.dawinian.neurodynamics.DataProvider;

import processing.core.PGraphics;

public class NXTMotorRenderer extends NXTRenderer {
	NXTMotor nxtMotor;

	public NXTMotorRenderer(NXTMotor nxtMotor) {
		super();
		this.nxtMotor = nxtMotor;
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
		g.stroke(255, 255, 0);
		float a = (float) ((nxtMotor.getNormalizedValues()[1]) * (Math.PI * 2));
		g.line(xc, yc, (float) (xc + Math.sin(a) * radius),
				(float) (yc + Math.cos(a) * radius));
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
	public void mouseClicked(int mouseX, int mouseY) {
		float xc = x + width / 2;
		float yc = y + height / 2;
		float dx = mouseX - xc;
		float dy = mouseY - yc;
		float a = (float) (Math.atan2(dx, dy) * (360 / (Math.PI * 2)));
		nxtMotor.setActualAngle(Math.round(a));
	}

	@Override
	public void mousePressed(int mouseX, int mouseY) {
		mouseClicked(mouseX, mouseY);
	}

	@Override
	public void mouseDragged(int mouseX, int mouseY) {
		mouseClicked(mouseX, mouseY);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		mouseClicked(mouseX, mouseY);
	}

	@Override
	public DataProvider getDataProvider() {
		return nxtMotor;
	}
}
