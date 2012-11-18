package org.opendrawer.nxtape;

import java.awt.Color;

import lejos.nxt.remote.RemoteMotor;

public class NXTMotor implements InputProvider {
	private RemoteMotor remoteMotor;
	private final String name;
	private final Color colour;
	private final float minAngle;
	private final float maxAngle;
	private final int restAngle;
	private final float friction;
	private float inputRate;
	private float virtualAngle;
	private float virtualSpeed;
	private float actualAngle;
	private float maxInputRate = 0.01f;
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
		inputRate = virtualSpeed = 0;
	}

	public int getRestAngle() {
		return restAngle;
	}

	@Override
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
	public void startStep() {
		virtualSpeed += inputRate;
		actualAngle = remoteMotor != null ? remoteMotor.getTachoCount()
				: restAngle;
		virtualAngle = actualAngle;
		if (virtualSpeed != 0) {
			virtualSpeed *= friction;
			if (virtualSpeed > 1)
				virtualSpeed = 1;
			else if (virtualSpeed < -1)
				virtualSpeed = -1;
			virtualAngle += virtualSpeed * (maxAngle - minAngle);
			if (virtualAngle < minAngle) {
				virtualSpeed = 0;
				virtualAngle = minAngle;
			} else if (virtualAngle > maxAngle) {
				virtualSpeed = 0;
				virtualAngle = maxAngle;
			}
		}
		int iVirtualAngle = (int) virtualAngle;
		if (remoteMotor != null && iVirtualAngle != actualAngle)
			remoteMotor.rotateTo(iVirtualAngle, true);
	}

	@Override
	public void finishStep() {
		inputRate = 0;
	}

	@Override
	public void setInputChannels(float[] data) {
		inputRate += data[0];
	}

	@Override
	public float[] getNormalizedValues() {
		return new float[] { (actualAngle - restAngle) / (maxAngle - minAngle),
				inputRate / maxInputRate };
	}

	@Override
	public String[] getChannelNames() {
		return subTitles;
	}

	@Override
	public int getChannelCount() {// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getInputChannelCount() {
		return 1;
	}

	public float getActualAngle() {
		return actualAngle;
	}
}
