package org.opendrawer.nxtape;

import java.awt.Color;

import processing.core.PGraphics;

import lejos.nxt.remote.RemoteMotor;

public class NXTMotor implements GraphicalDataProvider {
	private RemoteMotor remoteMotor;
	private final String name;
	private final Color colour;
	private final float minAngle;
	private final float maxAngle;
	private final int restAngle;
	private final float friction;
	private float virtualAngle;
	private float virtualSpeed;
	private float actualAngle;
	private final static int maxSpeed = 12;
	private static String[] subTitles = new String[] { "Angle", "Speed" };

	public NXTMotor(RemoteMotor remoteMotor, String name, Color colour,
			int minAngle, int maxAngle, int restAngle, float friction) {
		this.remoteMotor = remoteMotor;
		this.name = name;
		this.colour = colour;
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
		this.friction = friction;
		this.restAngle = (int) (virtualAngle = actualAngle = restAngle);
		virtualSpeed = 0;
	}

	public int getRestAngle() {
		return restAngle;
	}

	public String getName() {
		return name;
	}

	public Color getColour() {
		return colour;
	}

	public float getMinAngle() {
		return minAngle;
	}

	public float getMaxAngle() {
		return maxAngle;
	}

	@Override
	public void step() {
		actualAngle = remoteMotor.getTachoCount();
		virtualAngle = actualAngle;
		if (virtualSpeed != 0) {
			virtualSpeed *= friction;
			if (virtualSpeed > 1)
				virtualSpeed = 1;
			else if (virtualSpeed < -1)
				virtualSpeed = -1;
			virtualAngle += virtualSpeed * maxSpeed;
			if (virtualAngle < minAngle) {
				virtualSpeed = Math.abs(virtualSpeed);
				virtualAngle = minAngle + (minAngle - virtualAngle);
				virtualSpeed *= friction * friction;
			} else if (virtualAngle > maxAngle) {
				virtualSpeed = -Math.abs(virtualSpeed);
				virtualAngle = maxAngle + (maxAngle - virtualAngle);
				virtualSpeed *= friction * friction;
			}
		}
		remoteMotor.rotateTo((int) virtualAngle, true);
	}

	public void accelerate(float rate) {
		virtualSpeed += rate;
	}

	@Override
	public float[] getNormalizedValues() {
		return new float[] { (actualAngle - minAngle) / (maxAngle - minAngle),
				virtualSpeed / -2 + 0.5f };
	}

	@Override
	public String[] getChannelNames() {
		return subTitles;
	}

	@Override
	public int getChannels() {// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void drawAt(PGraphics g, float x, float y, float width, float height) {
		float radius = (Math.min(width, height) / 2) - 2.5f;
		float xc = x + width / 2;
		float yc = y + height / 2;
		g.fill(0);
		g.strokeWeight(5);
		g.stroke(64, 64, 64);
		g.ellipse(xc - radius, yc - radius, xc + radius, yc + radius);
		g.stroke(255, 255, 0);
		float a = (float) ((actualAngle / 360.0f) * (Math.PI * 2));
		g.line(xc, yc, (float) (xc + Math.sin(a) * radius),
				(float) (yc + Math.cos(a) * radius));
	}
}
