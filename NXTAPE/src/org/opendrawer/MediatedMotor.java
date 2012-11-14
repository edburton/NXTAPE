package org.opendrawer;

import java.awt.Color;

import lejos.nxt.remote.RemoteMotor;

public class MediatedMotor {
	private RemoteMotor remoteMotor;
	private final String name;
	private final Color colour;
	private final float minAngle;
	private final float maxAngle;
	private final int restAngle;
	private final float friction;
	private float maxRate;
	private float virtualAngle;
	private float virtualRate;
	private float actualAngle;
	private int maxSpeed;
	private float currentSpeed = 0;

	public MediatedMotor(RemoteMotor remoteMotor, String name, Color colour,
			int minAngle, int maxAngle, int restAngle, float friction) {
		this.remoteMotor = remoteMotor;
		this.name = name;
		this.colour = colour;
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
		this.friction = friction;
		this.restAngle = (int) (virtualAngle = actualAngle = restAngle);
		virtualRate = 0;
		maxSpeed = remoteMotor.getSpeed();
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

	public void step() {
		actualAngle = remoteMotor.getTachoCount();
		virtualAngle = actualAngle;
		if (currentSpeed > 0) {
			virtualRate *= friction;
			if (virtualRate > maxSpeed)
				virtualRate = maxSpeed;
			else if (virtualRate < -maxSpeed)
				virtualRate = -maxSpeed;
			virtualAngle += virtualRate;
			currentSpeed *= friction;
			if (virtualAngle < minAngle) {
				virtualRate = Math.abs(virtualRate);
				virtualAngle = minAngle + (minAngle - virtualAngle);
				virtualRate *= friction * friction;
				currentSpeed *= friction * friction;
			} else if (virtualAngle > maxAngle) {
				virtualRate = -Math.abs(virtualRate);
				virtualAngle = maxAngle + (maxAngle - virtualAngle);
				virtualRate *= friction * friction;
				currentSpeed *= friction * friction;
			}
			if (currentSpeed <= 1) {
				virtualRate = currentSpeed = 0;
			}
		}
		remoteMotor.setSpeed((int) currentSpeed);
		remoteMotor.rotateTo((int) virtualAngle, true);
	}

	public void accelerate(float rate) {
		virtualRate += rate;
		currentSpeed = maxSpeed;
	}

	public float getNormalizedValue() {
		return (actualAngle - minAngle) / (maxAngle - minAngle);
	}
}
