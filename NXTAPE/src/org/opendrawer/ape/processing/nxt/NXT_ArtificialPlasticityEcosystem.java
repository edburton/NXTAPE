package org.opendrawer.ape.processing.nxt;

import java.util.ArrayList;
import java.util.List;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.AccelHTSensor;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTInfo;

import org.opendrawer.ape.darwinianneurodynamics.Actor;
import org.opendrawer.ape.darwinianneurodynamics.Ecosystem;
import org.opendrawer.ape.darwinianneurodynamics.Predictor;
import org.opendrawer.ape.darwinianneurodynamics.SensorimotorInput;
import org.opendrawer.ape.darwinianneurodynamics.SensorimotorOutput;
import org.opendrawer.ape.darwinianneurodynamics.StateStream;
import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundle;
import org.opendrawer.ape.processing.nxt.dummy.EyeBall;
import org.opendrawer.ape.processing.nxt.dummy.EyeBallTwitchReflex;
import org.opendrawer.ape.processing.nxt.dummy.Muscle;
import org.opendrawer.ape.processing.nxt.dummy.SimpleArm;
import org.opendrawer.ape.processing.nxt.dummy.SimpleArmTwitchReflex;
import org.opendrawer.ape.processing.renderers.EcosystemRenderer;
import org.opendrawer.ape.processing.renderers.Renderer;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class NXT_ArtificialPlasticityEcosystem extends PApplet {
	private static final boolean presentationMode = false;

	private NXTComm nxtComm;
	private NXTInfo[] NXTs;
	private Ecosystem ecosystem;
	private EcosystemRenderer ecosytemRenderer;
	private boolean dummyMode = false;
	private static final String touch_left_name = "Touch Left";
	private static final String touch_bottom_name = "Touch Bottom";
	private static final String touch_right_name = "Touch Right";
	private static final String accelerometer_name = "Accelerometer";
	// private static final String compass_name = "Compass";

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
		smooth();
		frameRate(50);
		ellipseMode(CORNERS);
		rectMode(CORNERS);
		background(0);
		frame.setBackground(new java.awt.Color(0, 0, 0));
		setupNXT();
	}

	private void setupNXT() {
		// try {
		// nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		// NXTs = nxtComm.search(null);
		// } catch (NXTCommException e) {
		// e.printStackTrace();
		// }

		if (NXTs != null && NXTs.length == 1) {
			ecosystem = makeNXTEcology();
		} else {
			dummyMode = true;
			// ecosystem = makeEyeBallEcology();
			ecosystem = makeSimpleArmEcology();
		}
		ecosytemRenderer = new EcosystemRenderer(ecosystem);
		ecosytemRenderer.setVisibleAt(0, 0, getWidth(), getHeight());
	}

	@Override
	public void draw() {
		if (mousePressed) {
			translate(getWidth() / 2, getHeight() / 2);
			scale(ecosytemRenderer.getZoomToChild(),
					ecosytemRenderer.getZoomToChild());
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
		ecosystem.step();
		ecosytemRenderer.draw(g);
		// if (frameCount % 100 == 0)
		// println("frameRate: " + frameRate);
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
			Actor actor = new Actor();
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
		Ecosystem eco = new Ecosystem(120);
		List<Muscle> muscles = new ArrayList<Muscle>();
		EyeBall eyeBall = new EyeBall();

		eco.makeInput(eyeBall);

		for (int i = 0; i < 8; i++) {
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

		for (int i = 0; i < 0; i++) {
			Actor actor = new Actor();
			eco.addActor(actor);
		}

		List<StateStream> allPotentialPredictorStateStreams = new ArrayList<StateStream>();
		for (int i = 0; i < eco.getInputs().size(); i++)
			allPotentialPredictorStateStreams.addAll(eco.getInputs().get(i)
					.getStateStreams());
		for (int i = 0; i < eco.getOutputs().size(); i++)
			allPotentialPredictorStateStreams.addAll(eco.getOutputs().get(i)
					.getStateStreams());

		for (int i = 0; i < 16; i++) {
			int streams = 6;// (int) Math.floor(random(2,
			// allPotentialPredictorStateStreams.size()));
			int outputsIndex = 4;// (int) Math.floor(random(1, streams -
									// 1));
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

			for (int ins = 0; ins < outputsIndex; ins++) {
				StateStream stream = allPotentialPredictorStateStreams
						.get(streamIndexes[ins]);
				StateStream streamCopy = new StateStream(
						stream.getStatesProvider(),
						stream.getStatesProviderChannel(),
						stream.getStreamLength());
				inputStateStreamBundle.addStateStream(streamCopy);
			}

			for (int outs = outputsIndex; outs < streams; outs++) {
				StateStream stream = allPotentialPredictorStateStreams
						.get(streamIndexes[outs]);
				StateStream streamCopy = new StateStream(
						stream.getStatesProvider(),
						stream.getStatesProviderChannel(),
						stream.getStreamLength());
				oututStateStreamBundle.addStateStream(streamCopy);
			}

			Predictor predictor = new Predictor(inputStateStreamBundle,
					oututStateStreamBundle);
			eco.addPredictor(predictor);
		}
		return eco;
	}

	private Ecosystem makeSimpleArmEcology() {
		Ecosystem eco = new Ecosystem(120);
		int joints = 2;
		SimpleArm simpleArm = new SimpleArm(joints);

		eco.makeOutput(simpleArm);

		for (int i = 0; i < joints; i++) {
			StateStreamBundle reflexStateStreamBundle = new StateStreamBundle(
					eco.getStatesStreamLength());
			SimpleArmTwitchReflex simpleArmTwitchReflex = new SimpleArmTwitchReflex(
					reflexStateStreamBundle, simpleArm, 0, i);
			eco.addReflex(simpleArmTwitchReflex);
		}

		for (int i = 0; i < 4; i++) {
			Actor actor = new Actor();
			eco.addActor(actor);
		}

		for (int i = 0; i < 16; i++) {

			StateStreamBundle inputStateStreamBundle = new StateStreamBundle(
					eco.getStatesStreamLength());
			StateStreamBundle oututStateStreamBundle = new StateStreamBundle(
					eco.getStatesStreamLength());

			for (int j = 0; j < joints; j++) {
				inputStateStreamBundle.addStateStream(new StateStream(
						simpleArm, joints + j, eco.getStatesStreamLength()));
			}

			oututStateStreamBundle.addStateStream(new StateStream(simpleArm,
					joints * 2, eco.getStatesStreamLength()));
			oututStateStreamBundle.addStateStream(new StateStream(simpleArm,
					joints * 2 + 1, eco.getStatesStreamLength()));

			Predictor predictor = new Predictor(inputStateStreamBundle,
					oututStateStreamBundle);
			eco.addPredictor(predictor);
		}
		return eco;
	}
}
