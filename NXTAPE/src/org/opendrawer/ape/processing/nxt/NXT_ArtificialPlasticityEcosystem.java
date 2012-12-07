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
import org.opendrawer.ape.darwinianneurodynamics.HomogeneousStateStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.Ecosytem;
import org.opendrawer.ape.darwinianneurodynamics.Predictor;
import org.opendrawer.ape.darwinianneurodynamics.Reflex;
import org.opendrawer.ape.processing.StateStreamBundleListRenderer;
import org.opendrawer.ape.processing.HomogeneousStateStreamBundleRenderer;
import org.opendrawer.ape.processing.Renderer;
import org.opendrawer.ape.processing.nxt.dummy.EyeBall;
import org.opendrawer.ape.processing.nxt.dummy.EyeBallRenderer;
import org.opendrawer.ape.processing.nxt.dummy.Muscle;
import org.opendrawer.ape.processing.nxt.dummy.MuscleRenderer;
import org.opendrawer.ape.processing.nxt.dummy.TwitchReflex;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class NXT_ArtificialPlasticityEcosystem extends PApplet {
	private static final boolean presentationMode = true;

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
	private final int statesStreamLength = 100;
	private final Ecosytem ecosytem = new Ecosytem();
	private boolean dummyMode = false;
	private static final String touch_left_name = "Touch Left";
	private static final String touch_bottom_name = "Touch Bottom";
	private static final String touch_right_name = "Touch Right";
	private static final String accelerometer_name = "Accelerometer";
	// private static final String compass_name = "Compass";
	private final List<Renderer> renderers = new ArrayList<Renderer>();
	private float edgeMargin;
	private boolean cursorVisible = true;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (presentationMode)
			PApplet.main(new String[] { "--present",
					"org.opendrawer.ape.processing.nxt.NXT_ArtificialPlasticityEcosystem" });
		else
			PApplet.main("org.opendrawer.ape.processing.nxt.NXT_ArtificialPlasticityEcosystem");
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

		List<HomogeneousStateStreamBundleRenderer> homogeneousStateStreamBundleRenderers = new ArrayList<HomogeneousStateStreamBundleRenderer>();
		List<StateStreamBundleListRenderer> reflexRenderers = new ArrayList<StateStreamBundleListRenderer>();
		List<StateStreamBundleListRenderer> predictorRenderers = new ArrayList<StateStreamBundleListRenderer>();
		List<StateStreamBundleListRenderer> actorRenderers = new ArrayList<StateStreamBundleListRenderer>();

		HomogeneousStateStreamBundle bottomStatesStreamBundle;
		HomogeneousStateStreamBundle leftStatesStreamBundle;
		HomogeneousStateStreamBundle rightStatesStreamBundle;
		HomogeneousStateStreamBundle accelerometerStatesStreamBundle;
		HomogeneousStateStreamBundle armHeadMotorStatesStreamBundle;
		HomogeneousStateStreamBundle armMiddleMotorStatesStreamBundle;
		HomogeneousStateStreamBundle armBodyMotorStatesStreamBundle;

		homogeneousStateStreamBundleRenderers
				.add(new HomogeneousStateStreamBundleRenderer(
						accelerometerStatesStreamBundle = new HomogeneousStateStreamBundle(
								accelerometer, statesStreamLength),
						new NXTAccelerometerRenderer(accelerometer)));
		// homogeneousDataStreamBundleRenderers.add(new
		// HomogeneousStateStreamBundleRenderer(
		// new DataStreamBundle(compass, statesStreamLength),
		// new NXTCompassRenderer(compass), edgeMargin, y,
		// width, height));
		// y += height + margin;
		// homogeneousDataStreamBundleRenderers.add(new
		// HomogeneousStateStreamBundleRenderer(
		// new DataStreamBundle(touchTop, statesStreamLength),
		// new NXTTouchSensorRenderer(touchTop), edgeMargin, y, width,
		// height));
		// y += height + margin;

		homogeneousStateStreamBundleRenderers
				.add(new HomogeneousStateStreamBundleRenderer(
						bottomStatesStreamBundle = new HomogeneousStateStreamBundle(
								touchBottom, statesStreamLength),
						new NXTTouchSensorRenderer(touchBottom)));
		homogeneousStateStreamBundleRenderers
				.add(new HomogeneousStateStreamBundleRenderer(
						leftStatesStreamBundle = new HomogeneousStateStreamBundle(
								touchLeft, statesStreamLength),
						new NXTTouchSensorRenderer(touchLeft)));
		homogeneousStateStreamBundleRenderers
				.add(new HomogeneousStateStreamBundleRenderer(
						rightStatesStreamBundle = new HomogeneousStateStreamBundle(
								touchRight, statesStreamLength),
						new NXTTouchSensorRenderer(touchRight)));
		homogeneousStateStreamBundleRenderers
				.add(new HomogeneousStateStreamBundleRenderer(
						armHeadMotorStatesStreamBundle = new HomogeneousStateStreamBundle(
								armHeadMotor, statesStreamLength),
						new NXTMotorRenderer(armHeadMotor)));
		homogeneousStateStreamBundleRenderers
				.add(new HomogeneousStateStreamBundleRenderer(
						armMiddleMotorStatesStreamBundle = new HomogeneousStateStreamBundle(
								armMiddleMotor, statesStreamLength),
						new NXTMotorRenderer(armMiddleMotor)));
		homogeneousStateStreamBundleRenderers
				.add(new HomogeneousStateStreamBundleRenderer(
						armBodyMotorStatesStreamBundle = new HomogeneousStateStreamBundle(
								armBodyMotor, statesStreamLength),
						new NXTMotorRenderer(armBodyMotor)));

		for (int i = 0; i < homogeneousStateStreamBundleRenderers.size(); i++)
			ecosytem
					.addStatesStreamBundle(homogeneousStateStreamBundleRenderers
							.get(i).getStatesStreamBundle());

		Reflex accelerometerReflex;
		Reflex bottomReflexM;
		Reflex leftReflexM;
		Reflex rightReflexM;
		Reflex leftReflexB;
		Reflex rightReflexB;

		ecosytem
				.addReflex(accelerometerReflex = new LinearReflex(
						accelerometerStatesStreamBundle,
						armHeadMotorStatesStreamBundle, 1, 0, -1));
		ecosytem
				.addReflex(bottomReflexM = new LinearReflex(
						bottomStatesStreamBundle, armHeadMotorStatesStreamBundle,
						0, 0, -1));
		ecosytem
				.addReflex(leftReflexM = new LinearReflex(leftStatesStreamBundle,
						armMiddleMotorStatesStreamBundle, 0, 0, -1));
		ecosytem
				.addReflex(rightReflexM = new LinearReflex(
						rightStatesStreamBundle, armMiddleMotorStatesStreamBundle,
						0, 0, 1));
		ecosytem.addReflex(leftReflexB = new LinearReflex(
				leftStatesStreamBundle, armBodyMotorStatesStreamBundle, 0, 0, -1));
		ecosytem.addReflex(rightReflexB = new LinearReflex(
				rightStatesStreamBundle, armBodyMotorStatesStreamBundle, 0, 0, 1));

		reflexRenderers.add(new StateStreamBundleListRenderer(
				accelerometerReflex));
		reflexRenderers.add(new StateStreamBundleListRenderer(bottomReflexM));
		reflexRenderers.add(new StateStreamBundleListRenderer(leftReflexM));
		reflexRenderers.add(new StateStreamBundleListRenderer(rightReflexM));
		reflexRenderers.add(new StateStreamBundleListRenderer(leftReflexB));
		reflexRenderers.add(new StateStreamBundleListRenderer(rightReflexB));

		int nTypes = 3;
		int nType = 0;

		for (int i = 0; i < reflexRenderers.size(); i++)
			reflexRenderers.get(i).setKeyColor(
					new Color(Color
							.HSBtoRGB(nType / (float) nTypes, 1.0f, 0.5f)));
		nType++;

		for (int i = 0; i < 50; i++) {
			Actor actor = new Actor(null, null);
			ecosytem.addActor(actor);
			StateStreamBundleListRenderer renderer = new StateStreamBundleListRenderer(
					null);
			renderer.setKeyColor(new Color(Color.HSBtoRGB(nType
					/ (float) nTypes, 1.0f, 0.5f)));
			actorRenderers.add(renderer);
		}
		nType++;

		int r = 0;
		for (int i = 0; i < 50; i++) {
			r = (int) Math.floor(random(0, reflexRenderers.size()));
			StateStreamBundleListRenderer reflexRenderer = reflexRenderers.get(r);
			Predictor predictor = new Predictor(reflexRenderer
					.getStatesStreamBundleRenderers().get(0)
					.getStatesStreamBundle(), reflexRenderer
					.getStatesStreamBundleRenderers().get(1)
					.getStatesStreamBundle());
			ecosytem.addPredictor(predictor);
			StateStreamBundleListRenderer renderer = new StateStreamBundleListRenderer(
					predictor);
			renderer.setKeyColor(new Color(Color.HSBtoRGB(nType
					/ (float) nTypes, 1.0f, 0.5f)));
			predictorRenderers.add(renderer);
		}

		ecosytem.prepareStatesStreams();

		renderers.addAll(homogeneousStateStreamBundleRenderers);
		renderers.addAll(reflexRenderers);
		renderers.addAll(actorRenderers);
		renderers.addAll(predictorRenderers);

		int sensorimotors = homogeneousStateStreamBundleRenderers.size();
		int reflexes = reflexRenderers.size();
		int actors = actorRenderers.size();
		int predictors = predictorRenderers.size();

		int zones = 1 + sensorimotors + 1 + reflexes + 1 + actors + 1
				+ predictors + 1;

		float screenWidth = getWidth();
		float screenHeight = getHeight();
		float margin = edgeMargin;
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
		List<HomogeneousStateStreamBundleRenderer> homogeneousStateStreamBundleRenderers = new ArrayList<HomogeneousStateStreamBundleRenderer>();
		List<StateStreamBundleListRenderer> reflexRenderers = new ArrayList<StateStreamBundleListRenderer>();
		List<StateStreamBundleListRenderer> predictorRenderers = new ArrayList<StateStreamBundleListRenderer>();
		List<StateStreamBundleListRenderer> actorRenderers = new ArrayList<StateStreamBundleListRenderer>();

		EyeBall eyeBall = new EyeBall();
		HomogeneousStateStreamBundle eyeBallStatesStreamBundle;
		homogeneousStateStreamBundleRenderers
				.add(new HomogeneousStateStreamBundleRenderer(
						eyeBallStatesStreamBundle = new HomogeneousStateStreamBundle(
								eyeBall, statesStreamLength), new EyeBallRenderer(
								eyeBall)));

		ecosytem.addStatesStreamBundle(eyeBallStatesStreamBundle);

		for (int i = 0; i < 8; i++) {
			Muscle muscle = new Muscle("Muscle " + i);
			muscles.add(muscle);
			eyeBall.addMuscle(muscle);
			HomogeneousStateStreamBundle muscleStatesStreamBundle = new HomogeneousStateStreamBundle(
					muscle, statesStreamLength);
			homogeneousStateStreamBundleRenderers
					.add(new HomogeneousStateStreamBundleRenderer(
							muscleStatesStreamBundle, new MuscleRenderer(muscle)));
			ecosytem.addStatesStreamBundle(muscleStatesStreamBundle);
			TwitchReflex twitchReflex = new TwitchReflex(
					eyeBallStatesStreamBundle, muscleStatesStreamBundle, 0, 1);
			ecosytem.addReflex(twitchReflex);
			reflexRenderers.add(new StateStreamBundleListRenderer(twitchReflex));
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
			ecosytem.addActor(actor);
			StateStreamBundleListRenderer renderer = new StateStreamBundleListRenderer(
					null);
			renderer.setKeyColor(new Color(Color.HSBtoRGB(nType
					/ (float) nTypes, 1.0f, 0.5f)));
			actorRenderers.add(renderer);
		}
		nType++;

		int r = 0;
		for (int i = 0; i < 12; i++) {
			r = (int) Math.floor(random(0, reflexRenderers.size()));
			StateStreamBundleListRenderer reflex = reflexRenderers.get(r);
			Predictor predictor = new Predictor(reflex
					.getStatesStreamBundleRenderers().get(0)
					.getStatesStreamBundle(), reflex
					.getStatesStreamBundleRenderers().get(1)
					.getStatesStreamBundle());
			ecosytem.addPredictor(predictor);
			StateStreamBundleListRenderer renderer = new StateStreamBundleListRenderer(
					predictor);
			renderer.setKeyColor(new Color(Color.HSBtoRGB(nType
					/ (float) nTypes, 1.0f, 0.5f)));
			predictorRenderers.add(renderer);
		}

		ecosytem.prepareStatesStreams();

		renderers.addAll(homogeneousStateStreamBundleRenderers);
		renderers.addAll(reflexRenderers);
		renderers.addAll(actorRenderers);
		renderers.addAll(predictorRenderers);

		int sensorimotors = homogeneousStateStreamBundleRenderers.size();
		int reflexes = reflexRenderers.size();
		int actors = actorRenderers.size();
		int predictors = predictorRenderers.size();

		int zones = 1 + sensorimotors + 1 + reflexes + 1 + actors + 1
				+ predictors + 1;

		float screenWidth = getWidth();
		float screenHeight = getHeight();
		float margin = edgeMargin;
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
		ecosytem.step();
		background(0);
		if (mousePressed) {
			translate(getWidth() / 2, getHeight() / 2);
			scale(4, 4);
			translate(-mouseX, -mouseY);
			if (cursorVisible) {
				noCursor();
				cursorVisible = false;
			}
		} else if (!cursorVisible) {
			cursor();
			cursorVisible = true;
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
