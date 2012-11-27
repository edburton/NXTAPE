package org.opendrawer.nxtape;

import java.awt.Color;
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

import org.opendrawer.dawinian.neurodynamics.Actor;
import org.opendrawer.dawinian.neurodynamics.DataStreamCore;
import org.opendrawer.dawinian.neurodynamics.HomogeneousDataStreamBundle;
import org.opendrawer.dawinian.neurodynamics.Predictor;
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
	private int dataStreamWidth = 64;
	private final DataStreamCore dataStreamCore = new DataStreamCore();
	private boolean dummyMode = false;
	public static float lineWidth;
	public static float lineMarginWidth;
	private Renderer mouseFocusedRenderer;
	private static final String touch_left_name = "Touch Left";
	private static final String touch_bottom_name = "Touch Bottom";
	private static final String touch_right_name = "Touch Right";
	private static final String accelerometer_name = "Accelerometer";
	// private static final String compass_name = "Compass";
	private List<Renderer> renderers = new ArrayList<Renderer>();

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

	@Override
	public void setup() {
		if (presentationMode)
			size(displayWidth, displayHeight, OPENGL);
		else
			size(1024, 768, OPENGL);
		frameRate(200);
		lineMarginWidth = getHeight() / 384.0f;
		lineWidth = lineMarginWidth * 1.70710678118655f;
		smooth();
		frameRate(50);
		ellipseMode(CORNERS);
		rectMode(CORNERS);
		background(0);
		frame.setBackground(new java.awt.Color(0, 0, 0));
		setupNXT();
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

		List<HomogeneousDataStreamBundleRenderer> homogeneousDataStreamBundleRenderers = new ArrayList<HomogeneousDataStreamBundleRenderer>();
		List<DataStreamBundleMapRenderer> reflexRenderers = new ArrayList<DataStreamBundleMapRenderer>();
		List<DataStreamBundleMapRenderer> predictorRenderers = new ArrayList<DataStreamBundleMapRenderer>();
		List<DataStreamBundleMapRenderer> actorRenderers = new ArrayList<DataStreamBundleMapRenderer>();

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
						new NXTAccelerometerRenderer(accelerometer)));
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
						new NXTTouchSensorRenderer(touchBottom)));
		homogeneousDataStreamBundleRenderers
				.add(new HomogeneousDataStreamBundleRenderer(
						leftDataStreamBundle = new HomogeneousDataStreamBundle(
								touchLeft, dataStreamWidth),
						new NXTTouchSensorRenderer(touchLeft)));
		homogeneousDataStreamBundleRenderers
				.add(new HomogeneousDataStreamBundleRenderer(
						rightDataStreamBundle = new HomogeneousDataStreamBundle(
								touchRight, dataStreamWidth),
						new NXTTouchSensorRenderer(touchRight)));
		homogeneousDataStreamBundleRenderers
				.add(new HomogeneousDataStreamBundleRenderer(
						armHeadMotorDataStreamBundle = new HomogeneousDataStreamBundle(
								armHeadMotor, dataStreamWidth),
						new NXTMotorRenderer(armHeadMotor)));
		homogeneousDataStreamBundleRenderers
				.add(new HomogeneousDataStreamBundleRenderer(
						armMiddleMotorDataStreamBundle = new HomogeneousDataStreamBundle(
								armMiddleMotor, dataStreamWidth),
						new NXTMotorRenderer(armMiddleMotor)));
		homogeneousDataStreamBundleRenderers
				.add(new HomogeneousDataStreamBundleRenderer(
						armBodyMotorDataStreamBundle = new HomogeneousDataStreamBundle(
								armBodyMotor, dataStreamWidth),
						new NXTMotorRenderer(armBodyMotor)));

		for (int i = 0; i < homogeneousDataStreamBundleRenderers.size(); i++)
			dataStreamCore
					.addDataStreamBundle(homogeneousDataStreamBundleRenderers
							.get(i).getDataStreamBundle());

		Reflex accelerometerReflex;
		Reflex bottomReflexM;
		Reflex leftReflexM;
		Reflex rightReflexM;
		Reflex leftReflexB;
		Reflex rightReflexB;

		dataStreamCore.addReflex(accelerometerReflex = new Reflex(
				accelerometerDataStreamBundle, armHeadMotorDataStreamBundle, 1,
				0, -1));
		dataStreamCore
				.addReflex(bottomReflexM = new Reflex(bottomDataStreamBundle,
						armHeadMotorDataStreamBundle, 0, 0, -1));
		dataStreamCore.addReflex(leftReflexM = new Reflex(leftDataStreamBundle,
				armMiddleMotorDataStreamBundle, 0, 0, -0.5d));
		dataStreamCore.addReflex(rightReflexM = new Reflex(
				rightDataStreamBundle, armMiddleMotorDataStreamBundle, 0, 0,
				0.5d));
		dataStreamCore.addReflex(leftReflexB = new Reflex(leftDataStreamBundle,
				armBodyMotorDataStreamBundle, 0, 0, -0.5d));
		dataStreamCore
				.addReflex(rightReflexB = new Reflex(rightDataStreamBundle,
						armBodyMotorDataStreamBundle, 0, 0, 0.5d));

		reflexRenderers
				.add(new DataStreamBundleMapRenderer(accelerometerReflex));
		reflexRenderers.add(new DataStreamBundleMapRenderer(bottomReflexM));
		reflexRenderers.add(new DataStreamBundleMapRenderer(leftReflexM));
		reflexRenderers.add(new DataStreamBundleMapRenderer(rightReflexM));
		reflexRenderers.add(new DataStreamBundleMapRenderer(leftReflexB));
		reflexRenderers.add(new DataStreamBundleMapRenderer(rightReflexB));

		for (int i = 0; i < reflexRenderers.size(); i++)
			reflexRenderers.get(i).setKeyColor(new Color(0, 128, 0));

		for (int i = 0; i < 5; i++) {
			Actor actor = new Actor(null, null);
			dataStreamCore.addActor(actor);
			DataStreamBundleMapRenderer renderer = new DataStreamBundleMapRenderer(
					actor);
			renderer.setKeyColor(new Color(128, 64, 0));
			actorRenderers.add(renderer);
		}

		for (int i = 0; i < 15; i++) {
			Predictor predictor = new Predictor(null, null);
			dataStreamCore.addPredictor(predictor);
			DataStreamBundleMapRenderer renderer = new DataStreamBundleMapRenderer(
					predictor);
			renderer.setKeyColor(new Color(0, 0, 128));
			predictorRenderers.add(renderer);
		}

		dataStreamCore.prepareDataStreams();

		renderers.addAll(homogeneousDataStreamBundleRenderers);
		renderers.addAll(reflexRenderers);
		renderers.addAll(actorRenderers);
		renderers.addAll(predictorRenderers);

		float screenWidth = getWidth();
		float screenHeight = getHeight();
		float edgeMargin = screenWidth / 50;
		float margin = edgeMargin / 2;
		int gridWidth = (int) Math.ceil(Math.pow(renderers.size(), 1 / 3.0d));
		int gridHeight = (int) Math.floor(Math.pow(renderers.size(), 2 / 3.0d));

		float width = ((screenWidth - edgeMargin * 2) / gridWidth) - margin;
		float height = ((screenHeight - edgeMargin * 2) / gridHeight) - margin;

		int c = 0;

		for (int gx = 0; gx < gridWidth; gx++) {
			for (int gy = 0; gy < gridHeight; gy++) {
				if (c < renderers.size()) {
					float x = edgeMargin + gx * (width + margin);
					float y = edgeMargin + gy * (height + margin);
					renderers.get(c++).setVisibleAt(x, y, width, height);
				}
			}
		}
	}

	@Override
	public void draw() {
		dataStreamCore.step();
		background(0);
		for (int i = 0; i < renderers.size(); i++) {
			renderers.get(i).draw(g);
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
		for (int i = 0; i < renderers.size(); i++)
			if (renderers.get(i).contains(mouseX, mouseY))
				renderers.get(i).mouseClicked(mouseX, mouseY);
	}

	@Override
	public void mousePressed() {
		for (int i = 0; i < renderers.size(); i++)
			if (renderers.get(i).contains(mouseX, mouseY)) {
				mouseFocusedRenderer = renderers.get(i);
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
