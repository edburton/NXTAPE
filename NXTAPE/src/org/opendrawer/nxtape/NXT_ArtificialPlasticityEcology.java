package org.opendrawer.nxtape;

import java.awt.Color;
import java.util.ArrayList;

import processing.core.*;
import lejos.nxt.*;
import lejos.pc.comm.*;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.remote.RemoteMotor;

@SuppressWarnings("serial")
public class NXT_ArtificialPlasticityEcology extends PApplet {
	NXTConnectionManager NXTcm;
	NXTInfo[] NXTs;
	NXTTouchSensor touchTop;
	NXTTouchSensor touchLeft;
	NXTTouchSensor touchBottom;
	NXTTouchSensor touchRight;
	NXTMotor armHeadMotor;
	NXTMotor armMiddleMotor;
	NXTMotor armBodyMotor;
	int debugCounter = 0;
	int motorCentre = 0;

	int dataStreamWidth = 256;
	static int dataStreamCount = 7;
	GraphicalDataStream[] dataStreams;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] { "--present",
				"org.opendrawer.nxtape.NXT_ArtificialPlasticityEcology" });
	}

	private void setupNXT() {
		NXTcm = new NXTConnectionManager();
		println("-NXTConnectionManager instantiated-");
		NXTs = NXTcm.search();
		println("---");
		for (int i = 0; i < NXTs.length; i++)
			println(NXTs[i].name);

		// touchTop = new TouchSensor(SensorPort.S1);
		touchLeft = new NXTTouchSensor(new TouchSensor(SensorPort.S2),
				"Touch Left");
		touchBottom = new NXTTouchSensor(new TouchSensor(SensorPort.S3),
				"Touch Bottom");
		touchRight = new NXTTouchSensor(new TouchSensor(SensorPort.S4),
				"Touch Right");

		armHeadMotor = new NXTMotor(Motor.A, "arm head motor", new Color(255,
				0, 0), -180, 0, 0, 0.9f);
		armMiddleMotor = new NXTMotor(Motor.B, "arm midle motor", new Color(0,
				255, 0), -60, 60, 0, 0.9f);
		armBodyMotor = new NXTMotor(Motor.C, "arm body motor", new Color(0, 0,
				255), -60, 60, 0, 0.9f);

		dataStreams = new GraphicalDataStream[dataStreamCount];
		// dataStreams[0] = new GraphicalDataStream(dataStreamWidth,
		// "top touch sensor",Color.HSBtoRGB(0f, 1.0f, 1.0f));
		dataStreams[1] = new GraphicalDataStream(dataStreamWidth, touchBottom);
		dataStreams[2] = new GraphicalDataStream(dataStreamWidth, touchLeft);
		dataStreams[3] = new GraphicalDataStream(dataStreamWidth, touchRight);
		dataStreams[4] = new GraphicalDataStream(dataStreamWidth, armHeadMotor);
		dataStreams[5] = new GraphicalDataStream(dataStreamWidth,
				armMiddleMotor);
		dataStreams[6] = new GraphicalDataStream(dataStreamWidth, armBodyMotor);

		println("---");
	}

	public void setup() {
		setupNXT();
		size(800, 600);
		frameRate(50);
		ellipseMode(CORNERS);
		rectMode(CORNERS);
		background(0);
		frame.setBackground(new java.awt.Color(0, 0, 0));
	}

	public void draw() {
		background(32);

		// touchTop.step();
		touchBottom.step();
		touchLeft.step();
		touchRight.step();

		boolean top = false;// touchTop.isPressed();
		boolean bottom = touchBottom.getNormalizedValues()[0] != 0;
		boolean left = touchLeft.getNormalizedValues()[0] != 0;
		boolean right = touchRight.getNormalizedValues()[0] != 0;

		if (top) {
			armHeadMotor.accelerate(1);

		}
		if (bottom) {
			armHeadMotor.accelerate(-0.5f);
		} else {
			armHeadMotor.accelerate(0.01f);
		}
		if (left) {
			armMiddleMotor.accelerate(-0.5f);
			armBodyMotor.accelerate(-0.5f);
		}
		if (right) {

			armMiddleMotor.accelerate(+0.5f);
			armBodyMotor.accelerate(+0.5f);
		}

		armHeadMotor.step();
		armBodyMotor.step();
		armMiddleMotor.step();

		// dataStreams[0].write(top ? 1 : 0);
		dataStreams[1].write(touchBottom.getNormalizedValues());
		dataStreams[2].write(touchRight.getNormalizedValues());
		dataStreams[3].write(touchLeft.getNormalizedValues());
		dataStreams[4].write(armHeadMotor.getNormalizedValues());
		dataStreams[5].write(armMiddleMotor.getNormalizedValues());
		dataStreams[6].write(armBodyMotor.getNormalizedValues());

		float height = 520 / 6;
		for (int n = 1; n < 7; n++) {
			float y = 20 + ((n - 1.0f) / 6) * 560;
			dataStreams[n].drawAt(g, 20, y, 760, height);
		}

		if (++debugCounter % 1000 == 0)
			println("frameRate: " + frameRate);
	}
}
