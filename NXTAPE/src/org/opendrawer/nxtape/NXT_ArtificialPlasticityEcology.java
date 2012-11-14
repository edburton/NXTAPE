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

	int dataStreamWidth = 100;
	static int dataStreamCount = 7;
	DataStream[] dataStreams;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] {/* "--present", */"org.opendrawer.nxtape.NXT_ArtificialPlasticityEcology" });
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

		dataStreams = new DataStream[dataStreamCount];
		dataStreams[0] = new DataStream(dataStreamWidth, "top touch sensor",
				new Color(255, 128, 0));
		dataStreams[1] = new DataStream(dataStreamWidth, "bottom touch sensor",
				new Color(255, 128, 0));
		dataStreams[2] = new DataStream(dataStreamWidth, "right touch sensor",
				new Color(255, 128, 0));
		dataStreams[3] = new DataStream(dataStreamWidth, "left touch sensor",
				new Color(255, 255, 0));
		dataStreams[4] = new DataStream(dataStreamWidth,
				armHeadMotor.getName(), armHeadMotor.getColour());
		dataStreams[5] = new DataStream(dataStreamWidth,
				armMiddleMotor.getName(), armMiddleMotor.getColour());
		dataStreams[6] = new DataStream(dataStreamWidth,
				armBodyMotor.getName(), armBodyMotor.getColour());

		println("---");
	}

	public void setup() {
		setupNXT();
		size(1000, 700);
		frameRate(50);
		ellipseMode(RADIUS);
		rectMode(CORNERS);
		background(0);
		frame.setBackground(new java.awt.Color(0, 0, 0));
	}

	public void draw() {
		background(32);
		stroke(64, 64, 64);
		strokeWeight(5);
		noFill();
		ellipse(200, 200, 100, 100);

		//touchTop.step();
		touchBottom.step();
		touchLeft.step();
		touchRight.step();

		boolean top = false;// touchTop.isPressed();
		boolean bottom = touchBottom.getNormalizedValues()[0] != 0;
		boolean left = touchLeft.getNormalizedValues()[0] != 0;
		boolean right = touchRight.getNormalizedValues()[0] != 0;

		if (top) {
			fill(255, 128, 0);
			armHeadMotor.accelerate(1);

		} else
			fill(0, 0, 0);
		ellipse(200, 100, 50, 50);
		if (bottom) {
			fill(255, 128, 0);
			armHeadMotor.accelerate(-0.5f);
		} else {
			fill(0, 0, 0);
			armHeadMotor.accelerate(0.01f);
		}
		ellipse(200, 300, 50, 50);
		if (left) {
			fill(255, 128, 0);
			armMiddleMotor.accelerate(-0.5f);
			armBodyMotor.accelerate(-0.5f);
		} else
			fill(0, 0, 0);
		ellipse(100, 200, 50, 50);
		if (right) {
			fill(255, 128, 0);

			armMiddleMotor.accelerate(+0.5f);
			armBodyMotor.accelerate(+0.5f);
		} else
			fill(0, 0, 0);
		ellipse(300, 200, 50, 50);

		armHeadMotor.step();
		armBodyMotor.step();
		armMiddleMotor.step();

		fill(0, 0, 0);

		float headA = (float) ((armHeadMotor.getNormalizedValues()[0]) * TWO_PI)
				+ PI;
		float middleA = (float) ((armMiddleMotor.getNormalizedValues()[0]) * TWO_PI)
				+ PI;
		float bodyA = (float) ((armBodyMotor.getNormalizedValues()[0]) * TWO_PI)
				+ PI;
		stroke(64, 64, 64);
		ellipse(500, 200, 50, 50);
		stroke(255, 0, 0);
		line(500, 200, 500 + sin(headA) * 50, 200 + cos(headA) * 50);
		stroke(64, 64, 64);
		ellipse(650, 200, 50, 50);
		stroke(0, 255, 0);
		line(650, 200, 650 + sin(middleA) * 50, 200 + cos(middleA) * 50);
		stroke(64, 64, 64);
		ellipse(800, 200, 50, 50);
		stroke(0, 0, 255);
		line(800, 200, 800 + sin(bodyA) * 50, 200 + cos(bodyA) * 50);

		dataStreams[0].write(top ? 1 : 0);
		dataStreams[1].write(bottom ? 1 : 0);
		dataStreams[2].write(left ? 1 : 0);
		dataStreams[3].write(right ? 1 : 0);
		dataStreams[4].write(armHeadMotor.getNormalizedValues()[0]);
		dataStreams[5].write(armMiddleMotor.getNormalizedValues()[0]);
		dataStreams[6].write(armBodyMotor.getNormalizedValues()[0]);

		noStroke();
		for (int n = 0; n < 7; n++) {
			Color c = dataStreams[n].getColor();
			fill(c.getRGB());
			float xx = 0, yy = 0;
			for (int i = 0; i < dataStreamWidth; i++) {
				float v = dataStreams[n].read(i);
				float x = (50 + ((dataStreamWidth - i) / (float) dataStreamWidth) * 900);
				float y = 650 - v * 250;

				if (i > 0) {
					rect(xx + 2.5f, yy - 2.5f, x - 2.5f, yy + 2.5f, 2.5f);
				}
				xx = x;
				yy = y;
			}

		}

		if (++debugCounter % 1000 == 0)
			println("frameRate: " + frameRate);
	}
}
