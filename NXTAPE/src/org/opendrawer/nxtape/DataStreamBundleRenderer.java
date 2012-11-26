package org.opendrawer.nxtape;

import java.awt.Color;

import org.opendrawer.dawinian.neurodynamics.DataStreamBundle;

import processing.core.PGraphics;

public class DataStreamBundleRenderer extends Renderer {

	protected DataStreamBundle dataStreamBundle;

	public DataStreamBundleRenderer(DataStreamBundle dataStreamBundle, float x,
			float y, float width, float height) {
		super(x, y, width, height);
		this.dataStreamBundle = dataStreamBundle;
	}

	public DataStreamBundleRenderer(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	@Override
	public void draw(PGraphics g) {
		g.stroke(64, 64, 64);
		g.strokeWeight(NXT_ArtificialPlasticityEcology.lineWidth);
		g.line(getStreamLeft(), y + height / 2, x + width, y + height / 2);
		int nc = dataStreamBundle.getDataStreams().size();
		for (int c = nc - 1; c >= 0; c--) {
			float xx = 0, yy = 0;
			boolean started = false;
			int dataWidth = dataStreamBundle.getDataWidth();
			g.stroke(Color.HSBtoRGB(c / (float) nc, 1.0f, 1.0f));
			for (int i = 0; i < dataWidth; i++) {
				double v = dataStreamBundle.read(i, c);
				if (v != Float.NaN) {
					float x1 = (getStreamLeft())
							+ ((i / (float) (dataWidth - 1)) * (width - (getStreamLeft() - x)));
					float y1 = (float) ((y + height / 2) - v * height / 2);
					if (started)
						g.line(x1, y1, xx, yy);
					started = true;
					xx = x1;
					yy = y1;
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