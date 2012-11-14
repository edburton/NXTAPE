package org.opendrawer.nxtape;

import java.awt.Color;

import lejos.nxt.remote.RemoteMotor;

public class NXTMotor implements DataProvider {
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
	private final static int maxSpeed=12;
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
			virtualAngle += virtualSpeed*maxSpeed ;
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
				virtualSpeed / 2 + 0.5f };
	}

	@Override
	public String[] getValueNames() {
		return subTitles;
	}
}
