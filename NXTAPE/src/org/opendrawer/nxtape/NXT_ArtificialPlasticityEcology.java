package org.opendrawer.nxtape;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.AccelHTSensor;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class NXT_ArtificialPlasticityEcology extends PApplet {
	private static final boolean presentationMode = false;

	private NXTComm nxtComm;
	private NXTInfo[] NXTs;
	private NXTAccelerometer accelerometer;
	// private NXTTouchSensor touchTop;
	private NXTTouchSensor touchLeft;
	private NXTTouchSensor touchBottom;
	private NXTTouchSensor touchRight;
	private NXTMotor armHeadMotor;
	private NXTMotor armMiddleMotor;
	private NXTMotor armBodyMotor;
	private int debugCounter = 0;
	private static final int dataStreamWidth = 256;
	private static int dataStreamCount = 7;
	private DataStreamsRenderer[] dataStreamGraphicalRenderers;
	private float[][] sensoryData;
	private boolean dummyMode = false;
	public static float lineWidth;
	private InteractiveRenderer mouseFocusedRenderer;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (presentationMode)
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

		accelerometer = new NXTAccelerometer(!dummyMode ? new AccelHTSensor(
				SensorPort.S1) : null, "Accelerometer");
		// touchTop = new NXTTouchSensor(!dummyMode ? new TouchSensor(
		// SensorPort.S1) : null, "Touch Top");
		touchLeft = new NXTTouchSensor(!dummyMode ? new TouchSensor(
				SensorPort.S2) : null, "Touch Left");
		touchBottom = new NXTTouchSensor(!dummyMode ? new TouchSensor(
				SensorPort.S3) : null, "Touch Bottom");
		touchRight = new NXTTouchSensor(!dummyMode ? new TouchSensor(
				SensorPort.S4) : null, "Touch Right");

		armHeadMotor = new NXTMotor(!dummyMode ? Motor.A : null,
				"arm head motor", -180, 0, 0, 0.95f);
		armMiddleMotor = new NXTMotor(!dummyMode ? Motor.B : null,
				"arm midle motor", -60, 60, 0, 0.95f);
		armBodyMotor = new NXTMotor(!dummyMode ? Motor.C : null,
				"arm body motor", -60, 60, 0, 0.95f);

		float screenWidth = getWidth();
		float screenHeight = getHeight();
		float edgeMargin = screenWidth / 50;
		float margin = screenWidth / 100;
		float height = ((screenHeight - edgeMargin * 2) / 7) - margin;
		float y = edgeMargin;
		float width = screenWidth - edgeMargin * 2;

		dataStreamGraphicalRenderers = new DataStreamsRenderer[dataStreamCount];

		dataStreamGraphicalRenderers[0] = new DataStreamsRenderer(accelerometer,
				dataStreamWidth, new NXTAccelerometerRenderer(accelerometer),
				edgeMargin, y, width, height);
		y += height + margin;
		// dataStreamGraphicalRenderers[0] = new DataStreamRenderer(
		// new DataStream(dataStreamWidth, touchTop),
		// new NXTTouchSensorRenderer(touchTop), edgeMargin, y, width,
		// height);
		// y += height + margin;
		dataStreamGraphicalRenderers[1] = new DataStreamsRenderer(
				new DataStream(dataStreamWidth, touchBottom),
				new NXTTouchSensorRenderer(touchBottom), edgeMargin, y, width,
				height);
		y += height + margin;
		dataStreamGraphicalRenderers[2] = new DataStreamsRenderer(
				new DataStream(dataStreamWidth, touchLeft),
				new NXTTouchSensorRenderer(touchLeft), edgeMargin, y, width,
				height);
		y += height + margin;
		dataStreamGraphicalRenderers[3] = new DataStreamsRenderer(
				new DataStream(dataStreamWidth, touchRight),
				new NXTTouchSensorRenderer(touchRight), edgeMargin, y, width,
				height);
		y += height + margin;
		dataStreamGraphicalRenderers[4] = new DataStreamsRenderer(
				new DataStream(dataStreamWidth, armHeadMotor),
				new NXTMotorRenderer(armHeadMotor), edgeMargin, y, width,
				height);
		y += height + margin;
		dataStreamGraphicalRenderers[5] = new DataStreamsRenderer(
				new DataStream(dataStreamWidth, armMiddleMotor),
				new NXTMotorRenderer(armMiddleMotor), edgeMargin, y, width,
				height);
		y += height + margin;
		dataStreamGraphicalRenderers[6] = new DataStreamsRenderer(
				new DataStream(dataStreamWidth, armBodyMotor),
				new NXTMotorRenderer(armBodyMotor), edgeMargin, y, width,
				height);
	}

	@Override
	public void setup() {
		if (presentationMode)
			size(displayWidth, displayHeight, OPENGL);
		else
			size(displayWidth / 2, displayHeight / 2, OPENGL);
		frameRate(200);
		lineWidth = getHeight() / 128.0f;
		smooth();
		frameRate(50);
		ellipseMode(CORNERS);
		rectMode(CORNERS);
		background(0);
		frame.setBackground(new java.awt.Color(0, 0, 0));
		setupNXT();
	}

	@Override
	public void draw() {
		background(0);
		accelerometer.startStep();
		// touchTop.startStep();
		touchBottom.startStep();
		touchLeft.startStep();
		touchRight.startStep();

		// boolean top = touchTop.getNormalizedValues()[0] != 0;
		boolean bottom = touchBottom.getNormalizedValues()[0] != 0;
		boolean left = touchLeft.getNormalizedValues()[0] != 0;
		boolean right = touchRight.getNormalizedValues()[0] != 0;

		// if (top) {
		// armHeadMotor.setInputChannels(new float[] { 1.0f });
		// }
		if (bottom) {
			armHeadMotor.setInputChannels(new float[] { -1.0f });
		} else {
			armHeadMotor.setInputChannels(new float[] { 0.1f });
		}
		if (left) {
			armMiddleMotor.setInputChannels(new float[] { -1.0f });
			armBodyMotor.setInputChannels(new float[] { -1.0f });
		}
		if (right) {

			armMiddleMotor.setInputChannels(new float[] { 1.0f });
			armBodyMotor.setInputChannels(new float[] { 1.0f });
		}

		armHeadMotor.startStep();
		armBodyMotor.startStep();
		armMiddleMotor.startStep();

		dataStreamGraphicalRenderers[0].getDataStreams().write(
				accelerometer.getNormalizedValues());
		// dataStreamGraphicalRenderers[0].getDataStream().write(
		// touchTop.getNormalizedValues());
		dataStreamGraphicalRenderers[1].getDataStreams().write(
				touchBottom.getNormalizedValues());
		dataStreamGraphicalRenderers[2].getDataStreams().write(
				touchLeft.getNormalizedValues());
		dataStreamGraphicalRenderers[3].getDataStreams().write(
				touchRight.getNormalizedValues());
		dataStreamGraphicalRenderers[4].getDataStreams().write(
				armHeadMotor.getNormalizedValues());
		dataStreamGraphicalRenderers[5].getDataStreams().write(
				armMiddleMotor.getNormalizedValues());
		dataStreamGraphicalRenderers[6].getDataStreams().write(
				armBodyMotor.getNormalizedValues());

		for (int n = 0; n < dataStreamGraphicalRenderers.length; n++) {
			dataStreamGraphicalRenderers[n].draw(g);
		}

		if (++debugCounter % 100 == 0)
			println("frameRate: " + frameRate);

		if (dummyMode) {
			fill(255, 0, 0, (cos(debugCounter / 20.0f) + 1.0f) * 128);
			text("NXT not found", 2, 12);
		}

		accelerometer.finishStep();
		// touchTop.finishStep();
		touchBottom.finishStep();
		touchLeft.finishStep();
		touchRight.finishStep();
		armHeadMotor.finishStep();
		armBodyMotor.finishStep();
		armMiddleMotor.finishStep();
	}

	@Override
	public void mouseClicked() {
		for (int n = 0; n < dataStreamGraphicalRenderers.length; n++)
			if (dataStreamGraphicalRenderers[n].contains(mouseX, mouseY))
				dataStreamGraphicalRenderers[n].mouseClicked(mouseX, mouseY);
	}

	@Override
	public void mousePressed() {
		for (int n = 0; n < dataStreamGraphicalRenderers.length; n++)
			if (dataStreamGraphicalRenderers[n].contains(mouseX, mouseY)) {
				mouseFocusedRenderer = dataStreamGraphicalRenderers[n];
				mouseFocusedRenderer.mousePressed(mouseX, mouseY);
			}
	}

	@Override
	public void mouseDragged() {
		if (mouseFocusedRenderer != null)
			mouseFocusedRenderer.mouseDragged(mouseX, mouseY);
	}

	@Override
	public void mouseReleased() {
		if (mouseFocusedRenderer != null) {
			mouseFocusedRenderer.mouseReleased(mouseX, mouseY);
			mouseFocusedRenderer = null;
		}
	}
}
