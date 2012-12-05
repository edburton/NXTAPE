package org.opendrawer.ape.processing.nxt;

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

import org.opendrawer.ape.darwinianneurodynamics.Actor;
import org.opendrawer.ape.darwinianneurodynamics.HomogeneousDataStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.NeurodynamicStreamCore;
import org.opendrawer.ape.darwinianneurodynamics.Predictor;
import org.opendrawer.ape.darwinianneurodynamics.Reflex;
import org.opendrawer.ape.processing.DataStreamBundleListRenderer;
import org.opendrawer.ape.processing.HomogeneousDataStreamBundleRenderer;
import org.opendrawer.ape.processing.Renderer;
import org.opendrawer.ape.processing.nxt.dummy.EyeBall;
import org.opendrawer.ape.processing.nxt.dummy.EyeBallRenderer;
import org.opendrawer.ape.processing.nxt.dummy.Muscle;
import org.opendrawer.ape.processing.nxt.dummy.MuscleRenderer;
import org.opendrawer.ape.processing.nxt.dummy.TwitchReflex;

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
	private final int dataStreamWidth = 100;
	private final NeurodynamicStreamCore neurodynamicStreamCore = new NeurodynamicStreamCore();
	private boolean dummyMode = false;
	private static final String touch_left_name = "Touch Left";
	private static final String touch_bottom_name = "Touch Bottom";
	private static final String touch_right_name = "Touch Right";
	private static final String accelerometer_name = "Accelerometer";
	// private static final String compass_name = "Compass";
	private final List<Renderer> renderers = new ArrayList<Renderer>();
	private float edgeMargin;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (presentationMode)
			PApplet.main(new String[] { "--present",
					"org.opendrawer.ape.processing.nxt.NXT_ArtificialPlasticityEcology" });
		else
			PApplet.main("org.opendrawer.ape.processing.nxt.NXT_ArtificialPlasticityEcology");
	}

	@Override
	public void setup() {
		if (presentationMode)
			size(displayWidth, displayHeight, OPENGL);
		else
			size(1024, 768, OPENGL);
		frameRate(200);
		Renderer.lineMarginWidth = getHeight() / 384.0f;
		Renderer.lineWidth = Renderer.lineMarginWidth * 1.70710678118655f;
		edgeMargin = Renderer.lineMarginWidth * 4;
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

		if (NXTs.length != 1) {
			setupDummyMode();
			return;
		}

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
				"arm head motor", -180, 0, 0, 0.925f);
		armMiddleMotor = new NXTMotor(!dummyMode ? Motor.B : null,
				"arm midle motor", -60, 60, 0, 0.8f);
		armBodyMotor = new NXTMotor(!dummyMode ? Motor.C : null,
				"arm body motor", -60, 60, 0, 0.8f);

		List<HomogeneousDataStreamBundleRenderer> homogeneousDataStreamBundleRenderers = new ArrayList<HomogeneousDataStreamBundleRenderer>();
		List<DataStreamBundleListRenderer> reflexRenderers = new ArrayList<DataStreamBundleListRenderer>();
		List<DataStreamBundleListRenderer> predictorRenderers = new ArrayList<DataStreamBundleListRenderer>();
		List<DataStreamBundleListRenderer> actorRenderers = new ArrayList<DataStreamBundleListRenderer>();

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
			neurodynamicStreamCore
					.addDataStreamBundle(homogeneousDataStreamBundleRenderers
							.get(i).getDataStreamBundle());

		Reflex accelerometerReflex;
		Reflex bottomReflexM;
		Reflex leftReflexM;
		Reflex rightReflexM;
		Reflex leftReflexB;
		Reflex rightReflexB;

		neurodynamicStreamCore
				.addReflex(accelerometerReflex = new LinearReflex(
						accelerometerDataStreamBundle,
						armHeadMotorDataStreamBundle, 1, 0, -1));
		neurodynamicStreamCore
				.addReflex(bottomReflexM = new LinearReflex(
						bottomDataStreamBundle, armHeadMotorDataStreamBundle,
						0, 0, -1));
		neurodynamicStreamCore
				.addReflex(leftReflexM = new LinearReflex(leftDataStreamBundle,
						armMiddleMotorDataStreamBundle, 0, 0, -1));
		neurodynamicStreamCore
				.addReflex(rightReflexM = new LinearReflex(
						rightDataStreamBundle, armMiddleMotorDataStreamBundle,
						0, 0, 1));
		neurodynamicStreamCore.addReflex(leftReflexB = new LinearReflex(
				leftDataStreamBundle, armBodyMotorDataStreamBundle, 0, 0, -1));
		neurodynamicStreamCore.addReflex(rightReflexB = new LinearReflex(
				rightDataStreamBundle, armBodyMotorDataStreamBundle, 0, 0, 1));

		reflexRenderers.add(new DataStreamBundleListRenderer(
				accelerometerReflex));
		reflexRenderers.add(new DataStreamBundleListRenderer(bottomReflexM));
		reflexRenderers.add(new DataStreamBundleListRenderer(leftReflexM));
		reflexRenderers.add(new DataStreamBundleListRenderer(rightReflexM));
		reflexRenderers.add(new DataStreamBundleListRenderer(leftReflexB));
		reflexRenderers.add(new DataStreamBundleListRenderer(rightReflexB));

		int nTypes = 3;
		int nType = 0;

		for (int i = 0; i < reflexRenderers.size(); i++)
			reflexRenderers.get(i).setKeyColor(
					new Color(Color
							.HSBtoRGB(nType / (float) nTypes, 1.0f, 0.5f)));
		nType++;

		for (int i = 0; i < 50; i++) {
			Actor actor = new Actor(null, null);
			neurodynamicStreamCore.addActor(actor);
			DataStreamBundleListRenderer renderer = new DataStreamBundleListRenderer(
					null);
			renderer.setKeyColor(new Color(Color.HSBtoRGB(nType
					/ (float) nTypes, 1.0f, 0.5f)));
			actorRenderers.add(renderer);
		}
		nType++;

		int r = 0;
		for (int i = 0; i < 50; i++) {
			r = (int) Math.floor(random(0, reflexRenderers.size()));
			DataStreamBundleListRenderer reflex = reflexRenderers.get(r);
			Predictor predictor = new Predictor(reflex
					.getDataStreamBundleRenderers().get(0)
					.getDataStreamBundle(), reflex
					.getDataStreamBundleRenderers().get(1)
					.getDataStreamBundle());
			neurodynamicStreamCore.addPredictor(predictor);
			DataStreamBundleListRenderer renderer = new DataStreamBundleListRenderer(
					predictor);
			renderer.setKeyColor(new Color(Color.HSBtoRGB(nType
					/ (float) nTypes, 1.0f, 0.5f)));
			predictorRenderers.add(renderer);
		}

		neurodynamicStreamCore.prepareDataStreams();

		renderers.addAll(homogeneousDataStreamBundleRenderers);
		renderers.addAll(reflexRenderers);
		renderers.addAll(actorRenderers);
		renderers.addAll(predictorRenderers);

		int sensorimotors = homogeneousDataStreamBundleRenderers.size();
		int reflexes = reflexRenderers.size();
		int actors = actorRenderers.size();
		int predictors = predictorRenderers.size();

		int zones = 1 + sensorimotors + 1 + reflexes + 1 + actors + 1
				+ predictors + 1;

		float screenWidth = getWidth();
		float screenHeight = getHeight();
		float margin = edgeMargin / 2;
		int gridWidth = (int) Math.ceil(Math.pow(zones, 1 / 3.0d));
		int gridHeight = (int) Math.floor(Math.pow(zones, 2 / 3.0d));

		float width = ((screenWidth + margin - edgeMargin * 2) / gridWidth)
				- margin;
		float height = ((screenHeight + margin - edgeMargin * 2) / gridHeight)
				- margin;

		int c = 0;
		int divider = 0;
		for (int gx = 0; gx < gridWidth; gx++) {
			for (int gy = 0; gy < gridHeight; gy++) {
				float x = edgeMargin + gx * (width + margin);
				float y = edgeMargin + gy * (height + margin);
				if (divider > 0) {
					if (c < renderers.size()) {
						renderers.get(c).setVisibleAt(x, y, width, height);
					}
					c++;
				}
				divider++;

				if (divider > 1
						&& ((c == 0) || (c == sensorimotors)
								|| (c == (sensorimotors + reflexes)) || (c == (sensorimotors
								+ reflexes + actors))))
					divider = 0;
			}
		}
	}

	private void setupDummyMode() {
		dummyMode = true;

		List<Muscle> muscles = new ArrayList<Muscle>();
		List<HomogeneousDataStreamBundleRenderer> homogeneousDataStreamBundleRenderers = new ArrayList<HomogeneousDataStreamBundleRenderer>();
		List<DataStreamBundleListRenderer> reflexRenderers = new ArrayList<DataStreamBundleListRenderer>();
		List<DataStreamBundleListRenderer> predictorRenderers = new ArrayList<DataStreamBundleListRenderer>();
		List<DataStreamBundleListRenderer> actorRenderers = new ArrayList<DataStreamBundleListRenderer>();

		EyeBall eyeBall = new EyeBall();
		HomogeneousDataStreamBundle eyeBallDataStreamBundle;
		homogeneousDataStreamBundleRenderers
				.add(new HomogeneousDataStreamBundleRenderer(
						eyeBallDataStreamBundle = new HomogeneousDataStreamBundle(
								eyeBall, dataStreamWidth), new EyeBallRenderer(
								eyeBall)));

		for (int i = 0; i < 4; i++) {
			Muscle muscle = new Muscle("Muscle " + i);
			muscles.add(muscle);
			HomogeneousDataStreamBundle muscleDataStreamBundle = new HomogeneousDataStreamBundle(
					muscle, dataStreamWidth);
			homogeneousDataStreamBundleRenderers
					.add(new HomogeneousDataStreamBundleRenderer(
							muscleDataStreamBundle, new MuscleRenderer(muscle)));
			neurodynamicStreamCore.addDataStreamBundle(muscleDataStreamBundle);
			TwitchReflex twitchReflex = new TwitchReflex(
					muscleDataStreamBundle, muscleDataStreamBundle, 0, 1);
			neurodynamicStreamCore.addReflex(twitchReflex);
			reflexRenderers.add(new DataStreamBundleListRenderer(twitchReflex));
		}

		int nTypes = 3;
		int nType = 0;

		for (int i = 0; i < reflexRenderers.size(); i++)
			reflexRenderers.get(i).setKeyColor(
					new Color(Color
							.HSBtoRGB(nType / (float) nTypes, 1.0f, 0.5f)));
		nType++;

		for (int i = 0; i < 0; i++) {
			Actor actor = new Actor(null, null);
			neurodynamicStreamCore.addActor(actor);
			DataStreamBundleListRenderer renderer = new DataStreamBundleListRenderer(
					null);
			renderer.setKeyColor(new Color(Color.HSBtoRGB(nType
					/ (float) nTypes, 1.0f, 0.5f)));
			actorRenderers.add(renderer);
		}
		nType++;

		int r = 0;
		for (int i = 0; i < 26; i++) {
			r = (int) Math.floor(random(0, reflexRenderers.size()));
			DataStreamBundleListRenderer reflex = reflexRenderers.get(r);
			Predictor predictor = new Predictor(reflex
					.getDataStreamBundleRenderers().get(0)
					.getDataStreamBundle(), reflex
					.getDataStreamBundleRenderers().get(1)
					.getDataStreamBundle());
			neurodynamicStreamCore.addPredictor(predictor);
			DataStreamBundleListRenderer renderer = new DataStreamBundleListRenderer(
					predictor);
			renderer.setKeyColor(new Color(Color.HSBtoRGB(nType
					/ (float) nTypes, 1.0f, 0.5f)));
			predictorRenderers.add(renderer);
		}

		neurodynamicStreamCore.prepareDataStreams();

		renderers.addAll(homogeneousDataStreamBundleRenderers);
		renderers.addAll(reflexRenderers);
		renderers.addAll(actorRenderers);
		renderers.addAll(predictorRenderers);

		int sensorimotors = homogeneousDataStreamBundleRenderers.size();
		int reflexes = reflexRenderers.size();
		int actors = actorRenderers.size();
		int predictors = predictorRenderers.size();

		int zones = 1 + sensorimotors + 1 + reflexes + 1 + actors + 1
				+ predictors + 1;

		float screenWidth = getWidth();
		float screenHeight = getHeight();
		float margin = edgeMargin / 2;
		int gridWidth = (int) Math.ceil(Math.pow(zones, 1 / 3.0d));
		int gridHeight = (int) Math.floor(Math.pow(zones, 2 / 3.0d));

		float width = ((screenWidth + margin - edgeMargin * 2) / gridWidth)
				- margin;
		float height = ((screenHeight + margin - edgeMargin * 2) / gridHeight)
				- margin;

		int c = 0;
		int divider = 0;
		for (int gx = 0; gx < gridWidth; gx++) {
			for (int gy = 0; gy < gridHeight; gy++) {
				float x = edgeMargin + gx * (width + margin);
				float y = edgeMargin + gy * (height + margin);
				if (divider > 0) {
					if (c < renderers.size()) {
						renderers.get(c).setVisibleAt(x, y, width, height);
					}
					c++;
				}
				divider++;

				if (divider > 1
						&& ((c == 0) || (c == sensorimotors)
								|| (c == (sensorimotors + reflexes)) || (c == (sensorimotors
								+ reflexes + actors))))
					divider = 0;
			}
		}
	}

	@Override
	public void draw() {
		neurodynamicStreamCore.step();
		background(0);
		if (mousePressed) {
			translate(getWidth() / 2, getHeight() / 2);
			scale(4, 4);
			translate(-mouseX, -mouseY);
		}
		for (int i = 0; i < renderers.size(); i++) {
			renderers.get(i).draw(g);
		}
		if (++debugCounter % 100 == 0)
			println("frameRate: " + frameRate);
		if (dummyMode) {
			fill(255, 0, 0, (cos(debugCounter / 10.0f) + 1.0f) * 128);
			text("NXT NOT FOUND", edgeMargin, edgeMargin + 10);
		}
	}
}
