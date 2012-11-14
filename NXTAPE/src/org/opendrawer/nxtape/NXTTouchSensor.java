package org.opendrawer.nxtape;

import processing.core.PGraphics;
import lejos.nxt.TouchSensor;

public class NXTTouchSensor implements GraphicalDataProvider {
	private final TouchSensor touchSensor;
	private final String name;
	private static String[] streamName = new String[] { "On/Off" };
	private boolean on = false;

	public NXTTouchSensor(TouchSensor touchSensor, String name) {
		this.touchSensor = touchSensor;
		this.name = name;
	}

	public float[] getNormalizedValues() {
		return new float[] { on ? 1 : 0 };
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String[] getChannelNames() {
		return streamName;
	}

	@Override
	public void step() {
		on = touchSensor.isPressed();
	}

	@Override
	public int getChannels() {
		return 1;
	}

	@Override
	public void drawAt(PGraphics g, float x, float y, float width, float height) {
		float radius = (Math.min(width, height) / 2) - 2.5f;
		float xc = x + width / 2;
		float yc = y + height / 2;
		if (on)
			g.fill(255, 255, 0);
		else
			g.fill(0);
		g.strokeWeight(5);
		g.stroke(64, 64, 64);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
	}

}
