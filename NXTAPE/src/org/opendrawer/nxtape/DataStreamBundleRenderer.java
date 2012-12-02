package org.opendrawer.nxtape;

import java.awt.Color;

import org.opendrawer.dawinian.neurodynamics.DataStreamBundle;

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
			float graphHeight = height
					- NXT_ArtificialPlasticityEcology.lineWidth / 2.0f;
			for (int c = nc - 1; c >= 0; c--) {
				int dataWidth = dataStreamBundle.getDataWidth();
				g.stroke(Color.HSBtoRGB(c / (float) nc, 1.0f, 1.0f));
				g.beginShape();
				for (int i = 0; i < dataWidth; i++) {
					double v = dataStreamBundle.getDataStreams().get(c)
							.readWithMinMaxScaledToZeroOne(i);
					if (!Double.isNaN(v)) {
						float x1 = (getStreamLeft())
								+ ((i / (float) (dataWidth - 1)) * (width - (getStreamLeft() - x)));
						float y1 = (float) ((y + height) - v * graphHeight);
						g.vertex(x1, y1);
					}
				}
				g.endShape();
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