package org.opendrawer.nxtape;

import lejos.nxt.remote.RemoteMotor;

import org.opendrawer.dawinian.neurodynamics.DataProvider;
import org.opendrawer.dawinian.neurodynamics.OutputDataProvider;

public class NXTMotor implements OutputDataProvider {
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
	private double maxInputRate = 1f;
	private double maxRate = 40.0f;
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
			topSpeed = remoteMotor.getSpeed();
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
		int iVirtualAngle = (int) Math.round(virtualAngle);
		if (Math.abs(actualAngle - iVirtualAngle) > 1) {
			iVirtualAngle += actualAngle - iVirtualAngle;
		}
		virtualSpeed += inputRate * maxInputRate;
		if (virtualSpeed != 0) {
			if (virtualSpeed > maxRate)
				virtualSpeed = maxRate;
			else if (virtualSpeed < -maxRate)
				virtualSpeed = -maxRate;
			virtualAngle += virtualSpeed;
			if (virtualAngle < minAngle) {
				virtualSpeed = Math.abs(virtualSpeed);
				virtualAngle = minAngle;
			} else if (virtualAngle > maxAngle) {
				virtualSpeed = -Math.abs(virtualSpeed);
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
		iVirtualAngle = (int) Math.round(virtualAngle);
		if (remoteMotor != null && iVirtualAngle != actualAngle
				&& iVirtualAngle != targetAngle) {
			remoteMotor.rotateTo(iVirtualAngle, true);
			targetAngle = iVirtualAngle;
		}
		virtualSpeed *= friction;
	}

	@Override
	public void setOutputChannel(double data, int dataChannel) {
		inputRate = data;
		if (inputRate < -1)
			inputRate = -1;
		else if (inputRate > 1)
			inputRate = 1;
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
