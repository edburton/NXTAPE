package org.opendrawer.nxtape;

import org.opendrawer.dawinian.neurodynamics.DataProvider;

import processing.core.PGraphics;

public class NXTTouchSensorRenderer extends NXTRenderer {
	NXTTouchSensor nxtTouchSensor;

	public NXTTouchSensorRenderer(NXTTouchSensor nxtTouchSensor) {
		super();
		this.nxtTouchSensor = nxtTouchSensor;
	}

	@Override
	public void draw(PGraphics g) {
		float radius = (Math.min(width, height) / 2)
				- NXT_ArtificialPlasticityEcology.lineWidth / 2;
		float xc = x + width / 2;
		float yc = y + height / 2;
		if (nxtTouchSensor.isOn())
			g.fill(255, 255, 0);
		else
			g.fill(16);
		g.strokeWeight(NXT_ArtificialPlasticityEcology.lineWidth);
		g.stroke(64, 64, 64);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
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
		nxtTouchSensor.setOn(!nxtTouchSensor.isOn());
	}

	@Override
	public DataProvider getDataProvider() {
		return nxtTouchSensor;
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
