package org.opendrawer.nxtape;

import lejos.nxt.TouchSensor;

public class NXTTouchSensor implements DataProvider {
	private final TouchSensor touchSensor;
	private final String name;
	private static String[] streamName=new String[] {"On/Off"};
	private boolean on=false;
	
	public NXTTouchSensor(TouchSensor touchSensor, String name) {
		this.touchSensor = touchSensor;
		this.name = name;
	}
	
	public float[] getNormalizedValues() {
		return new float[] {on?1:0};
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String[] getValueNames() {
		return streamName;
	}

	@Override
	public void step() {
		on=touchSensor.isPressed();
	}

}
