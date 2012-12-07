package org.opendrawer.ape.processing;

import java.awt.Color;

import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundle;

import processing.core.PGraphics;

public class StateStreamBundleRenderer extends Renderer {

	protected StateStreamBundle stateStreamBundle;

	public StateStreamBundleRenderer(StateStreamBundle stateStreamBundle) {
		super();
		this.stateStreamBundle = stateStreamBundle;
	}

	public StateStreamBundleRenderer(StateStreamBundle stateStreamBundle,
			float x, float y, float width, float height) {
		super();
		this.stateStreamBundle = stateStreamBundle;
	}

	public StateStreamBundleRenderer(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		if (stateStreamBundle != null) {
			g.noFill();
			int nc = stateStreamBundle.getStateStreams().size();
			float graphHeight = height - Renderer.lineWidth / 2.0f;
			for (int c = nc - 1; c >= 0; c--) {
				double[] states = stateStreamBundle.getStateStreams().get(c)
						.read();
				if (states != null) {
					int streamLength = states.length;
					double min = Double.NaN;
					double max = Double.NaN;
					for (int i = 0; i < streamLength; i++) {
						if (!Double.isNaN(states[i])) {
							if (Double.isNaN(min))
								min = states[i];
							else if (states[i] < min)
								min = states[i];
							if (Double.isNaN(max))
								max = states[i];
							else if (states[i] > max)
								max = states[i];
						}
					}
					if (!(Double.isNaN(min) || Double.isNaN(max) || max == min)) {
						g.stroke(Color.HSBtoRGB(c / (float) nc, 1.0f, 1.0f));
						g.strokeWeight(Renderer.lineWidth);
						boolean drawing = false;
						int vertexCount = 0;
						float x1 = 0;
						float y1 = 0;
						for (int i = 0; i < streamLength; i++) {
							double v = states[i];
							if (!Double.isNaN(v)) {
								if (!drawing) {
									g.beginShape();
									drawing = true;
									vertexCount = 0;
								}
								v = (v - min) / (max - min);
								x1 = (getStreamLeft())
										+ ((i / (float) (streamLength - 1)) * (width - (getStreamLeft() - x)));
								y1 = (float) ((y + height) - v * graphHeight);
								y1 -= c;
								g.vertex(x1, y1);
								vertexCount++;
							} else {
								if (drawing) {
									if (vertexCount++ % 2 == 1)
										g.vertex(x1, y1);
									g.endShape();
									vertexCount = 0;
									drawing = false;
								}
							}
						}
						if (drawing) {
							if (vertexCount++ % 2 == 1)
								g.vertex(x1, y1);
							g.endShape();
							drawing = false;
						}
					}
				}
			}
		}
	}

	protected float getStreamLeft() {
		return x;
	}

	public StateStreamBundle getStatesStreamBundle() {
		return stateStreamBundle;
	}

}