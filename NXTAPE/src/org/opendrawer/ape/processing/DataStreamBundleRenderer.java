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
					if (!Double.isNaN(min) && !Double.isNaN(max)
							&& (max - min > 0)) {
						g.stroke(Color.HSBtoRGB(c / (float) nc, 1.0f, 1.0f));
						boolean drawing = false;
						for (int i = 0; i < dataWidth; i++) {
							double v = data[i];
							if (!Double.isNaN(v)) {
								if (!drawing) {
									g.beginShape();
									drawing = true;
								}
								v = (v - min) / (max - min);
								float x1 = (getStreamLeft())
										+ ((i / (float) (dataWidth - 1)) * (width - (getStreamLeft() - x)));
								float y1 = (float) ((y + height) - v
										* graphHeight);
								g.vertex(x1, y1);
							} else {
								if (drawing) {
									g.endShape();
									drawing = false;
								}
							}
						}
						if (drawing) {
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