package org.opendrawer.nxtape;

import java.util.ArrayList;
import java.util.List;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.AccelHTSensor;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

import org.opendrawer.dawinian.neurodynamics.DataStreamCore;
import org.opendrawer.dawinian.neurodynamics.HomogeneousDataStreamBundle;
import org.opendrawer.dawinian.neurodynamics.Reflex;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class NXT_ArtificialPlasticityEcology extends PApplet {
	private static final boolean presentationMode = false;

	private NXTComm nxtComm;
	private NXTInfo[] NXTs;
	private NXTAccelerometer accelerometer;
	// private NXTCompass compass;
	// private NXTTouchSensor touchTop;
	private NXTTouchSensor touchLeft;
	private NXTTouchSensor touchBottom;
	private NXTTouchSensor touchRight;
	private NXTMotor armHeadMotor;
	private NXTMotor armMiddleMotor;
	private NXTMotor armBodyMotor;
	private int debugCounter = 0;
	private int dataStreamWidth = 256;
	private final DataStreamCore dataStreamCore = new DataStreamCore();
	private List<HomogeneousDataStreamBundleRenderer> homogeneousDataStreamBundleRenderers = new ArrayList<HomogeneousDataStreamBundleRenderer>();
	private boolean dummyMode = false;
	public static float lineWidth;
	private InteractiveRenderer mouseFocusedRenderer;
	private static final String touch_left_name = "Touch Left";
	private static final String touch_bottom_name = "Touch Bottom";
	private static final String touch_right_name = "Touch Right";
	private static final String accelerometer_name = "Accelerometer";
	private static final String compass_name = "Compass";

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
				SensorPort.S1) : null, accelerometer_name);
		// compass = new NXTCompass(!dummyMode ? new CompassHTSensor(
		// SensorPort.S3) : null, compass_name);
		// touchTop = new NXTTouchSensor(!dummyMode ? new TouchSensor(
		// SensorPort.S1) : null, "Touch Top");
		touchLeft = new NXTTouchSensor(!dummyMode ? new TouchSensor(
				SensorPort.S2) : null, touch_left_name);
		touchBottom = new NXTTouchSensor(!dummyMode ? new TouchSensor(
				SensorPort.S3) : null, touch_bottom_name);
		touchRight = new NXTTouchSensor(!dummyMode ? new TouchSensor(
				SensorPort.S4) : null, touch_right_name);

		armHeadMotor = new NXTMotor(!dummyMode ? Motor.A : null,
				"arm head motor", -180, 0, 0, 0.9f);
		armMiddleMotor = new NXTMotor(!dummyMode ? Motor.B : null,
				"arm midle motor", -60, 60, 0, 0.9f);
		armBodyMotor = new NXTMotor(!dummyMode ? Motor.C : null,
				"arm body motor", -60, 60, 0, 0.9f);

		float screenWidth = getWidth();
		float screenHeight = getHeight();
		float edgeMargin = screenWidth / 50;
		float margin = screenWidth / 100;
		float height = ((screenHeight - edgeMargin * 2) / 7) - margin;
		float y = edgeMargin;
		float width = screenWidth - edgeMargin * 2;

		HomogeneousDataStreamBundle bottomDataStreamBundle;
		HomogeneousDataStreamBundle leftDataStreamBundle;
		HomogeneousDataStreamBundle rightDataStreamBundle;
		HomogeneousDataStreamBundle accelerometerDataStreamBundle;
		HomogeneousDataStreamBundle armHeadMotorDataStreamBundle;
		HomogeneousDataStreamBundle armMiddleMotorDataStreamBundle;
		HomogeneousDataStreamBundle armBodyMotorDataStreamBundle;

		homogeneousDataStreamBundleRenderers
				.add(new HomogeneousDataStreamBundleRenderer(
						accelerometerDataStreamBundle = new HomogeneousDataStreamBundle(
								accelerometer, dataStreamWidth),
						new NXTAccelerometerRenderer(accelerometer),
						edgeMargin, y, width, height));
		y += height + margin;
		// homogeneousDataStreamBundleRenderers.add(new
		// HomogeneousDataStreamBundleRenderer(
		// new DataStreamBundle(compass, dataStreamWidth),
		// new NXTCompassRenderer(compass), edgeMargin, y,
		// width, height));
		// y += height + margin;
		// homogeneousDataStreamBundleRenderers.add(new
		// HomogeneousDataStreamBundleRenderer(
		// new DataStreamBundle(touchTop, dataStreamWidth),
		// new NXTTouchSensorRenderer(touchTop), edgeMargin, y, width,
		// height));
		// y += height + margin;

		homogeneousDataStreamBundleRenderers
				.add(new HomogeneousDataStreamBundleRenderer(
						bottomDataStreamBundle = new HomogeneousDataStreamBundle(
								touchBottom, dataStreamWidth),
						new NXTTouchSensorRenderer(touchBottom), edgeMargin, y,
						width, height));
		y += height + margin;
		homogeneousDataStreamBundleRenderers
				.add(new HomogeneousDataStreamBundleRenderer(
						leftDataStreamBundle = new HomogeneousDataStreamBundle(
								touchLeft, dataStreamWidth),
						new NXTTouchSensorRenderer(touchLeft), edgeMargin, y,
						width, height));
		y += height + margin;
		homogeneousDataStreamBundleRenderers
				.add(new HomogeneousDataStreamBundleRenderer(
						rightDataStreamBundle = new HomogeneousDataStreamBundle(
								touchRight, dataStreamWidth),
						new NXTTouchSensorRenderer(touchRight), edgeMargin, y,
						width, height));
		y += height + margin;
		homogeneousDataStreamBundleRenderers
				.add(new HomogeneousDataStreamBundleRenderer(
						armHeadMotorDataStreamBundle = new HomogeneousDataStreamBundle(
								armHeadMotor, dataStreamWidth),
						new NXTMotorRenderer(armHeadMotor), edgeMargin, y,
						width, height));
		y += height + margin;
		homogeneousDataStreamBundleRenderers
				.add(new HomogeneousDataStreamBundleRenderer(
						armMiddleMotorDataStreamBundle = new HomogeneousDataStreamBundle(
								armMiddleMotor, dataStreamWidth),
						new NXTMotorRenderer(armMiddleMotor), edgeMargin, y,
						width, height));
		y += height + margin;
		homogeneousDataStreamBundleRenderers
				.add(new HomogeneousDataStreamBundleRenderer(
						armBodyMotorDataStreamBundle = new HomogeneousDataStreamBundle(
								armBodyMotor, dataStreamWidth),
						new NXTMotorRenderer(armBodyMotor), edgeMargin, y,
						width, height));

		for (int i = 0; i < homogeneousDataStreamBundleRenderers.size(); i++)
			dataStreamCore
					.addDataStreamBundle(homogeneousDataStreamBundleRenderers
							.get(i).getDataStreamBundle());

		dataStreamCore.addReflex(new Reflex(accelerometerDataStreamBundle,
				armHeadMotorDataStreamBundle, 1, 0, -1));

		dataStreamCore.addReflex(new Reflex(bottomDataStreamBundle,
				armHeadMotorDataStreamBundle, 0, 0, -1));
		dataStreamCore.addReflex(new Reflex(leftDataStreamBundle,
				armMiddleMotorDataStreamBundle, 0, 0, -0.5d));
		dataStreamCore.addReflex(new Reflex(rightDataStreamBundle,
				armMiddleMotorDataStreamBundle, 0, 0, 0.5d));
		dataStreamCore.addReflex(new Reflex(leftDataStreamBundle,
				armBodyMotorDataStreamBundle, 0, 0, -0.5d));
		dataStreamCore.addReflex(new Reflex(rightDataStreamBundle,
				armBodyMotorDataStreamBundle, 0, 0, 0.5d));

		dataStreamCore.prepareDataStreams();
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

		dataStreamCore.stepInputs();

		armHeadMotor.setOutputChannel(0.0d, 0);
		armMiddleMotor.setOutputChannel(0.0d, 0);
		armBodyMotor.setOutputChannel(0.0d, 0);

		dataStreamCore.stepOutputs();

		for (int i = 0; i < homogeneousDataStreamBundleRenderers.size(); i++) {
			homogeneousDataStreamBundleRenderers.get(i).draw(g);
		}

		if (++debugCounter % 100 == 0)
			println("frameRate: " + frameRate);

		if (dummyMode) {
			fill(255, 0, 0, (cos(debugCounter / 20.0f) + 1.0f) * 128);
			text("NXT not found", 2, 12);
		}
	}

	@Override
	public void mouseClicked() {
		for (int i = 0; i < homogeneousDataStreamBundleRenderers.size(); i++)
			if (homogeneousDataStreamBundleRenderers.get(i).contains(mouseX,
					mouseY))
				homogeneousDataStreamBundleRenderers.get(i).mouseClicked(
						mouseX, mouseY);
	}

	@Override
	public void mousePressed() {
		for (int i = 0; i < homogeneousDataStreamBundleRenderers.size(); i++)
			if (homogeneousDataStreamBundleRenderers.get(i).contains(mouseX,
					mouseY)) {
				mouseFocusedRenderer = homogeneousDataStreamBundleRenderers
						.get(i);
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
