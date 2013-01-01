package org.opendrawer.ape.processing.renderers;

import org.opendrawer.ape.darwinianneurodynamics.StateStream;
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
		if (stateStreamBundle != null) {
			g.noStroke();
			int nc = stateStreamBundle.getStateStreams().size();
			float streamHeight = height - Renderer.lineMarginWidth * 4;
			float streamWidth = (width - Renderer.lineMarginWidth * 2)
					- getStreamRightMargin();
			for (int c = 0; c < nc; c++) {
				StateStream stateStream = stateStreamBundle.getStateStreams()
						.get(c);
				double[] states = stateStream.read();
				if (states != null) {
					int streamLength = states.length;
					double min = stateStream.getMin();
					double max = stateStream.getMax();
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
							x1 = ((x + streamWidth + Renderer.lineMarginWidth) - ((i / (float) (streamLength - 1)) * streamWidth));
							float subStep = stateStream.getSubStep();
							if (subStep > 0)
								x1 -= subStep
										* (1 / (float) (streamLength - 1))
										* streamWidth;
							y1 = (float) ((y + height - Renderer.lineMarginWidth * 2) - v
									* streamHeight);
							g.vertex(x1, y1);
							xs[vertexCount] = x1;
							ys[vertexCount] = y1;
							vertexCount++;
						} else {
							if (drawing) {
								g.fill(createKeyColourInt(c, nc));
								g.beginShape(PConstants.QUAD_STRIP);
								for (int i1 = vertexCount - 1; i1 >= 0; i1--) {
									g.vertex(xs[i1], ys[i1]
											- Renderer.lineMarginWidth / 2);
									g.vertex(xs[i1], ys[i1]
											+ Renderer.lineMarginWidth / 2);
								}
								g.endShape();
								vertexCount = 0;
								drawing = false;
							}
						}
					}
					if (drawing) {
						g.fill(createKeyColourInt(c, nc));
						g.beginShape(PConstants.QUAD_STRIP);
						for (int i1 = vertexCount - 1; i1 >= 0; i1--) {
							g.vertex(xs[i1], ys[i1] - Renderer.lineMarginWidth
									/ 2);
							g.vertex(xs[i1], ys[i1] + Renderer.lineMarginWidth
									/ 2);
						}
						g.endShape();
						drawing = false;
					}
				}
			}
		}
	}

	protected float getStreamRightMargin() {
		return 0;
	}

	public StateStreamBundle getStatesStreamBundle() {
		return stateStreamBundle;
	}
}