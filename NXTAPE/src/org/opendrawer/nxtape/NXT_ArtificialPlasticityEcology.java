package org.opendrawer.nxtape;

import java.awt.Color;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class NXT_ArtificialPlasticityEcology extends PApplet {
	private NXTComm nxtComm;
	private NXTInfo[] NXTs;
	private NXTTouchSensor touchTop;
	private NXTTouchSensor touchLeft;
	private NXTTouchSensor touchBottom;
	private NXTTouchSensor touchRight;
	private NXTMotor armHeadMotor;
	private NXTMotor armMiddleMotor;
	private NXTMotor armBodyMotor;
	private int debugCounter = 0;
	private int dataStreamWidth = 256;
	private static int dataStreamCount = 7;
	private GraphicalDataStream[] dataStreams;
	private boolean dummyMode = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean present = true;
		if (present)
			PApplet.main(new String[] { "--present",
					"org.opendrawer.nxtape.NXT_ArtificialPlasticityEcology" });
		else
			PApplet.main("org.opendrawer.nxtape.NXT_ArtificialPlasticityEcology");
	}

	private void setupNXT() {
		try {
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
			NXTs = nxtComm.search(null);
		} catch (NXTCommException e) {
			e.printStackTrace();
		}
		dummyMode = NXTs.length != 1;

		if (dummyMode)
			println(NXTs.length + " NXTs found. Initiating Dummy mode");

		touchTop = new NXTTouchSensor(!dummyMode ? new TouchSensor(
				SensorPort.S1) : null, "Touch Top");
		touchLeft = new NXTTouchSensor(!dummyMode ? new TouchSensor(
				SensorPort.S2) : null, "Touch Left");
		touchBottom = new NXTTouchSensor(!dummyMode ? new TouchSensor(
				SensorPort.S3) : null, "Touch Bottom");
		touchRight = new NXTTouchSensor(!dummyMode ? new TouchSensor(
				SensorPort.S4) : null, "Touch Right");

		armHeadMotor = new NXTMotor(!dummyMode ? Motor.A : null,
				"arm head motor", new Color(255, 0, 0), -180, 0, 0, 0.9f);
		armMiddleMotor = new NXTMotor(!dummyMode ? Motor.B : null,
				"arm midle motor", new Color(0, 255, 0), -60, 60, 0, 0.9f);
		armBodyMotor = new NXTMotor(!dummyMode ? Motor.C : null,
				"arm body motor", new Color(0, 0, 255), -60, 60, 0, 0.9f);

		dataStreams = new GraphicalDataStream[dataStreamCount];
		dataStreams[0] = new GraphicalDataStream(dataStreamWidth, touchTop);
		dataStreams[1] = new GraphicalDataStream(dataStreamWidth, touchBottom);
		dataStreams[2] = new GraphicalDataStream(dataStreamWidth, touchLeft);
		dataStreams[3] = new GraphicalDataStream(dataStreamWidth, touchRight);
		dataStreams[4] = new GraphicalDataStream(dataStreamWidth, armHeadMotor);
		dataStreams[5] = new GraphicalDataStream(dataStreamWidth,
				armMiddleMotor);
		dataStreams[6] = new GraphicalDataStream(dataStreamWidth, armBodyMotor);
	}

	@Override
	public void setup() {
		setupNXT();
		size(800, 600);
		frameRate(50);
		ellipseMode(CORNERS);
		rectMode(CORNERS);
		background(0);
		frame.setBackground(new java.awt.Color(0, 0, 0));
	}

	@Override
	public void draw() {
		background(0);
		touchTop.startStep();
		touchBottom.startStep();
		touchLeft.startStep();
		touchRight.startStep();

		boolean top = touchTop.getNormalizedValues()[0] != 0;
		boolean bottom = touchBottom.getNormalizedValues()[0] != 0;
		boolean left = touchLeft.getNormalizedValues()[0] != 0;
		boolean right = touchRight.getNormalizedValues()[0] != 0;

		if (top) {
			armHeadMotor.setInputChannels(new float[] { 0.01f });
		}
		if (bottom) {
			armHeadMotor.setInputChannels(new float[] { -0.01f });
		} else {
			armHeadMotor.setInputChannels(new float[] { 0.001f });
		}
		if (left) {
			armMiddleMotor.setInputChannels(new float[] { -0.01f });
			armBodyMotor.setInputChannels(new float[] { -0.01f });
		}
		if (right) {

			armMiddleMotor.setInputChannels(new float[] { 0.01f });
			armBodyMotor.setInputChannels(new float[] { 0.01f });
		}

		armHeadMotor.startStep();
		armBodyMotor.startStep();
		armMiddleMotor.startStep();

		dataStreams[0].write(touchTop.getNormalizedValues());
		dataStreams[1].write(touchBottom.getNormalizedValues());
		dataStreams[2].write(touchRight.getNormalizedValues());
		dataStreams[3].write(touchLeft.getNormalizedValues());
		dataStreams[4].write(armHeadMotor.getNormalizedValues());
		dataStreams[5].write(armMiddleMotor.getNormalizedValues());
		dataStreams[6].write(armBodyMotor.getNormalizedValues());

		float height = 440 / 7;
		for (int n = 0; n < 7; n++) {
			float y = 20 + (n / 7.0f) * (580);
			dataStreams[n].drawAt(g, 20, y, 760, height);
		}

		if (++debugCounter % 1000 == 0)
			println("frameRate: " + frameRate);

		if (dummyMode) {
			fill(255, 0, 0, (cos(debugCounter / 20.0f) + 1.0f) * 128);
			text("NXT not found", 2, 12);
		}

		touchTop.finishStep();
		touchBottom.finishStep();
		touchLeft.finishStep();
		touchRight.finishStep();
		armHeadMotor.finishStep();
		armBodyMotor.finishStep();
		armMiddleMotor.finishStep();
	}
}
