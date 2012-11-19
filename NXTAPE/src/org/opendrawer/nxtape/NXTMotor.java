package org.opendrawer.nxtape;

import lejos.nxt.remote.RemoteMotor;

public class NXTMotor implements InputProvider {
	private RemoteMotor remoteMotor;
	private final String name;
	private final int minAngle;
	private final int maxAngle;
	private final int restAngle;
	private final float friction;
	private float inputRate;
	private float virtualAngle;
	private float virtualSpeed;
	private float targetAngle;
	private int actualAngle;
	private float maxInputRate = 0.01f;
	private static String[] subTitles = new String[] { "Angle", "Speed" };
	private boolean inhibited = false;

	public NXTMotor(RemoteMotor remoteMotor, String name, int minAngle,
			int maxAngle, int restAngle, float friction) {
		this.remoteMotor = remoteMotor;
		this.name = name;
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
		this.friction = friction;
		this.restAngle = (int) (virtualAngle = actualAngle = restAngle);
		targetAngle = this.restAngle;
		inputRate = virtualSpeed = 0;
	}

	public int getRestAngle() {
		return restAngle;
	}

	@Override
	public String getName() {
		return name;
	}

	public float getMinAngle() {
		return minAngle;
	}

	public float getMaxAngle() {
		return maxAngle;
	}

	@Override
	public void startStep() {
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
		virtualSpeed += inputRate;
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
		int iVirtualAngle = Math.round(virtualAngle);
		if (remoteMotor != null && iVirtualAngle != actualAngle
				&& iVirtualAngle != targetAngle) {
			remoteMotor.rotateTo(iVirtualAngle, true);
			targetAngle = iVirtualAngle;
		}
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
		return new float[] {
				((float) actualAngle - restAngle) / (maxAngle - minAngle),
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

	public void setActualAngle(int actualAngle) {
		this.actualAngle = actualAngle;
	}

	@Override
	public void setInhihited(boolean inhibited) {
		this.inhibited = inhibited;
	}
}
