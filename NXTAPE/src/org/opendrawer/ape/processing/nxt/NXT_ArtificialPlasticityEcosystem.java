package org.opendrawer.ape.processing.nxt;

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
import org.opendrawer.ape.darwinianneurodynamics.CuriosityLoop;
import org.opendrawer.ape.darwinianneurodynamics.Ecosystem;
import org.opendrawer.ape.darwinianneurodynamics.Predictor;
import org.opendrawer.ape.darwinianneurodynamics.SensorimotorInput;
import org.opendrawer.ape.darwinianneurodynamics.SensorimotorOutput;
import org.opendrawer.ape.darwinianneurodynamics.StateStream;
import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;
import org.opendrawer.ape.processing.nxt.dummy.EyeBall;
import org.opendrawer.ape.processing.nxt.dummy.EyeBallTwitchReflex;
import org.opendrawer.ape.processing.nxt.dummy.Muscle;
import org.opendrawer.ape.processing.nxt.dummy.SimpleArm;
import org.opendrawer.ape.processing.nxt.dummy.WobbleReflex;
import org.opendrawer.ape.processing.renderers.EcosystemRenderer;
import org.opendrawer.ape.processing.renderers.Renderer;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class NXT_ArtificialPlasticityEcosystem extends PApplet {
	private static final boolean presentationMode = true;

	private NXTComm nxtComm;
	private NXTInfo[] NXTs;
	private final List<Ecosystem> ecosystems = new ArrayList<Ecosystem>();
	private final List<EcosystemRenderer> ecosytemRenderers = new ArrayList<EcosystemRenderer>();
	private boolean dummyMode = false;
	private static final String touch_left_name = "Touch Left";
	private static final String touch_bottom_name = "Touch Bottom";
	private static final String touch_right_name = "Touch Right";
	private static final String accelerometer_name = "Accelerometer";
	// private static final String compass_name = "Compass";
	private float zoomToModule = 1;

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
		Renderer.lineMarginWidth = getHeight() / 768.0f;
		Renderer.lineWidth = Renderer.lineMarginWidth * 1.70710678118655f;
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

		if (NXTs != null && NXTs.length == 1) {
			ecosystems.add(makeNXTEcology());
		} else {
			dummyMode = true;
			// ecosystems.add(makeEyeBallEcology());
			ecosystems.add(makeSimpleArmEcology());
		}
		float height = getHeight() / (float) ecosystems.size();
		for (int i = 0; i < ecosystems.size(); i++) {
			ecosytemRenderers.add(new EcosystemRenderer(ecosystems.get(i)));
			ecosytemRenderers.get(i).setVisibleAt(0, height * i, getWidth(),
					height);
		}
		setZoomToModule();
	}

	private void setZoomToModule() {
		zoomToModule = Float.MAX_VALUE;
		for (int i = 0; i < ecosytemRenderers.size(); i++) {
			float zoom = ecosytemRenderers.get(i).getZoomToChild();
			if (zoom < zoomToModule)
				zoomToModule = zoom;
		}
	}

	@Override
	public void draw() {
		if (mousePressed) {
			translate(getWidth() / 2, getHeight() / 2);
			scale(zoomToModule, zoomToModule);
			translate(-mouseX, -mouseY);
			if (cursorVisible) {
				noCursor();
				cursorVisible = false;
			}
		} else if (!cursorVisible) {
			cursor();
			cursorVisible = true;
		}
		background(0);
		for (int i = 0; i < ecosystems.size(); i++)
			ecosystems.get(i).step();
		for (int i = 0; i < ecosytemRenderers.size(); i++)
			ecosytemRenderers.get(i).draw(g);
		if (frameCount % 100 == 0)
			println("frameRate: " + frameRate);
	}

	private Ecosystem makeNXTEcology() {
		Ecosystem eco = new Ecosystem(20);
		NXTAccelerometer accelerometerNXT = new NXTAccelerometer(
				!dummyMode ? new AccelHTSensor(SensorPort.S1) : null,
				accelerometer_name);
		NXTTouchSensor touchLeftNXT = new NXTTouchSensor(
				!dummyMode ? new TouchSensor(SensorPort.S2) : null,
				touch_left_name);
		NXTTouchSensor touchBottomNXT = new NXTTouchSensor(
				!dummyMode ? new TouchSensor(SensorPort.S3) : null,
				touch_bottom_name);
		NXTTouchSensor touchRightNXT = new NXTTouchSensor(
				!dummyMode ? new TouchSensor(SensorPort.S4) : null,
				touch_right_name);

		NXTMotor armHeadMotorNXT = new NXTMotor(!dummyMode ? Motor.A : null,
				"arm head motor", -180, 0, 0, 0.925f);
		NXTMotor armMiddleMotorNXT = new NXTMotor(!dummyMode ? Motor.B : null,
				"arm midle motor", -90, 90, 0, 0.8f);
		NXTMotor armBodyMotorNXT = new NXTMotor(!dummyMode ? Motor.C : null,
				"arm body motor", -90, 90, 0, 0.8f);

		SensorimotorInput accelerometer = eco.makeInput(accelerometerNXT);
		SensorimotorInput touchLeft = eco.makeInput(touchLeftNXT);
		SensorimotorInput touchBottom = eco.makeInput(touchBottomNXT);
		SensorimotorInput touchRight = eco.makeInput(touchRightNXT);

		SensorimotorOutput armHeadMotor = eco.makeOutput(armHeadMotorNXT);
		SensorimotorOutput armMiddleMotor = eco.makeOutput(armMiddleMotorNXT);
		SensorimotorOutput armBodyMotor = eco.makeOutput(armBodyMotorNXT);

		eco.addReflex(new LinearReflex(accelerometer, armHeadMotor
				.getOutputStatesProvider(), 1, 0, -1));
		eco.addReflex(new LinearReflex(touchBottom, armHeadMotor
				.getOutputStatesProvider(), 0, 0, -1));
		eco.addReflex(new LinearReflex(touchLeft, armMiddleMotor
				.getOutputStatesProvider(), 0, 0, -1));
		eco.addReflex(new LinearReflex(touchRight, armMiddleMotor
				.getOutputStatesProvider(), 0, 0, 1));
		eco.addReflex(new LinearReflex(touchLeft, armBodyMotor
				.getOutputStatesProvider(), 0, 0, -1));
		eco.addReflex(new LinearReflex(touchRight, armBodyMotor
				.getOutputStatesProvider(), 0, 0, 1));

		for (int i = 0; i < 50; i++) {
			Actor actor = new Actor(null, null);
			eco.addActor(actor);
		}

		List<StateStream> allPotentialPredictorStateStreams = new ArrayList<StateStream>();
		for (int i = 0; i < eco.getInputs().size(); i++)
			allPotentialPredictorStateStreams.addAll(eco.getInputs().get(i)
					.getStateStreams());
		for (int i = 0; i < eco.getOutputs().size(); i++)
			allPotentialPredictorStateStreams.addAll(eco.getOutputs().get(i)
					.getStateStreams());

		for (int i = 0; i < 50; i++) {
			int streams = (int) Math.floor(random(2,
					allPotentialPredictorStateStreams.size()));
			int outputsIndex = (int) Math.floor(random(1, streams - 1));
			int[] streamIndexes = new int[streams];
			for (int s = 0; s < streams; s++)
				streamIndexes[s] = -1;

			for (int s = 0; s < streams; s++) {
				boolean original = false;
				int p = -1;
				while (!original) {
					original = true;
					p = (int) Math.floor(random(0,
							allPotentialPredictorStateStreams.size()));
					for (int ss = 0; ss < streams; ss++)
						if (p == streamIndexes[ss])
							original = false;
				}
				streamIndexes[s] = p;
			}

			StateStreamBundle inputStateStreamBundle = new StateStreamBundle(
					eco.getStatesStreamLength());
			StateStreamBundle oututStateStreamBundle = new StateStreamBundle(
					eco.getStatesStreamLength());

			for (int ins = 0; ins < outputsIndex; ins++)
				inputStateStreamBundle
						.addStateStream(allPotentialPredictorStateStreams
								.get(streamIndexes[ins]));
			for (int outs = outputsIndex; outs < streams; outs++)
				oututStateStreamBundle
						.addStateStream(allPotentialPredictorStateStreams
								.get(streamIndexes[outs]));

			Predictor predictor = new Predictor(inputStateStreamBundle,
					oututStateStreamBundle);
			eco.addPredictor(predictor);

		}
		return eco;
	}

	private Ecosystem makeEyeBallEcology() {
		Ecosystem eco = new Ecosystem(60);
		List<Muscle> muscles = new ArrayList<Muscle>();
		EyeBall eyeBall = new EyeBall();

		eco.makeInput(eyeBall);

		for (int i = 0; i < 4; i++) {
			Muscle muscle = new Muscle("Muscle " + i);
			muscles.add(muscle);
			eyeBall.addMuscle(muscle);
			eco.makeOutput(muscle);
			StateStreamBundle reflexStateStreamBundle = new StateStreamBundle(
					eco.getStatesStreamLength());
			reflexStateStreamBundle.addStatesProviderStreams(eyeBall);
			EyeBallTwitchReflex eyeBallTwitchReflex = new EyeBallTwitchReflex(
					reflexStateStreamBundle, muscle, 0, 0);
			eco.addReflex(eyeBallTwitchReflex);
		}

		for (int i = 0; i < 12; i++) {
			Predictor predictor;
			Actor actor;
			List<StateStreamBundle> predictorBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.INPUT | StatesProvider.OUTPUT, 2, 1,
							6);
			List<StateStreamBundle> actorInputBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.INPUT, 1, 1, 6);
			List<StateStreamBundle> actorOutputBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.INPUT, 1, 1, 6);
			predictor = new Predictor(predictorBundles.get(0),
					predictorBundles.get(1));
			actor = new Actor(actorInputBundles.get(0),
					actorOutputBundles.get(0));
			CuriosityLoop curiosityLoop = new CuriosityLoop(predictor, actor);
			eco.addCuriosityLoop(curiosityLoop);
		}
		return eco;
	}

	private Ecosystem makeSimpleArmEcology() {
		Ecosystem eco = new Ecosystem(50);
		int joints = 3;
		SimpleArm simpleArm = new SimpleArm(joints);

		eco.makeOutput(simpleArm);

		for (int i = 0; i < joints; i++) {
			StateStreamBundle reflexStateStreamBundle = new StateStreamBundle(
					eco.getStatesStreamLength());
			WobbleReflex wobbleReflex = new WobbleReflex(
					reflexStateStreamBundle, simpleArm, 0, i);
			eco.addReflex(wobbleReflex);
		}

		for (int i = 0; i < 10; i++) {
			Predictor predictor;
			Actor actor;
			List<StateStreamBundle> predictorBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.INPUT | StatesProvider.OUTPUT, 2, 1,
							joints * 2);
			List<StateStreamBundle> actorInputBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.INPUT, 1, 1, joints);
			List<StateStreamBundle> actorOutputBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.OUTPUT, 1, 1, joints);
			predictor = new Predictor(predictorBundles.get(0),
					predictorBundles.get(1));
			actor = new Actor(actorInputBundles.get(0),
					actorOutputBundles.get(0));
			CuriosityLoop curiosityLoop = new CuriosityLoop(predictor, actor);
			eco.addCuriosityLoop(curiosityLoop);
		}

		return eco;
	}
}
