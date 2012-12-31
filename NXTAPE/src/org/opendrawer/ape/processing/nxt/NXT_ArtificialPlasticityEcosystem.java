package org.opendrawer.ape.processing.nxt;

import java.util.ArrayList;
import java.util.List;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.AccelHTSensor;
import lejos.pc.comm.NXTInfo;

import org.opendrawer.ape.darwinianneurodynamics.Actor;
import org.opendrawer.ape.darwinianneurodynamics.CuriosityLoop;
import org.opendrawer.ape.darwinianneurodynamics.Ecosystem;
import org.opendrawer.ape.darwinianneurodynamics.Predictor;
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
		frameRate(200);
		ellipseMode(CORNERS);
		rectMode(CORNERS);
		background(0);
		frame.setBackground(new java.awt.Color(0, 0, 0));
		setupNXT();
	}

	private void setupNXT() {
		NXTInfo[] NXTs = null;
		// try {
		// NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		// NXTs = nxtComm.search(null);
		// } catch (NXTCommException e) {
		// e.printStackTrace();
		// }

		if (NXTs != null && NXTs.length == 1) {
			ecosystems.add(makeNXTEcology());
		} else {
			ecosystems.add(makeEyeBallEcology());
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
		eco.makeInput(new NXTAccelerometer(new AccelHTSensor(SensorPort.S1)));
		eco.makeInput(new NXTTouchSensor(new TouchSensor(SensorPort.S2)));
		eco.makeInput(new NXTTouchSensor(new TouchSensor(SensorPort.S3)));
		eco.makeInput(new NXTTouchSensor(new TouchSensor(SensorPort.S4)));

		eco.makeOutput(new NXTMotor(Motor.A, -180, 0, 0, 0.925f));
		eco.makeOutput(new NXTMotor(Motor.B, -90, 90, 0, 0.8f));
		eco.makeOutput(new NXTMotor(Motor.C, -90, 90, 0, 0.8f));

		for (int i = 0; i < 8; i++) {
			Actor predictor;
			Actor actor;
			List<StateStreamBundle> predictorBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.INPUT | StatesProvider.OUTPUT, 2, 1,
							8);
			List<StateStreamBundle> actorInputBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.INPUT, 1, 1, 8);
			List<StateStreamBundle> actorOutputBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.OUTPUT, 1, 1, 8);
			predictor = new Predictor(predictorBundles.get(0),
					predictorBundles.get(1));
			actor = new Actor(actorInputBundles.get(0),
					actorOutputBundles.get(0));
			CuriosityLoop curiosityLoop = new CuriosityLoop(predictor, actor);
			eco.addCuriosityLoop(curiosityLoop);
		}

		return eco;
	}

	private Ecosystem makeEyeBallEcology() {
		Ecosystem eco = new Ecosystem(20);
		List<Muscle> muscles = new ArrayList<Muscle>();
		EyeBall eyeBall = new EyeBall();

		eco.makeInput(eyeBall);

		for (int i = 0; i < 8; i++) {
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

		for (int i = 0; i < 12; i++) {
			Actor predictor;
			Actor actor;
			List<StateStreamBundle> predictorBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.INPUT, 2, 1, muscles.size() * 2);
			List<StateStreamBundle> actorInputBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.INPUT, 1, 1, muscles.size() * 2);
			List<StateStreamBundle> actorOutputBundles = eco
					.getRandomUniqueSensorimotorStateStreamBundles(
							StatesProvider.OUTPUT, 1, 2, muscles.size() * 2);
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
		Ecosystem eco = new Ecosystem(20);
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
