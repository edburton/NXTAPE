package org.opendrawer.nxtape;

import processing.core.PGraphics;

public class NXTTouchSensorRenderer extends InteractiveRenderer {
	NXTTouchSensor nxtTouchSensor;

	public NXTTouchSensorRenderer(NXTTouchSensor nxtTouchSensor) {
		super();
		this.nxtTouchSensor = nxtTouchSensor;
	}

	@Override
	public void draw(PGraphics g) {
		float radius = (Math.min(width, height) / 2) - 2.5f;
		float xc = x + width / 2;
		float yc = y + height / 2;
		if (nxtTouchSensor.isOn())
			g.fill(255, 255, 0);
		else
			g.fill(16);
		g.strokeWeight(5);
		g.stroke(64, 64, 64);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
	}

	public boolean contains(float x1, float y1) {
		float radius = (Math.min(width, height) / 2) - 2.5f;
		float xc = x + width / 2;
		float yc = y + height / 2;
		float dx = x1 - xc;
		float dy = y1 - yc;
		return Math.sqrt(dx * dx + dy * dy) <= radius;
	}

	public void mouseClicked(int mouseX, int mouseY) {
		nxtTouchSensor.setOn(!nxtTouchSensor.isOn());
	}
}
