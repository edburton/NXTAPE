package org.opendrawer.ape.processing.renderers;

import java.awt.Color;

import org.opendrawer.ape.darwinianneurodynamics.StateStreamBundle;

import processing.core.PConstants;
import processing.core.PGraphics;

public class StateStreamBundleRenderer extends Renderer {

	protected StateStreamBundle stateStreamBundle;

	public StateStreamBundleRenderer(StateStreamBundle stateStreamBundle) {
		this.stateStreamBundle = stateStreamBundle;
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		g.noStroke();
		if (stateStreamBundle != null) {
			g.noFill();
			int nc = stateStreamBundle.getStateStreams().size();
			float lineWidth = (Renderer.lineWidth * 2) / nc;
			for (int c = 0; c < nc; c++) {
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
					min = -1;
					max = 1;
					if (!(Double.isNaN(min) || Double.isNaN(max) || max == min)) {
						boolean drawing = false;
						int vertexCount = 0;
						float x1 = 0;
						float y1 = 0;
						float xs[] = new float[streamLength];
						float ys[] = new float[streamLength];
						for (int i = 0; i < streamLength; i++) {
							double v = states[i];
							if (!Double.isNaN(v)) {
								if (!drawing) {
									drawing = true;
									vertexCount = 0;
								}
								v = (v - min) / (max - min);
								x1 = (getStreamLeft() + Renderer.lineWidth)
										+ ((i / (float) (streamLength - 1)) * ((width - Renderer.lineWidth * 2) - (getStreamLeft() - x)));
								y1 = (float) ((y + height - Renderer.lineWidth) - v
										* (height - Renderer.lineWidth * 2));
								// y1 -= ((c - (nc - 1)) * Renderer.lineWidth) /
								// 8.0f;
								// x1 -= ((c - (nc - 1)) * Renderer.lineWidth) /
								// 8.0f;
								g.vertex(x1, y1);
								xs[vertexCount] = x1 + (lineWidth * c);
								ys[vertexCount] = y1 + (lineWidth * c);
								vertexCount++;
							} else {
								if (drawing) {

									for (int i1 = vertexCount - 2; i1 >= 0; i1--) {
										float dx = xs[i1 + 1] - xs[i1];
										float dy = ys[i1 + 1] - ys[i1];
										float a = (float) Math.atan2(dx, dy);
										a = (float) ((a + Math.PI) / (Math.PI * 2));
										g.fill(Color.HSBtoRGB(c / (float) nc,
												1.0f, a));
										g.beginShape(PConstants.QUADS);
										g.vertex(xs[i1], ys[i1]);
										g.vertex(xs[i1 + 1], ys[i1 + 1]);
										g.vertex(xs[i1 + 1] + lineWidth,
												ys[i1 + 1] + lineWidth);
										g.vertex(xs[i1] + lineWidth, ys[i1]
												+ lineWidth);
										g.endShape(PConstants.CLOSE);
									}

									vertexCount = 0;
									drawing = false;
								}
							}
						}
						if (drawing) {

							for (int i1 = vertexCount - 2; i1 >= 0; i1--) {
								float dx = xs[i1 + 1] - xs[i1];
								float dy = ys[i1 + 1] - ys[i1];
								float a = (float) Math.atan2(dx, dy);
								a = (float) ((a + Math.PI) / (Math.PI * 2));
								g.fill(Color.HSBtoRGB(c / (float) nc, 1.0f, a));
								g.beginShape(PConstants.QUADS);
								g.vertex(xs[i1], ys[i1]);
								g.vertex(xs[i1 + 1], ys[i1 + 1]);
								g.vertex(xs[i1 + 1] + lineWidth, ys[i1 + 1]
										+ lineWidth);
								g.vertex(xs[i1] + lineWidth, ys[i1] + lineWidth);
								g.endShape(PConstants.CLOSE);
							}

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