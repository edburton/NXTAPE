package org.opendrawer.ape.processing.nxt;

import lejos.nxt.remote.RemoteMotor;

import org.opendrawer.ape.darwinianneurodynamics.OutputStatesProvider;

public class NXTMotor extends OutputStatesProvider {
	private final RemoteMotor remoteMotor;
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
	private final double maxInputRate = 1f;
	private final double maxRate = 40.0f;
	private static String[] stateNames = new String[] { "Impulse", "Angle" };
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
	public void updateStates() {
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
	public void setOutputState(double state, int stateIndex) {
		inputRate = state;
		if (inputRate < -1)
			inputRate = -1;
		else if (inputRate > 1)
			inputRate = 1;
	}

	@Override
	public double[] getStates() {
		return new double[] { inputRate,
				((double) actualAngle - restAngle) / (maxAngle - minAngle) };
	}

	@Override
	public String[] getStateNames() {
		return stateNames;
	}

	@Override
	public int getStatesLength() {
		return 2;
	}

	public double getActualAngle() {
		return actualAngle;
	}

	public void setGUIAngle(int actualAngle) {
		this.actualAngle = (int) (restAngle + ((actualAngle / 360.0) * (maxAngle - minAngle)));
	}
}
