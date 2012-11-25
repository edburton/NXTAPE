package org.opendrawer.nxtape;

import lejos.nxt.remote.RemoteMotor;

import org.opendrawer.dawinian.neurodynamics.DataProvider;
import org.opendrawer.dawinian.neurodynamics.OutputProvider;

public class NXTMotor implements OutputProvider {
	private RemoteMotor remoteMotor;
	private final String name;
	private final int minAngle;
	private final int maxAngle;
	private final int restAngle;
	private final double friction;
	private double inputRate;
	private double virtualAngle;
	private double virtualSpeed;
	private double targetAngle;
	private int actualAngle;
	private double maxInputRate = 1;
	private double maxRate = 10.0f;
	private static String[] channelNames = new String[] { "Impulse", "Angle" };
	private static final int[] channelTypes = new int[] { DataProvider.OUTPUT,
			DataProvider.INPUT };
	private boolean inhibited = false;
	private double topSpeed;
	private int currentSpeed;

	public NXTMotor(RemoteMotor remoteMotor, String name, int minAngle,
			int maxAngle, int restAngle, double friction) {
		this.remoteMotor = remoteMotor;
		this.name = name;
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
		this.friction = friction;
		this.restAngle = (int) (virtualAngle = actualAngle = restAngle);
		targetAngle = this.restAngle;
		inputRate = virtualSpeed = 0;
		if (remoteMotor != null) {
			topSpeed = remoteMotor.getSpeed() / 4;
			currentSpeed = (int) topSpeed;
			remoteMotor.setSpeed(currentSpeed);
			remoteMotor.rotateTo(restAngle, true);
		}
	}

	public int getRestAngle() {
		return restAngle;
	}

	@Override
	public String getName() {
		return name;
	}

	public double getMinAngle() {
		return minAngle;
	}

	public double getMaxAngle() {
		return maxAngle;
	}

	@Override
	public void step() {
		if (inhibited) {
			virtualSpeed = 0;
			if (remoteMotor != null) {
				int angle = remoteMotor.getTachoCount();
				virtualAngle = angle;
				int iActualAngle = Math.round(actualAngle);
				if (angle != iActualAngle && iActualAngle != targetAngle) {
					remoteMotor.rotateTo(iActualAngle, true);
					targetAngle = iActualAngle;
				}
			}
			return;
		}
		if (remoteMotor != null)
			actualAngle = remoteMotor.getTachoCount();
		virtualSpeed += inputRate * maxInputRate;
		virtualAngle = (actualAngle + virtualAngle) / 2;
		if (virtualSpeed != 0) {
			virtualSpeed *= friction;
			if (virtualSpeed > maxRate)
				virtualSpeed = maxRate;
			else if (virtualSpeed < -maxRate)
				virtualSpeed = -maxRate;
			virtualAngle += virtualSpeed;
			if (virtualAngle < minAngle) {
				virtualSpeed = 0;
				virtualAngle = minAngle;
			} else if (virtualAngle > maxAngle) {
				virtualSpeed = 0;
				virtualAngle = maxAngle;
			}
		}
		if (remoteMotor != null) {
			int speed = (int) (Math.round(topSpeed
					* (Math.abs(virtualSpeed) / maxRate)));
			if (speed <= 0)
				speed = 1;
			if (speed != currentSpeed) {
				remoteMotor.setSpeed(speed);
				currentSpeed = speed;
			}
		}
		int iVirtualAngle = (int) Math.round(virtualAngle);
		if (remoteMotor != null && iVirtualAngle != actualAngle
				&& iVirtualAngle != targetAngle) {
			remoteMotor.rotateTo(iVirtualAngle, true);
			targetAngle = iVirtualAngle;
		}
	}

	@Override
	public void setOutputChannel(double data, int dataChannel) {
		inputRate = data;
	}

	@Override
	public double[] getNormalizedValues() {
		return new double[] { inputRate,
				((double) actualAngle - restAngle) / (maxAngle - minAngle) };
	}

	@Override
	public String[] getChannelNames() {
		return channelNames;
	}

	@Override
	public int getChannelCount() {
		return 2;
	}

	public double getActualAngle() {
		return actualAngle;
	}

	public void setActualAngle(int actualAngle) {
		this.actualAngle = actualAngle;
	}

	@Override
	public void setInhihited(boolean inhibited) {
		this.inhibited = inhibited;
	}

	@Override
	public int[] getChannelTypes() {
		return channelTypes;
	}
}
