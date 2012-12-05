package org.opendrawer.ape.processing;

import java.awt.Color;

import org.opendrawer.ape.darwinianneurodynamics.DataStreamBundle;

import processing.core.PGraphics;

public class DataStreamBundleRenderer extends Renderer {

	protected DataStreamBundle dataStreamBundle;

	public DataStreamBundleRenderer(DataStreamBundle dataStreamBundle) {
		super();
		this.dataStreamBundle = dataStreamBundle;
	}

	public DataStreamBundleRenderer(DataStreamBundle dataStreamBundle, float x,
			float y, float width, float height) {
		super();
		this.dataStreamBundle = dataStreamBundle;
	}

	public DataStreamBundleRenderer(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	@Override
	public void draw(PGraphics g) {
		super.draw(g);
		if (dataStreamBundle != null) {
			g.noFill();
			int nc = dataStreamBundle.getDataStreams().size();
			float graphHeight = height - Renderer.lineWidth / 2.0f;
			for (int c = nc - 1; c >= 0; c--) {
				double[] data = dataStreamBundle.getDataStreams().get(c).read();
				if (data != null) {
					int dataWidth = data.length;
					double min = Double.NaN;
					double max = Double.NaN;
					for (int i = 0; i < dataWidth; i++) {
						if (!Double.isNaN(data[i])) {
							if (Double.isNaN(min))
								min = data[i];
							else if (data[i] < min)
								min = data[i];
							if (Double.isNaN(max))
								max = data[i];
							else if (data[i] > max)
								max = data[i];
						}
					}
					if (!(Double.isNaN(min) || Double.isNaN(max) || max == min)) {
						g.stroke(Color.HSBtoRGB(c / (float) nc, 1.0f, 1.0f));
						g.strokeWeight(Renderer.lineWidth);
						boolean drawing = false;
						int vertexCount = 0;
						float x1 = 0;
						float y1 = 0;
						for (int i = 0; i < dataWidth; i++) {
							double v = data[i];
							if (!Double.isNaN(v)) {
								if (!drawing) {
									g.beginShape();
									drawing = true;
									vertexCount = 0;
								}
								v = (v - min) / (max - min);
								x1 = (getStreamLeft())
										+ ((i / (float) (dataWidth - 1)) * (width - (getStreamLeft() - x)));
								y1 = (float) ((y + height) - v * graphHeight);
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

	public DataStreamBundle getDataStreamBundle() {
		return dataStreamBundle;
	}

}