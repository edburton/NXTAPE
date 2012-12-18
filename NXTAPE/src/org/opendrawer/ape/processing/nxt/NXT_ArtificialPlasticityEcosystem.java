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
import org.opendrawer.ape.darwinianneurodynamics.SensorimotorBundle;
import org.opendrawer.ape.darwinianneurodynamics.StateStream;
import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundle;
import org.opendrawer.ape.darwinianneurodynamics.StatesProvider;
import org.opendrawer.ape.processing.nxt.dummy.EyeBall;
import org.opendrawer.ape.processing.nxt.dummy.Muscle;
import org.opendrawer.ape.processing.nxt.dummy.SimpleArm;
import org.opendrawer.ape.processing.nxt.dummy.WobbleReflex;
import org.opendrawer.ape.processing.renderers.EcosystemRenderer;
import org.opendrawer.ape.processing.renderers.Renderer;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class NXT_ArtificialPlasticityEcosystem extends PApplet {
	private static final boolean presentationMode = true;

	private final List<Ecosystem> ecosystems = new ArrayList<Ecosystem>();
	private final List<EcosystemRenderer> ecosytemRenderers = new ArrayList<EcosystemRenderer>();
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
		NXTInfo[] NXTs = null;
		try {
			NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
			NXTs = nxtComm.search(null);
		} catch (NXTCommException e) {
			e.printStackTrace();
		}

		if (NXTs != null && NXTs.length == 1) {
			ecosystems.add(makeNXTEcology());
		} else {
			ecosystems.add(makeEyeBallEcology());
			// ecosystems.add(makeSimpleArmEcology());
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
				new AccelHTSensor(SensorPort.S1));
		NXTTouchSensor touchLeftNXT = new NXTTouchSensor(new TouchSensor(
				SensorPort.S2));
		NXTTouchSensor touchBottomNXT = new NXTTouchSensor(new TouchSensor(
				SensorPort.S3));
		NXTTouchSensor touchRightNXT = new NXTTouchSensor(new TouchSensor(
				SensorPort.S4));

		NXTMotor armHeadMotorNXT = new NXTMotor(Motor.A, -180, 0, 0, 0.925f);
		NXTMotor armMiddleMotorNXT = new NXTMotor(Motor.B, -90, 90, 0, 0.8f);
		NXTMotor armBodyMotorNXT = new NXTMotor(Motor.C, -90, 90, 0, 0.8f);

		SensorimotorBundle accelerometer = eco.makeInput(accelerometerNXT);
		SensorimotorBundle touchLeft = eco.makeInput(touchLeftNXT);
		SensorimotorBundle touchBottom = eco.makeInput(touchBottomNXT);
		SensorimotorBundle touchRight = eco.makeInput(touchRightNXT);

		SensorimotorBundle armHeadMotor = eco.makeOutput(armHeadMotorNXT);
		SensorimotorBundle armMiddleMotor = eco.makeOutput(armMiddleMotorNXT);
		SensorimotorBundle armBodyMotor = eco.makeOutput(armBodyMotorNXT);

		eco.addReflex(new LinearReflex(accelerometer, armHeadMotor
				.getStatesProvider(), 1, 0, -1));
		eco.addReflex(new LinearReflex(touchBottom, armHeadMotor
				.getStatesProvider(), 0, 0, -1));
		eco.addReflex(new LinearReflex(touchLeft, armMiddleMotor
				.getStatesProvider(), 0, 0, -1));
		eco.addReflex(new LinearReflex(touchRight, armMiddleMotor
				.getStatesProvider(), 0, 0, 1));
		eco.addReflex(new LinearReflex(touchLeft, armBodyMotor
				.getStatesProvider(), 0, 0, -1));
		eco.addReflex(new LinearReflex(touchRight, armBodyMotor
				.getStatesProvider(), 0, 0, 1));

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

		for (int i = 0; i < 6; i++) {
			Muscle muscle = new Muscle();
			muscles.add(muscle);
			eyeBall.addMuscle(muscle);
			eco.makeOutput(muscle);
			// StateStreamBundle reflexStateStreamBundle = new
			// StateStreamBundle(
			// eco.getStatesStreamLength());
			// reflexStateStreamBundle.addStatesProviderStreams(eyeBall);
			// EyeBallTwitchReflex eyeBallTwitchReflex = new
			// EyeBallTwitchReflex(
			// reflexStateStreamBundle, muscle, 0, 0);
			// eco.addReflex(eyeBallTwitchReflex);
		}

		for (int i = 0; i < 8; i++) {
			Actor predictor;
			Actor actor;
			List<StateStreamBundle> predictorBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.INPUT | StatesProvider.OUTPUT, 2, 1,
							muscles.size() * 2);
			List<StateStreamBundle> actorInputBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.INPUT, 1, 1, muscles.size() * 2);
			List<StateStreamBundle> actorOutputBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.OUTPUT, 1, 1, muscles.size() * 2);
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
		Ecosystem eco = new Ecosystem(100);
		int joints = 3;
		SimpleArm simpleArm = new SimpleArm(joints);

		eco.makeOutput(simpleArm);

		for (int i = 0; i < 0; i++) {
			StateStreamBundle reflexStateStreamBundle = new StateStreamBundle(
					eco.getStatesStreamLength());
			WobbleReflex wobbleReflex = new WobbleReflex(
					reflexStateStreamBundle, simpleArm, 0, i);
			eco.addReflex(wobbleReflex);
		}

		for (int i = 0; i < 8; i++) {
			Actor predictor;
			Actor actor;
			List<StateStreamBundle> predictorBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.INPUT | StatesProvider.OUTPUT, 2, 1,
							joints * 2);
			List<StateStreamBundle> actorInputBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.INPUT, 1, joints, joints);
			List<StateStreamBundle> actorOutputBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.OUTPUT, 1, joints, joints);
			predictor = new Predictor(actorInputBundles.get(0),
					actorOutputBundles.get(0));
			actor = new Actor(actorInputBundles.get(0),
					actorOutputBundles.get(0));
			CuriosityLoop curiosityLoop = new CuriosityLoop(predictor, actor);
			eco.addCuriosityLoop(curiosityLoop);
		}

		return eco;
	}
}
